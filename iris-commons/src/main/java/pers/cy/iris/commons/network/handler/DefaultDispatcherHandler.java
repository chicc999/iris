package pers.cy.iris.commons.network.handler;

import pers.cy.iris.commons.exception.ServiceTooBusyException;
import pers.cy.iris.commons.exception.UnknowCommandException;
import pers.cy.iris.commons.network.HandlerTask;
import pers.cy.iris.commons.network.ResponseFuture;
import pers.cy.iris.commons.network.protocol.Acknowledge;
import pers.cy.iris.commons.network.protocol.Command;
import pers.cy.iris.commons.network.protocol.Header;
import pers.cy.iris.commons.network.protocol.HeaderType;
import pers.cy.iris.commons.network.protocol.response.ErrorResponse;
import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;

/**
 * command派发handler
 */
@ChannelHandler.Sharable
public class DefaultDispatcherHandler extends SimpleChannelInboundHandler<Command> {

	private static final Logger logger = LoggerFactory.getLogger(DefaultDispatcherHandler.class);

	private CommandHandlerFactory handlerFactory;

	// 存放同步和异步命令应答
	protected final Map<Integer, ResponseFuture> futures;

	//异步处理请求或者响应回调的线程池
	protected ExecutorService serviceExecutor;

	public DefaultDispatcherHandler(CommandHandlerFactory handlerFactory,ExecutorService serviceExecutor,Map<Integer, ResponseFuture> futures) {
		this.handlerFactory = handlerFactory;
		this.serviceExecutor = serviceExecutor;
		this.futures = futures;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Command command) throws Exception {
		if (command == null) {
			return;
		}

		logger.debug("dispatcher command : {} {}", command.getHeader().getHeaderType().toString(), command.getHeader().getTypeString());

		HeaderType headerType = command.getHeader().getHeaderType();
		switch (headerType) {
			case REQUEST: { // 如果是请求命令， 做请求处理
				try {
					processRequest(ctx, command);
				}catch (Exception e){
					logger.error(e.getMessage(), e);
					//如果请求需要响应
					if (command.getHeader().getAcknowledge() != Acknowledge.ACK_NO) {
						//写出响应,如果出现异常则调用exceptionCaught打印异常关闭连接
						ctx.writeAndFlush(new ErrorResponse(-1, e.toString(), command.getRequestId()))
								.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
					}
				}
				break;
			}
			case RESPONSE: { // 如果是响应命令， 做响应处理
				processResponse(ctx, command);
				break;
			}
		}
	}

	private void processResponse(final ChannelHandlerContext ctx, final Command command) {
		Header header = command.getHeader();
		// 超时被删除了
		final ResponseFuture responseFuture = futures.remove(header.getRequestId());
		if (responseFuture == null) {
			logger.info("ack type:" + header.getTypeString() + " requestId:" + header
						.getRequestId() + " but responseFuture is null");
			return;
		}

		responseFuture.setResponse(command);
		//回调交给线程池,避免回调任务阻塞IO线程
		try {
			serviceExecutor.submit(new Runnable() {
				@Override
				public void run() {
					responseFuture.done();
				}
			});
		}catch (RejectedExecutionException e){
			//队列已满,拒绝执行回调
			responseFuture.cancel(new ServiceTooBusyException("请求成功,但没有足够的资源执行回调",e));
		}

	}

	private void processRequest(ChannelHandlerContext ctx, Command command) throws Exception{
		int type = command.getHeader().getType();
		if (Command.HEARTBEAT == type) {
			//双向心跳,无需处理
			return;
		}

		Header header = command.getHeader();

		CommandHandler handler = handlerFactory.getHandler(header.getType());
		if(handler == null){
			throw new UnknowCommandException("处理"+ header.getTypeString() + "的handler不存在");
		}

		try {
			HandlerTask task = new HandlerTask(ctx, command, handler);
			ExecutorService commandExecutor = handler.getExecutorService(command);
			if (commandExecutor == null) {
				// 如果没用指定处理该命令专用的线程池，则在普通业务线程池执行
				this.serviceExecutor.submit(task);
			} else {
				commandExecutor.submit(task);
			}
		} catch (RejectedExecutionException e) {
			throw new ServiceTooBusyException("too many requests and thread pool is busy, reject request %d from %s ",e);
		}

	}

}

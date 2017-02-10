package com.cy.iris.commons.network.handler;

import com.cy.iris.commons.exception.UnknowCommandException;
import com.cy.iris.commons.network.ResponseFuture;
import com.cy.iris.commons.network.protocol.Acknowledge;
import com.cy.iris.commons.network.protocol.Command;
import com.cy.iris.commons.network.protocol.Header;
import com.cy.iris.commons.network.protocol.HeaderType;
import com.cy.iris.commons.network.protocol.response.ErrorResponse;
import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * command派发handler
 */
@ChannelHandler.Sharable
public class DefaultDispatcherHandler extends SimpleChannelInboundHandler<Command> {

	private static final Logger logger = LoggerFactory.getLogger(DefaultDispatcherHandler.class);

	private CommandHandlerFactory handlerFactory;

	// 存放同步和异步命令应答
	protected final Map<Integer, ResponseFuture> futures;

	public DefaultDispatcherHandler(CommandHandlerFactory handlerFactory,Map<Integer, ResponseFuture> futures) {
		this.handlerFactory = handlerFactory;
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

	private void processResponse(ChannelHandlerContext ctx, Command command) {
		Header header = command.getHeader();
		// 超时被删除了
		final ResponseFuture responseFuture = futures.get(header.getRequestId());
		if (responseFuture == null) {
			logger.info("ack type:" + header.getTypeString() + " requestId:" + header
						.getRequestId() + " but responseFuture is null");

			return;
		}

		responseFuture.setResponse(command);
		responseFuture.done();

	}

	private void processRequest(ChannelHandlerContext ctx, Command command) throws Exception{
		int type = command.getHeader().getType();
		if (Command.HEARTBEAT == type) {
			//双向心跳,无需处理
			return;
		}

		CommandHandler handler = handlerFactory.getHandler(command.getHeader().getType());
		if(handler == null){
			throw new UnknowCommandException("处理"+ command.getHeader().getTypeString() + "的handler不存在");
		}


		handler.process(ctx,command);

	}


	/**
	 * 仅为系统错误才在这里回复并关闭网络,正常业务错误不关闭网络
	 * @param ctx
	 * @param cause
	 * @throws Exception
	 */
	@Override
	public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
		logger.error(cause.getMessage(), cause);
		ctx.close();
	}

}

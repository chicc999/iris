package com.cy.iris.commons.network.handler;

import com.cy.iris.commons.network.protocol.Command;
import com.cy.iris.commons.network.protocol.HeaderType;
import com.cy.iris.commons.network.protocol.response.ErrorResponse;
import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * command派发handler
 */
@ChannelHandler.Sharable
public class DefaultDispatcherHandler extends SimpleChannelInboundHandler<Command> {

	private static final Logger logger = LoggerFactory.getLogger(DefaultDispatcherHandler.class);

	private CommandHandlerFactory handlerFactory;

	public DefaultDispatcherHandler(CommandHandlerFactory handlerFactory) {
		this.handlerFactory = handlerFactory;
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
				processRequest(ctx, command);
				break;
			}
			case RESPONSE: { // 如果是响应命令， 做响应处理
				processResponse(ctx, command);
				break;
			}
		}
	}

	private void processResponse(ChannelHandlerContext ctx, Command command) {


	}

	private void processRequest(ChannelHandlerContext ctx, Command command) {
		int type = command.getHeader().getType();
		if (Command.HEARTBEAT == type) {
			//双向心跳,无需处理
			return;
		}

		CommandHandler handler = handlerFactory.getHandler(command.getHeader().getType());
		try {
			handler.process(ctx,command);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
		logger.error(cause.getMessage(), cause);
		ctx.writeAndFlush(new ErrorResponse(-1,cause.toString())).addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				if(!future.isSuccess() && null != future.cause() ){
					logger.error("回写response失败",future.cause());
				}
				//网络模块发生解析异常直接关闭连接,应用层仅仅回复错误响应
				ctx.close();
			}
		});
	}

}

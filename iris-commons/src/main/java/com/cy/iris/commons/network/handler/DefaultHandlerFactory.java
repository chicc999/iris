package com.cy.iris.commons.network.handler;

import com.cy.iris.commons.network.protocol.Command;
import com.cy.iris.commons.network.protocol.response.ErrorResponse;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ExecutorService;

/**
 * 默认的处理实现,提供公共的处理方法
 */
public class DefaultHandlerFactory implements CommandHandlerFactory{

	private DefaultHandler handler = new DefaultHandler();

	@Override
	public CommandHandler getHandler(int type) {
		return handler;
	}
}

class DefaultHandler implements CommandHandler{

	@Override
	public Command process(ChannelHandlerContext ctx, Command command) throws Exception {
		return new ErrorResponse(-1,"请实现CommandHandlerFactory和CommandHandler以代替默认实现",command.getRequestId());
	}

	@Override
	public ExecutorService getExecutorService(Command command) {
		return null;
	}
}

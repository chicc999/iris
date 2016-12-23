package com.cy.iris.commons.network.handler;

/**
 * 默认的处理实现,提供公共的处理方法
 */
public class DefaultHandlerFactory implements CommandHandlerFactory{

	protected SessionHandler sessionHandler;

	@Override
	public CommandHandler getHandler(int type) {
		return null;
	}
}

package com.cy.iris.commons.network;

import com.cy.iris.commons.network.handler.CommandHandler;
import com.cy.iris.commons.network.protocol.Command;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by cy on 16/12/29.
 */
public class HandlerTask implements Runnable{

	private ChannelHandlerContext ctx;
	private Command request;
	private CommandHandler handler;

	public HandlerTask(ChannelHandlerContext ctx, Command request, CommandHandler handler) {
		this.ctx = ctx;
		this.request = request;
		this.handler = handler;
	}

	@Override
	public void run() {

	}
}

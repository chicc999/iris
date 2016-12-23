package com.cy.iris.commons.network.handler;

import com.cy.iris.commons.network.protocol.Command;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by cy on 16/12/23.
 */
public class DispatcherHandler extends SimpleChannelInboundHandler<Command> {
	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, Command command) throws Exception {

	}
}

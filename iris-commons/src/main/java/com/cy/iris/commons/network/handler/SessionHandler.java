package pers.cy.iris.commons.network.handler;

import pers.cy.iris.commons.network.protocol.*;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ExecutorService;

/**
 * Created by cy on 16/11/11.
 */
public class SessionHandler implements CommandHandler{

	@Override
	public Command process(ChannelHandlerContext ctx, Command command) throws Exception {
		return null;
	}

	@Override
	public ExecutorService getExecutorService(Command command) {
		return null;
	}
}

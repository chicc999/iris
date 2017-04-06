package pers.cy.iris.coordinator.handler;

import pers.cy.iris.commons.network.handler.CommandHandler;
import pers.cy.iris.commons.network.protocol.Command;
import pers.cy.iris.commons.network.protocol.response.ErrorResponse;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ExecutorService;

/**
 * Created by cy on 17/2/14.
 */
public class GetClusterHandler implements CommandHandler{
	@Override
	public Command process(ChannelHandlerContext ctx, Command command) throws Exception {
		return new ErrorResponse(200,"实验",command.getRequestId());
	}

	@Override
	public ExecutorService getExecutorService(Command command) {
		return null;
	}
}

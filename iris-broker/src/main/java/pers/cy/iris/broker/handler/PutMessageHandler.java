package pers.cy.iris.broker.handler;

import io.netty.channel.ChannelHandlerContext;
import pers.cy.iris.broker.store.Store;
import pers.cy.iris.commons.network.handler.CommandHandler;
import pers.cy.iris.commons.network.protocol.Command;
import pers.cy.iris.commons.network.protocol.request.PutMessage;
import pers.cy.iris.commons.network.protocol.response.ErrorResponse;

import java.util.concurrent.ExecutorService;

/**
 * @Author:cy
 * @Date:Created in  17/4/14
 * @Destription:
 */
public class PutMessageHandler implements CommandHandler{
	protected Store store;
	@Override
	public Command process(ChannelHandlerContext ctx, Command command) throws Exception {
		PutMessage putMessage = (PutMessage)command;
		System.out.println("收到putMessage请求,"+new String(putMessage.getMessages()[0].getBody()));
		return new ErrorResponse(200,"",command.getRequestId());
	}

	@Override
	public ExecutorService getExecutorService(Command command) {
		return null;
	}
}

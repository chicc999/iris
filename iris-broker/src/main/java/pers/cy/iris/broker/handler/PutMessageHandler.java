package pers.cy.iris.broker.handler;

import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import pers.cy.iris.broker.store.Store;
import pers.cy.iris.commons.model.message.Message;
import pers.cy.iris.commons.model.message.StoreMessage;
import pers.cy.iris.commons.network.handler.CommandHandler;
import pers.cy.iris.commons.network.protocol.Command;
import pers.cy.iris.commons.network.protocol.request.PutMessage;
import pers.cy.iris.commons.network.protocol.response.ErrorResponse;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

/**
 * @Author:cy
 * @Date:Created in  17/4/14
 * @Destription:
 */
public class PutMessageHandler implements CommandHandler{

	@Autowired
	protected Store store;

	@Override
	public Command process(ChannelHandlerContext ctx, Command command) throws Exception {
		PutMessage putMessage = (PutMessage)command;
		System.out.println("收到putMessage请求,"+new String(putMessage.getMessages()[0].getBody()));
		Message[] messages = putMessage.getMessages();
		for(Message message : messages) {
			store.putMessage((StoreMessage) message);
		}
		return new ErrorResponse(200,"",command.getRequestId());
	}

	@Override
	public ExecutorService getExecutorService(Command command) {
		return null;
	}
}

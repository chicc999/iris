package pers.cy.iris.broker.handler;

import pers.cy.iris.commons.network.handler.CommandHandler;
import pers.cy.iris.commons.network.handler.CommandHandlerFactory;
import pers.cy.iris.commons.network.protocol.Command;

/**
 * @Author:cy
 * @Date:Created in  17/4/14
 * @Destription:
 */
public class BrokerHandlerFactory implements CommandHandlerFactory{

	private PutMessageHandler putMessageHandler = new PutMessageHandler();
	@Override
	public CommandHandler getHandler(int type) {
		switch (type){
			case Command.PUT_MESSAGE:
				return putMessageHandler;
			default:
		}
		return null;
	}
}

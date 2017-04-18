package pers.cy.iris.broker.handler;

import org.springframework.beans.factory.annotation.Autowired;
import pers.cy.iris.commons.network.handler.CommandHandler;
import pers.cy.iris.commons.network.handler.CommandHandlerFactory;
import pers.cy.iris.commons.network.protocol.Command;

/**
 * @Author:cy
 * @Date:Created in  17/4/14
 * @Destription:
 */
public class BrokerHandlerFactory implements CommandHandlerFactory{

	@Autowired
	private PutMessageHandler putMessageHandler ;

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

package pers.cy.iris.coordinator.handler;

import pers.cy.iris.commons.network.handler.CommandHandler;
import pers.cy.iris.commons.network.handler.CommandHandlerFactory;
import pers.cy.iris.commons.network.handler.SessionHandler;

/**
 * Created by cy on 17/2/14.
 */
public class CoordinatorHandlerFactory implements CommandHandlerFactory {

	private SessionHandler sessionHandler;

	@Override
	public CommandHandler getHandler(int type) {
		return new GetClusterHandler();
	}
}

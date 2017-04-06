package pers.cy.iris.client.producer;

import pers.cy.iris.commons.service.LifeCycle;

/**
 * Created by cy on 17/2/12.
 */
interface Producer extends LifeCycle{
	void send();
}

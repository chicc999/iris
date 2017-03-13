package com.cy.iris.client.producer;

import com.cy.iris.commons.service.LifeCycle;

/**
 * Created by cy on 17/2/12.
 */
interface Producer extends LifeCycle{
	void send();
}

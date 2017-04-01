package com.cy.iris.commons.cluster.event;

import com.cy.iris.commons.cluster.ClusterEvent;

/**
 * @Author:cy
 * @Date:Created in  17/4/1
 * @Destription:
 */
public class AllBrokerUpdateEvent extends ClusterEvent {
	public AllBrokerUpdateEvent() {
		this.type = ClusterEventType.ALL_BROKER_UPDATE;
	}
}

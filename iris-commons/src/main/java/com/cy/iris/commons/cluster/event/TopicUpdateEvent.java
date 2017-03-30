package com.cy.iris.commons.cluster.event;

import com.cy.iris.commons.cluster.ClusterEvent;

/**
 * @Author:cy
 * @Date:Created in  17/3/30
 * @Destription:
 */
public class TopicUpdateEvent extends ClusterEvent{
	public TopicUpdateEvent() {
		this.type = ClusterEventType.TOPIC_UPDATE;
	}
}

package com.cy.iris.broker.MetaManager;

import com.cy.iris.commons.cluster.ClusterEvent;
import com.cy.iris.commons.util.eventmanager.EventListener;

/**
 * @Author:cy
 * @Date:Created in  17/4/2
 * @Destription: 监听器处理集群事件
 */
public class ClusterEventListener implements EventListener<ClusterEvent>{
	@Override
	public void onEvent(ClusterEvent event) {
		switch (event.getType()) {
			case ALL_BROKER_UPDATE:
				//TODO
				break;
			case TOPIC_UPDATE:
				//TODO
				break;
			default:
				return;
		}

	}
}

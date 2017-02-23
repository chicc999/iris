package com.cy.iris.coordinator.cluster;

import java.io.Serializable;

/**
 * Created by cy on 17/2/21.
 */
public class TopicConfig implements Serializable{
	private static final long serialVersionUID = 5674581453999776697L;

	private String topic;

	public TopicConfig(String topic) {
		this.topic = topic;
	}
}

package com.cy.iris.commons.cluster.event;

import com.cy.iris.commons.cluster.ClusterEvent;

/**
 * @Author:cy
 * @Date:Created in  17/4/1
 * @Destription:
 */
public class UpdateExceptionEvent extends ClusterEvent{

	private Exception exception;

	public UpdateExceptionEvent(Exception exception) {
		this.exception = exception;
		this.type = ClusterEventType.UPDATE_EXCEPTION;
	}

	public Exception getException() {
		return exception;
	}
}

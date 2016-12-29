package com.cy.iris.commons.network.protocol;

import com.cy.iris.commons.network.protocol.request.GetCluster;
import com.cy.iris.commons.network.protocol.request.HeartBeat;

/**
 * Created by cy on 16/12/28.
 */
public class CommandFactory {
	public static Command create(Header header) {
		if (header == null) {
			return null;
		}

		switch (header.getType()) {
			case Command.HEARTBEAT:
				return new HeartBeat();
			case Command.GET_CLUSTER:
				return new GetCluster();
		}
		return null;
	}
}

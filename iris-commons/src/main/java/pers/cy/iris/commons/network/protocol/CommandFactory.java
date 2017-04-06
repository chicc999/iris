package pers.cy.iris.commons.network.protocol;

import pers.cy.iris.commons.exception.UnknowCommandException;
import pers.cy.iris.commons.network.protocol.request.GetCluster;
import pers.cy.iris.commons.network.protocol.request.HeartBeat;
import pers.cy.iris.commons.network.protocol.response.ErrorResponse;

/**
 * Created by cy on 16/12/28.
 */
public class CommandFactory {
	public static Command create(Header header) throws UnknowCommandException{
		if (header == null) {
			return null;
		}

		switch (header.getType()) {
			case Command.HEARTBEAT:
				return new HeartBeat(header);
			case Command.GET_CLUSTER:
				return new GetCluster(header);
			case Command.BOOLEAN_ACK:
				return new ErrorResponse(header);
		}
		throw new UnknowCommandException("未定义的协议" + header.getType());
	}
}

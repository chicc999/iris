package pers.cy.iris.commons.network.protocol.request;

import io.netty.buffer.ByteBuf;
import pers.cy.iris.commons.network.protocol.Command;

/**
 * @Author:cy
 * @Date:Created in  17/4/6
 * @Destription:
 */
public class PutMessage extends Command{
	@Override
	protected ByteBuf encodeBody() {
		return null;
	}

	@Override
	protected void decodeBody(ByteBuf in) {

	}
}

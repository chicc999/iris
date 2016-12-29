package com.cy.iris.commons.network.protocol.request;

import com.cy.iris.commons.network.protocol.Command;
import com.cy.iris.commons.network.protocol.Header;
import com.cy.iris.commons.network.protocol.HeaderType;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;


/**
 * Created by cy on 16/12/28.
 */
public class HeartBeat extends Command{

	public HeartBeat() {
		this.header.Type(HEARTBEAT).HeaderType(HeaderType.REQUEST);
	}

	@Override
	protected ByteBuf encodeBody() {
		ByteBuf body = PooledByteBufAllocator.DEFAULT.buffer();
		return body;
	}

	@Override
	protected void decodeBody(ByteBuf in)  {

	}

	@Override
	public String getTypeString() {
		return "HEART_BEAT";
	}
}

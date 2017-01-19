package com.cy.iris.commons.network.protocol.response;

import com.cy.iris.commons.network.protocol.Command;
import com.cy.iris.commons.network.protocol.Header;
import com.cy.iris.commons.network.protocol.HeaderType;
import com.cy.iris.commons.util.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

/**
 * Created by cy on 17/1/3.
 */
public class ErrorResponse extends Command {



	public ErrorResponse(Header header) {
		this.header = header;
		this.header.typeString("ERROR_RESPONSE");
	}

	public ErrorResponse(int status, String error) {
		super(new Header(HeaderType.RESPONSE,HEARTBEAT).typeString("ERROR_RESPONSE"));
		this.header.setStatus(status);
		this.header.setError(error);
	}

	@Override
	protected ByteBuf encodeBody() {
		ByteBuf body = PooledByteBufAllocator.DEFAULT.buffer();
		return body;
	}

	@Override
	protected void decodeBody(ByteBuf in) {
		return;
	}
}

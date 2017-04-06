package pers.cy.iris.commons.network.protocol.response;

import pers.cy.iris.commons.network.protocol.Command;
import pers.cy.iris.commons.network.protocol.Header;
import pers.cy.iris.commons.network.protocol.HeaderType;
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
		super(new Header(HeaderType.RESPONSE,BOOLEAN_ACK).typeString("ERROR_RESPONSE"));
		this.header.setStatus(status);
		this.header.setError(error);
	}

	public ErrorResponse(int status, String error,int requestId) {
		super(new Header(HeaderType.RESPONSE,BOOLEAN_ACK).typeString("ERROR_RESPONSE"));
		this.header.setStatus(status);
		this.header.setError(error);
		this.header.setRequestId(requestId);
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

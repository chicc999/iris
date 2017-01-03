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

	// 响应状态码
	private int status;
	// 响应错误信息
	private String error;

	public ErrorResponse() {
		super(new Header(HeaderType.RESPONSE,HEARTBEAT).typeString("ERROR_RESPONSE"));
	}

	public ErrorResponse(int status, String error) {
		super(new Header(HeaderType.RESPONSE,HEARTBEAT).typeString("ERROR_RESPONSE"));
		this.status = status;
		this.error = error;
	}

	@Override
	protected ByteBuf encodeBody() {
		ByteBuf body = PooledByteBufAllocator.DEFAULT.buffer();
		body.writeInt(this.status);
		Serializer.write(this.error,body);
		return body;
	}

	@Override
	protected void decodeBody(ByteBuf in) {
		this.status = in.readInt();
		this.error = Serializer.read(in);
	}
}

package com.cy.iris.commons.network.protocol.request;

import com.cy.iris.commons.network.protocol.Command;
import com.cy.iris.commons.network.protocol.Header;
import com.cy.iris.commons.network.protocol.HeaderType;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

/**
 * Created by cy on 16/12/29.
 */
public class GetCluster extends Command{
	// 应用
	private String app;
	// 客户端ID
	private String clientId;
	// 客户端所在数据中心
	private byte dataCenter;

	public GetCluster() {
		super(new Header(HeaderType.REQUEST,GET_CLUSTER));
	}

	@Override
	public String getTypeString() {
		return "GET_CLUSTER";
	}

	@Override
	protected ByteBuf encodeBody() {
		ByteBuf body = PooledByteBufAllocator.DEFAULT.buffer();
		return body;
	}

	@Override
	protected void decodeBody(ByteBuf in) {

	}
}

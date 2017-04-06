package com.cy.iris.commons.network.protocol.request;

import com.cy.iris.commons.network.protocol.Command;
import com.cy.iris.commons.network.protocol.Header;
import com.cy.iris.commons.network.protocol.HeaderType;
import com.cy.iris.commons.util.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;

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
		super(new Header(HeaderType.REQUEST,GET_CLUSTER).typeString("GET_CLUSTER"));
	}

	public GetCluster(Header header) {
		super(header);
		this.header.typeString("GET_CLUSTER");
	}

	public String getApp() {
		return app;
	}

	public void setApp(String app) {
		this.app = app;
	}

	public GetCluster app(String app) {
		this.app = app;
		return this;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public GetCluster clientId(String clientId) {
		this.clientId = clientId;
		return this;
	}

	public byte getDataCenter() {
		return dataCenter;
	}

	public void setDataCenter(byte dataCenter) {
		this.dataCenter = dataCenter;
	}

	public GetCluster dataCenter(byte dataCenter) {
		this.dataCenter = dataCenter;
		return this;
	}

	@Override
	protected ByteBuf encodeBody() {
		ByteBuf body = PooledByteBufAllocator.DEFAULT.buffer();
		Serializer.write(app,body);
		Serializer.write(clientId,body);
		body.writeByte(dataCenter);
		return body;
	}

	@Override
	protected void decodeBody(ByteBuf in) {
		this.app = Serializer.read(in);
		this.clientId = Serializer.read(in);
		this.dataCenter = in.readByte();
	}
}

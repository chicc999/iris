package com.cy.iris.commons.network.protocol;

import com.cy.iris.commons.util.ArgumentUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;

import java.nio.ByteBuffer;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 命令格式
 * +--------+------+
 * | header | body |
 * +--------+------+
 */
public abstract class Command {

	// 心跳
	public static final int HEARTBEAT = 0;
	// 获取集群
	public static final int GET_CLUSTER = 1;


	// 头部
	protected Header header;

	protected Command() {
		this.header = new Header();
	}

	public Command(Header header) {
		this.header = header;
	}

	public ByteBuf encodeHeader() throws Exception {
		ByteBuf out = PooledByteBufAllocator.DEFAULT.buffer();

		//header序列化
		return header.encode(out);

	}

	public Header getHeader() {
		return header;
	}

	public int getRequestId() {
		return header.getRequestId();
	}

	protected abstract ByteBuf encodeBody() ;

	protected abstract void decodeBody(ByteBuf in) ;

	@Override
	public String toString() {
		return this.header.toString();
	}

}

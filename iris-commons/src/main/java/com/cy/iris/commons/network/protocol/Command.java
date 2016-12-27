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
 * +---------------+--------+------------+------+
 * | header length | header | body length| body |
 * +---------------+--------+------------+------+
 * |    4 byte     |        |   4 byte   |
 */
public abstract class Command {

	// 头部
	protected Header header;

	protected Command() {

	}

	public Command(Header header) {
		this.header = header;
	}

	public void encode(ByteBuf out) throws Exception {
		ArgumentUtil.isNotNull("command encode ByteBuf",out);

		// header的length需要写完后才能知道
		int begin = out.writerIndex();
		//写入header长度,此时不知道,先占位
		out.writeInt(0);

		//header序列化
		header.encode(out);
		// 动态计算长度
		int headerEnd = out.writerIndex();
		out.writerIndex(begin);
		out.writeInt(headerEnd-begin-4);
		out.writerIndex(headerEnd);

		out.writeInt(0);
		encodeBody(out);
		int bodyEnd = out.writerIndex();
		out.writeInt(bodyEnd-headerEnd-4);
		out.writerIndex(bodyEnd);
	}

	abstract void encodeBody(ByteBuf out) throws Exception ;
}

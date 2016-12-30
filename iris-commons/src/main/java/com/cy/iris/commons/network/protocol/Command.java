package com.cy.iris.commons.network.protocol;

import com.cy.iris.commons.network.CommandCallback;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFutureListener;

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

	// 执行成功或失败以后的回调
	protected CommandCallback callback;

	//写网络成功或者失败以后的回调
	protected ChannelFutureListener listenner;

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

	public CommandCallback getCallback() {
		return callback;
	}

	public void setCallback(CommandCallback callback) {
		this.callback = callback;
	}

	public ChannelFutureListener getListenner() {
		return listenner;
	}

	public void setListenner(ChannelFutureListener listenner) {
		this.listenner = listenner;
	}

	protected abstract ByteBuf encodeBody() ;

	protected abstract void decodeBody(ByteBuf in) ;

	@Override
	public String toString() {
		return this.header.toString();
	}

}

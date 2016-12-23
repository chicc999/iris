package com.cy.iris.commons.network.handler;

import io.netty.channel.ChannelHandlerContext;

/**
 * 业务层处理心跳
 */
public interface HeartbeatHandler {
	/**
	 * 空闲事件
	 *
	 * @param ctx 上下文
	 */
	void heartbeat(ChannelHandlerContext ctx);
}

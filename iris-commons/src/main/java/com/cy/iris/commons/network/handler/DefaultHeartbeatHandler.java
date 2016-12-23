package com.cy.iris.commons.network.handler;

import io.netty.channel.ChannelHandlerContext;

/**
 * 默认心跳处理器
 * 向对方发一个无需回复的心跳信息,发生异常则关闭连接.
 */
public class DefaultHeartbeatHandler implements HeartbeatHandler {
	@Override
	public void heartbeat(final ChannelHandlerContext ctx) {
		// 心跳不用应答
/*		Header header = new Header(HeaderType.REQUEST, Command.HEARTBEAT);
		header.setAcknowledge(Acknowledge.ACK_NO);
		ctx.writeAndFlush(new Heartbeat(header)).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);*/
	}
}
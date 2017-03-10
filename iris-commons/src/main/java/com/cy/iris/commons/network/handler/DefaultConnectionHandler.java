package com.cy.iris.commons.network.handler;

import com.cy.iris.commons.network.protocol.request.HeartBeat;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 处理连接相关的事件,默认处理了心跳事件.
 * 如果要响应其他channel事件,重写对应方法.
 */
@ChannelHandler.Sharable
public class DefaultConnectionHandler extends ChannelDuplexHandler {

	private static final Logger logger = LoggerFactory.getLogger(DefaultConnectionHandler.class);

	@Override
	public void channelRegistered(final ChannelHandlerContext ctx) throws Exception {
		super.channelRegistered(ctx);
	}

	@Override
	public void channelUnregistered(final ChannelHandlerContext ctx) throws Exception {
		super.channelUnregistered(ctx);
	}

	@Override
	public void channelActive(final ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		logger.debug("连接 {} 断开",ctx.channel());
	}

	@Override
	public void userEventTriggered(final ChannelHandlerContext ctx, final Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			if (event.state().equals(IdleState.ALL_IDLE)) {
				logger.debug("channel {} is idle.", ctx.channel());
				ctx.writeAndFlush(new HeartBeat()).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
			}
		}
		super.userEventTriggered(ctx, evt);
	}

	/**
	 * 仅为系统错误才在这里回复并关闭网络,正常业务错误不关闭网络
	 * @param ctx
	 * @param cause
	 * @throws Exception
	 */
	@Override
	public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
		logger.error(cause.getMessage(), cause);
		ctx.close();
	}
}

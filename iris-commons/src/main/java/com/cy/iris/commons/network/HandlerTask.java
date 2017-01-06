package com.cy.iris.commons.network;

import com.cy.iris.commons.network.handler.CommandHandler;
import com.cy.iris.commons.network.protocol.Command;
import com.cy.iris.commons.network.protocol.Header;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by cy on 16/12/29.
 */
public class HandlerTask implements Runnable {

	private ChannelHandlerContext ctx;
	private Command request;
	private CommandHandler handler;

	public HandlerTask(ChannelHandlerContext ctx, Command request, CommandHandler handler) {
		this.ctx = ctx;
		this.request = request;
		this.handler = handler;
	}

	@Override
	public void run() {
		// 处理请求命令
		Header header = request.getHeader();
		Command response = null;
		try {
			response = handler.process(ctx, request);
		} catch (Throwable e) {
			//TODO 默认的错误响应,传回错误
		}
		response.getHeader().setRequestId(request.getRequestId());
		ChannelFutureListener listenner = response.getListenner() == null ? ChannelFutureListener.CLOSE_ON_FAILURE : response.getListenner();
		//服务端不处理commandCallback,直接在DispatcherHandler处理对应type.客户端则在发送时指定回调函数.
		ctx.writeAndFlush(response).addListener(listenner);
	}
}

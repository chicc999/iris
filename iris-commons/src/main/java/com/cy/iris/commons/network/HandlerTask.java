package pers.cy.iris.commons.network;

import pers.cy.iris.commons.network.handler.CommandHandler;
import pers.cy.iris.commons.network.protocol.Acknowledge;
import pers.cy.iris.commons.network.protocol.Command;
import pers.cy.iris.commons.network.protocol.Header;
import pers.cy.iris.commons.network.protocol.response.ErrorResponse;
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
			//如果请求需要答复
			if (request.getHeader().getAcknowledge() != Acknowledge.ACK_NO) {
				//写出响应,如果出现异常则调用exceptionCaught打印异常关闭连接
				ctx.writeAndFlush(new ErrorResponse(-1, e.toString(), request.getRequestId()))
						.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
			}
		}
		response.getHeader().setRequestId(request.getRequestId());
		ChannelFutureListener listenner = response.getListenner() == null ? ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE : response.getListenner();
		//服务端不处理commandCallback,直接在DispatcherHandler处理对应type.客户端则在发送时指定回调函数.
		ctx.writeAndFlush(response).addListener(listenner);
	}
}

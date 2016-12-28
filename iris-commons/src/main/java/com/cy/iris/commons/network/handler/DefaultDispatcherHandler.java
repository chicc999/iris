package com.cy.iris.commons.network.handler;

import com.cy.iris.commons.network.protocol.Command;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * command派发handler
 */
@ChannelHandler.Sharable
public class DefaultDispatcherHandler extends SimpleChannelInboundHandler<Command> {

	private static final Logger logger = LoggerFactory.getLogger(DefaultDispatcherHandler.class);

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Command command) throws Exception {
		processCommand(ctx, command);
	}

	private void processCommand(ChannelHandlerContext ctx, Command command) {
		if (command == null) {
			return;
		}
		logger.debug("dispatcher command : {} {}",command.getHeader().getHeaderType().toString(),command.getTypeString());
//		HeaderType headerType = command.getHeader().getHeaderType();
//		switch (headerType) {
//			case REQUEST: { // 如果是请求命令， 做请求处理
//				processRequest(ctx, command);
//				break;
//			}
//			case RESPONSE: { // 如果是响应命令， 做响应处理
//				processResponse(ctx, command);
//				break;
//			}
//		}
	}
}

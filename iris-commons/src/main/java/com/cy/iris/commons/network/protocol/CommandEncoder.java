package com.cy.iris.commons.network.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by cy on 16/12/28.
 */
public class CommandEncoder extends MessageToByteEncoder {

	private Logger logger = LoggerFactory.getLogger(CommandEncoder.class);

	@Override
	protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
		if(msg instanceof Command){
			Command command = (Command)msg;
			ByteBuf header = command.encodeHeader();

			logger.debug("encode : header len {}",header.readableBytes());

			out.writeInt(header.readableBytes());
			out.writeBytes(header);

			ByteBuf body = command.encodeBody();

			logger.debug("encode : body len {}",body.readableBytes());

			out.writeInt(body.readableBytes());
			out.writeBytes(body);
		}else{
			out.writeBytes((ByteBuf)msg);
		}
	}

}

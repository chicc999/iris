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

		Command command = (Command) msg;
		ByteBuf header = command.encodeHeader();

		logger.debug("encode : header len {}", header.readableBytes());

		ByteBuf body = command.encodeBody();

		logger.debug("encode : body len {}", body.readableBytes());
		//header,body长度以及描述他们所用的长度
		out.writeInt(header.readableBytes() + body.readableBytes() + 4 + 4);
		out.writeInt(header.readableBytes());
		out.writeBytes(header);
		out.writeInt(body.readableBytes());
		out.writeBytes(body);

	}

}

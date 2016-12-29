package com.cy.iris.commons.network.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by cy on 16/12/28.
 */
public class CommandDecoder extends ByteToMessageDecoder {

	private static final Logger logger = LoggerFactory.getLogger(CommandDecoder.class);

	private enum State {
		Header,
		Body
	}

	private State state = State.Header;

	private int headerSize = 0;

	private int bodySize = 0;

	private Header header;

	private Command command;

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

		switch (state){
			case Header:
				if(in.readableBytes()<4){
					return;
				}
				headerSize = in.readInt();
				logger.debug("decode : header len {}",headerSize);

				if(in.readableBytes()<headerSize){
					return;
				}

				header = new Header();
				header.decode(in);

				command = CommandFactory.create(header);
				state = State.Body;
			case Body:
				if(in.readableBytes()<4){
					return;
				}
				bodySize = in.readInt();

				logger.debug("decode : body len {}",bodySize);

				if(in.readableBytes()<bodySize){
					return;
				}

				command.decodeBody(in);
				out.add(command);
				logger.debug("Command decode : {}",command);
				state = State.Header;
		}
	}
}

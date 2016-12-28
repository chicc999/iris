package com.cy.iris.commons.network.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Created by cy on 16/12/28.
 */
public class CommandDecoder extends ByteToMessageDecoder {

	private enum State {  //2
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

				if(in.readableBytes()<bodySize){
					return;
				}

				command.decodeBody(in);
				out.add(command);
				state = State.Header;
		}
	}
}

package pers.cy.iris.coordinator.raft.command;

import io.netty.buffer.ByteBuf;
import pers.cy.iris.commons.network.protocol.Command;
import pers.cy.iris.commons.network.protocol.Header;
import pers.cy.iris.commons.network.protocol.HeaderType;

/**
 * @Author:cy
 * @Date:Created in  2020/5/10
 * @Destription:
 */
public class AppendEntriesAck extends Command {

	private long term;

	private boolean success;

	public AppendEntriesAck(){
		super(new Header(HeaderType.REQUEST,APPEND_ENTRIES_ACK).typeString("APPEND_ENTRIES_ACK"));
	}

	@Override
	protected ByteBuf encodeBody(ByteBuf out) throws Exception {
		out.writeLong(term);
		out.writeBoolean(success);
		return out;
	}

	@Override
	protected void decodeBody(ByteBuf in) throws Exception {
		this.term = in.readLong();
		this.success = in.readBoolean();
	}
}

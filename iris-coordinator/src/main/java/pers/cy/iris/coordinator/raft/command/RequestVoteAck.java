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
public class RequestVoteAck extends Command {

	private long term;

	private boolean voteGranted;

	public RequestVoteAck(){
		super(new Header(HeaderType.REQUEST,REQUEST_VOTE_ACK).typeString("REQUEST_VOTE_ACK"));
	}

	@Override
	protected ByteBuf encodeBody(ByteBuf out) throws Exception {
		out.writeLong(term);
		out.writeBoolean(voteGranted);
		return out;
	}

	@Override
	protected void decodeBody(ByteBuf in) throws Exception {
		this.term = in.readLong();
		this.voteGranted = in.readBoolean();
	}


	public long getTerm() {
		return term;
	}

	public void setTerm(long term) {
		this.term = term;
	}

	public boolean isVoteGranted() {
		return voteGranted;
	}

	public void setVoteGranted(boolean voteGranted) {
		this.voteGranted = voteGranted;
	}
}

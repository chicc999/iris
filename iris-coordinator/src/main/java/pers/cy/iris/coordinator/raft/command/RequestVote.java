package pers.cy.iris.coordinator.raft.command;

import io.netty.buffer.ByteBuf;
import pers.cy.iris.commons.network.protocol.Command;
import pers.cy.iris.commons.network.protocol.Header;
import pers.cy.iris.commons.network.protocol.HeaderType;
import pers.cy.iris.commons.util.Serializer;

/**
 * @Author:cy
 * @Date:Created in  2020/5/9
 * @Destription:
 */
public class RequestVote extends Command{

	private long term;

	private String candidateId;

	private long lastLogIndex;

	private long lastLogTerm;

	public RequestVote(){
		super(new Header(HeaderType.REQUEST,REQUEST_VOTE).typeString("REQUEST_VOTE"));
	}

	@Override
	protected ByteBuf encodeBody(ByteBuf out) throws Exception {
		out.writeLong(term);
		Serializer.writeShortString(candidateId,out);
		out.writeLong(lastLogIndex);
		out.writeLong(lastLogTerm);
		return out;
	}

	@Override
	protected void decodeBody(ByteBuf in) throws Exception {
		this.term = in.readLong();
		this.candidateId = Serializer.readShortString(in);
		this.lastLogIndex = in.readLong();
		this.lastLogTerm = in.readLong();
	}

	public long getTerm() {
		return term;
	}

	public void setTerm(long term) {
		this.term = term;
	}

	public String getCandidateId() {
		return candidateId;
	}

	public void setCandidateId(String candidateId) {
		this.candidateId = candidateId;
	}

	public long getLastLogIndex() {
		return lastLogIndex;
	}

	public void setLastLogIndex(long lastLogIndex) {
		this.lastLogIndex = lastLogIndex;
	}

	public long getLastLogTerm() {
		return lastLogTerm;
	}

	public void setLastLogTerm(long lastLogTerm) {
		this.lastLogTerm = lastLogTerm;
	}
}

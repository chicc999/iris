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
public class AppendEntries extends Command {


	private long leaderTerm;

	private String leaderId;

	private long prevLogIndexs;

	private long[] prevLogTermEntries;

	private long leaderCommit;

	public AppendEntries(){
		super(new Header(HeaderType.REQUEST,APPEND_ENTRIES).typeString("APPEND_ENTRIES"));
	}


	@Override
	protected ByteBuf encodeBody(ByteBuf out) throws Exception {
		return null;
	}

	@Override
	protected void decodeBody(ByteBuf in) throws Exception {

	}

	public long getLeaderTerm() {
		return leaderTerm;
	}

	public void setLeaderTerm(long leaderTerm) {
		this.leaderTerm = leaderTerm;
	}

	public String getLeaderId() {
		return leaderId;
	}

	public void setLeaderId(String leaderId) {
		this.leaderId = leaderId;
	}

	public long getPrevLogIndexs() {
		return prevLogIndexs;
	}

	public void setPrevLogIndexs(long prevLogIndexs) {
		this.prevLogIndexs = prevLogIndexs;
	}

	public long[] getPrevLogTermEntries() {
		return prevLogTermEntries;
	}

	public void setPrevLogTermEntries(long[] prevLogTermEntries) {
		this.prevLogTermEntries = prevLogTermEntries;
	}

	public long getLeaderCommit() {
		return leaderCommit;
	}

	public void setLeaderCommit(long leaderCommit) {
		this.leaderCommit = leaderCommit;
	}
}

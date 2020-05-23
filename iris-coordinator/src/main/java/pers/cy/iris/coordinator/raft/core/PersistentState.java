package pers.cy.iris.coordinator.raft.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.cy.iris.commons.store.AbstractCheckPoint;
import pers.cy.iris.commons.util.JsonUtil;

import java.io.File;
import java.io.IOException;

/**
 * @Author:cy
 * @Date:Created in  2020/5/23
 * @Destription: 需要在所有机器上持久化的状态信息
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PersistentState extends AbstractCheckPoint {

	private static Logger logger = LoggerFactory.getLogger(PersistentState.class);

	/* ============ 在所有服务器上需要持久化的状态信息 ============ */
	// 当前任期
	private volatile long currentTerm = 0;
	// 候选人在当前任期的投票
	private volatile String votedFor = "";

	public PersistentState() {
	}

	public PersistentState(File file) {
		super(file);
	}

	public PersistentState(String path) {
		super(new File(path, "persistent_state"));
	}

	@Override
	protected int getCheckSum() {
		return hashCode();
	}

	@Override
	protected void doRecover(byte[] data) {
		try {
			PersistentState state = JsonUtil.readValue(new String(data),PersistentState.class);
			this.currentTerm = state.getCurrentTerm();
			this.votedFor = state.getVotedFor();
		} catch (IOException e) {
			logger.error("recover persistent state error.",e);
			throw new RuntimeException(e);
		}
	}

	@Override
	protected byte[] encode() {
		try {
			return JsonUtil.writeValue(this);
		} catch (IOException e) {
			logger.error("persistent state error.",e);
			throw new RuntimeException(e);
		}
	}

	public long getCurrentTerm() {
		return currentTerm;
	}

	public void setCurrentTerm(long currentTerm) {
		this.currentTerm = currentTerm;
	}

	public String getVotedFor() {
		return votedFor;
	}

	public void setVotedFor(String votedFor) {
		this.votedFor = votedFor;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof PersistentState)) return false;

		PersistentState that = (PersistentState) o;

		if (getCurrentTerm() != that.getCurrentTerm()) return false;
		return getVotedFor().equals(that.getVotedFor());
	}

	@Override
	public int hashCode() {
		int result = (int) (getCurrentTerm() ^ (getCurrentTerm() >>> 32));
		result = 31 * result + getVotedFor().hashCode();
		return result;
	}
}

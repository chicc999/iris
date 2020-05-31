package pers.cy.iris.coordinator.raft.core;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.cy.iris.commons.service.Service;
import pers.cy.iris.commons.util.ArgumentUtil;
import pers.cy.iris.commons.util.bootstrap.ServerType;
import pers.cy.iris.coordinator.CoordinatorConfig;
import pers.cy.iris.coordinator.raft.command.AppendEntriesAck;
import pers.cy.iris.coordinator.raft.command.RequestVote;
import pers.cy.iris.coordinator.raft.command.RequestVoteAck;

import java.io.File;
import java.util.Map;

/**
 * @Author:cy
 * @Date:Created in  2020/5/10
 * @Destription:
 */
public class ClusterNode extends Service {

	private static final Logger logger = LoggerFactory.getLogger(ClusterNode.class);

	/* ============ 在所有服务器上需要持久化的状态信息 ============ */

	private PersistentState persistentState;
	// 日志管理
	private LogManager logManager;

	/* ============ 在所有服务器上都有的内存状态 ============ */
	// 最高已提交的日志的序号
	private volatile long commitIndex;
	// 最新已经被提交到状态机的日志序号
	private volatile long lastApplied;

	/* ============ 主节点内存维护的状态 ============ */

	private Map<String/** 节点名称 **/, Long /** 下一个待发送的日志序号 **/> nextIndex[];
	private Map<String/** 节点名称 **/, Long /** 日志被复制成功的最高序号 **/> matchIndex[];


	private volatile ClusterRole role = ClusterRole.FOLLOWER;

	private CoordinatorConfig config;

	private String candidateId;


	@Override
	public void beforeStart() throws Exception {
		// 设定节点名称
		if(StringUtils.isEmpty(candidateId)) {
			candidateId = System.getProperty(ServerType.Coordinator.nameKey());
		}

		persistentState = new PersistentState(config.getPersistentPath() + File.separator + candidateId);


	}

	@Override
	public void doStart() throws Exception {

	}

	@Override
	public void afterStart() throws Exception {

	}

	@Override
	public void beforeStop() {

	}

	@Override
	public void doStop() throws Exception {

	}


	public RequestVoteAck requestVote(RequestVote requestVote){
		RequestVoteAck requestVoteAck = new RequestVoteAck();

		final long term = requestVote.getTerm();

		final long currentTerm = persistentState.getCurrentTerm();

		if(term >= currentTerm
				&& canVoted(requestVote.getCandidateId())
				&& isUpToDate(requestVote.getLastLogTerm(),requestVote.getLastLogIndex())){

			persistentState.setVotedFor(requestVote.getCandidateId());
			persistentState.setCurrentTerm(term);
			// 发送投票响应前持久化投票状态和轮次，避免宕机重启后此轮重复投票
			// update on stable storage before responding to RPCs
			persistentState.flush();

			requestVoteAck.setTerm(persistentState.getCurrentTerm());
			requestVoteAck.setVoteGranted(true);

		}else{
			// chapter 3.3 page 15
			// If a server receives a request with a stale term number,
			// it rejects the request.
			requestVoteAck.setTerm(currentTerm);
			requestVoteAck.setVoteGranted(false);
		}

		return requestVoteAck;

	}

	/**
	 * 此轮还有资格投票
	 *
	 * @param candidateId 请求投票的服务
	 * @return
	 */
	private boolean canVoted(String candidateId){
		// 从来没有投过票或者本轮投的就是这个节点
		return persistentState.getVotedFor()==null
				|| persistentState.getVotedFor().equals(candidateId);
	}

	/**
	 * 日志是否足够新
	 *
	 * @return true 目标日志不落后于自身
	 */
	private boolean isUpToDate(long term,long index){

		LogEntry lastEntry = logManager.getLast();

		if(lastEntry == null){
			return true;
		}

		// chapter 3.6.1 page 22
		// Raft determines which of two logs is more up-to-date by comparing the index
		// and term of last entries in the logs
		return lastEntry.getTerm() < term
				|| (lastEntry.getTerm()==term
					&& lastEntry.getIndex() <= index);

	}


	public AppendEntriesAck appendEntry(){
		return null;
	}


}

package pers.cy.iris.coordinator.raft.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.cy.iris.commons.service.Service;
import pers.cy.iris.coordinator.CoordinatorConfig;

import java.io.File;
import java.util.Map;

/**
 * @Author:cy
 * @Date:Created in  2020/5/10
 * @Destription:
 */
public class ClusterNode extends Service{

	private static final Logger LOGGER = LoggerFactory.getLogger(ClusterNode.class);

	/* ============ 在所有服务器上需要持久化的状态信息 ============ */
	// 当前任期
	private volatile long currentTerm = 0;
	// 候选人在当前任期的投票
	private volatile String votedFor = null;
	// 日志管理
	private LogManager logManager;

	/* ============ 在所有服务器上都有的内存状态 ============ */
    // 最高已提交的日志的序号
	private volatile long commitIndex;
	// 最新已经被提交到状态机的日志序号
	private volatile long lastApplied;

	/* ============ 主节点内存维护的状态 ============ */

	private Map<String/** 节点名称 **/,Long /** 下一个待发送的日志序号 **/> nextIndex[];
	private Map<String/** 节点名称 **/,Long /** 日志被复制成功的最高序号 **/> matchIndex[];


	private volatile ClusterRole role = ClusterRole.FOLLOWER;

	private File stateFile;

	private CoordinatorConfig config;

	private String candidateId;


	@Override
	public void beforeStart() throws Exception {

		// 创建持久化节点状态文件
		File path = new File(config.getPersistentPath().trim());
		if (path.exists() && !path.isDirectory()) {
			throw new IllegalStateException("config.persistentPath is not a directory. " + path.getPath());
		}
		if (!path.exists()) {
			if (!path.mkdirs()) {
				if (!path.exists()) {
					throw new IllegalStateException("create directory error. " + path.getPath());
				}
			}
		}
		if (!path.canRead() || !path.canWrite()) {
			throw new IllegalStateException("no permission to access directory. " + path.getPath());
		}
		stateFile = new File(path, "cluster_state_" + candidateId +".properties");

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
}

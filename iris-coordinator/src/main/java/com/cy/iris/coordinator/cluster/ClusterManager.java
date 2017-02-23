package com.cy.iris.coordinator.cluster;

import com.cy.iris.commons.service.Service;
import com.cy.iris.commons.util.ArgumentUtil;
import com.cy.iris.coordinator.CoordinatorConfig;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static com.cy.iris.coordinator.startup.Bootstrap.COORDINATOR_NAME;

/**
 * Created by cy on 17/2/16.
 */
public class ClusterManager extends Service{

	private static final Logger logger = LoggerFactory.getLogger(ClusterManager.class);

	private static final String COORDINATOR_PATH = "/coordinator/live/";

	private static final String TOPIC_PATH = "topic";

	private CuratorFramework zkClient ;

	private CoordinatorConfig coordinatorConfig;

	// 集群配置信息
	private Map<String, TopicConfig> topics = new HashMap<String, TopicConfig>();


	@Override
	public void beforeStart() throws Exception {
		ArgumentUtil.isNotNull("zkClient",zkClient);
		ArgumentUtil.isNotNull("coordinatorConfig",coordinatorConfig);


	}

	@Override
	public void doStart() throws Exception {
		zkClient.start();
		zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
				.forPath(COORDINATOR_PATH + System.getProperty(COORDINATOR_NAME) + "_", System.getProperty(COORDINATOR_NAME).getBytes("utf-8"));


	}

	@Override
	public void afterStart() throws Exception {

	}

	@Override
	public void beforeStop() {

	}

	@Override
	public void doStop() {

	}

	public void setZkClient(CuratorFramework zkClient) {
		this.zkClient = zkClient;
	}

	public void setCoordinatorConfig(CoordinatorConfig coordinatorConfig) {
		this.coordinatorConfig = coordinatorConfig;
	}

	private void updateTopic(){

	}


	private boolean addTopic(String topic){
		if(topics.containsKey(topic)){

		}
		return false;
	}

	private boolean getCluster(String topic){
		return false;
	}
}

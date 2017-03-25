package com.cy.iris.broker.MetaManager;

import com.cy.iris.commons.model.TopicConfig;
import com.cy.iris.commons.service.Service;
import com.cy.iris.commons.util.bootstrap.ServerType;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author:cy
 * @Date:Created in  17/3/23
 * @Destription:
 */
public class MetaManager extends Service{

	private static final Logger logger = LoggerFactory.getLogger(MetaManager.class);

	private static final String BROKER_PATH = "/broker/live/";

	private static final String TOPIC_PATH = "/topic";

	private CuratorFramework zkClient ;

	private MetaConfig metaConfig;

	// 主题配置信息
	private Map<String, TopicConfig> topics = new HashMap<String, TopicConfig>();

	@Override
	public void beforeStart() throws Exception {
		if(zkClient == null){
			ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(
					metaConfig.getZookeeperBaseSleepTimeMs(), metaConfig.getZookeeperMaxRetries());
			zkClient = CuratorFrameworkFactory.builder()
					.connectString(metaConfig.getConnectionString())
					.retryPolicy(retryPolicy)
					.connectionTimeoutMs(metaConfig.getConnectionTimeout())
					.sessionTimeoutMs(metaConfig.getSessionTimeout())
					.namespace(metaConfig.getNameSpace())
					.build();
		}
	}

	@Override
	public void doStart() throws Exception {
		zkClient.start();

		//注册broker存活
		zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
				.forPath(BROKER_PATH + System.getProperty(ServerType.Broker.nameKey()) + "_", System.getProperty(ServerType.Broker.nameKey()).getBytes("utf-8"));

		//检测并初始化topic路径
		if(null == this.zkClient.checkExists().forPath(this.TOPIC_PATH)) {
			this.zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(this.TOPIC_PATH,"[]".getBytes("utf-8"));
		}


	}

	@Override
	public void afterStart() throws Exception {

	}

	@Override
	public void beforeStop() {

	}

	@Override
	public void doStop() {
		zkClient.close();
	}

	public void setMetaConfig(MetaConfig metaConfig) {
		this.metaConfig = metaConfig;
	}
}

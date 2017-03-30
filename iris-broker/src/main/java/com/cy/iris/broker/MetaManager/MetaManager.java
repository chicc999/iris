package com.cy.iris.broker.MetaManager;

import com.cy.iris.commons.cluster.ClusterEvent;
import com.cy.iris.commons.cluster.event.TopicUpdateEvent;
import com.cy.iris.commons.model.TopicConfig;
import com.cy.iris.commons.service.Service;
import com.cy.iris.commons.util.ArgumentUtil;
import com.cy.iris.commons.util.JsonUtil;
import com.cy.iris.commons.util.bootstrap.ServerType;
import com.cy.iris.commons.util.eventmanager.EventManager;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author:cy
 * @Date:Created in  17/3/23
 * @Destription: 集群信息管理
 */
public class MetaManager extends Service{

	private static final Logger logger = LoggerFactory.getLogger(MetaManager.class);

	private static final String BROKER_LIVE_PATH = "/broker/live/";

	private static final String TOPIC_PATH = "/topic";

	private static final String BROKER_PATH = "/broker";

	private EventManager<ClusterEvent> clusterEventManager = new EventManager<ClusterEvent>("ClusterEventManager");

	private CuratorFramework zkClient ;

	private MetaConfig metaConfig;

	// 主题配置信息
	private volatile Map<String, TopicConfig> topics = new HashMap<String, TopicConfig>();

	private NodeCache topicCache;

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
				.forPath(BROKER_LIVE_PATH + System.getProperty(ServerType.Broker.nameKey()) + "_", System.getProperty(ServerType.Broker.nameKey()).getBytes("utf-8"));

		//检测并初始化topic路径
		if(null == this.zkClient.checkExists().forPath(this.TOPIC_PATH)) {
			this.zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(this.TOPIC_PATH,"[]".getBytes("utf-8"));
		}

		//注册监听器
		if(topicCache == null) {
			topicCache = new NodeCache(this.zkClient, this.TOPIC_PATH, false);
			topicCache.getListenable().addListener(new NodeCacheListener() {
				@Override
				public void nodeChanged() throws Exception {
					updateTopic(topicCache.getCurrentData().getData());
				}
			});
		}
		//启动监听器
		topicCache.start();

		clusterEventManager.start();

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
		try {
			topicCache.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		clusterEventManager.stop();
	}

	public void setMetaConfig(MetaConfig metaConfig) {
		this.metaConfig = metaConfig;
	}


	private void updateTopic(byte[] content) throws IOException {
		if(content == null || content.length == 0){
			return;
		}
		Map<String,TopicConfig> current = new HashMap<String, TopicConfig>();

		List<TopicConfig> topicConfigs = JsonUtil.readListValue(new String(content),TopicConfig.class);

		for(TopicConfig topicConfig:topicConfigs){
			if(topicConfig !=null && topicConfig.getTopic() != null && !StringUtils.isBlank(topicConfig.getTopic())){
				current.put(topicConfig.getTopic(),topicConfig);
			}
		}
		topics = current;
		//添加topic更新事件
		clusterEventManager.add(new TopicUpdateEvent());
	}

}

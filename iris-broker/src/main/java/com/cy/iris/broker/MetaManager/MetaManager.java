package pers.cy.iris.broker.MetaManager;

import pers.cy.iris.commons.cluster.Broker;
import pers.cy.iris.commons.cluster.BrokerCluster;
import pers.cy.iris.commons.cluster.BrokerGroup;
import pers.cy.iris.commons.cluster.ClusterEvent;
import pers.cy.iris.commons.cluster.event.AllBrokerUpdateEvent;
import pers.cy.iris.commons.cluster.event.TopicUpdateEvent;
import pers.cy.iris.commons.cluster.event.UpdateExceptionEvent;
import pers.cy.iris.commons.model.TopicConfig;
import pers.cy.iris.commons.service.Service;
import pers.cy.iris.commons.util.ArgumentUtil;
import pers.cy.iris.commons.util.JsonUtil;
import pers.cy.iris.commons.util.bootstrap.ServerType;
import pers.cy.iris.commons.util.eventmanager.EventManager;
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
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Author:cy
 * @Date:Created in  17/3/23
 * @Destription: 集群信息管理
 */
public class MetaManager extends Service{

	private static final Logger logger = LoggerFactory.getLogger(MetaManager.class);

	private static final String BROKER_LIVE_PATH = "/broker/live/";

	private static final String TOPIC_PATH = "/topic_zip";

	private static final String BROKER_PATH = "/broker_zip";

	private EventManager<ClusterEvent> clusterEventManager = new EventManager<ClusterEvent>("ClusterEventManager");

	private CuratorFramework zkClient ;

	private MetaConfig metaConfig;

	// 当前Broker
	private Broker broker;
	// 当前分组
	private BrokerGroup brokerGroup;

	// 主题配置信息
	private volatile Map<String, TopicConfig> topics ;

	// 集群
	private ConcurrentMap<String, BrokerCluster> clusters ;

	// Group
	private Map<String, BrokerGroup> groups = new HashMap<String, BrokerGroup>();
	// Broker
	private Map<String, Broker> brokers = new HashMap<String, Broker>();

	private NodeCache topicCache;

	private NodeCache brokerCache;

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

		//初始化本地broker
		if(this.broker == null){
			this.broker = new Broker(System.getProperty(ServerType.Broker.nameKey()));
		}

		//初始化本地所在brokerGroup
		if(this.brokerGroup == null){
			this.brokerGroup = new BrokerGroup();
		}
		this.brokerGroup.addBroker(this.broker);



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
				public void nodeChanged()  {
					try {
						updateTopic(topicCache.getCurrentData().getData());
					} catch (IOException e) {
						clusterEventManager.add(new UpdateExceptionEvent(e));
					}
				}
			});
		}
		//启动监听器
		topicCache.start();


		//检测并初始化broker路径
		if(null == this.zkClient.checkExists().forPath(this.BROKER_PATH)) {
			this.zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(this.BROKER_PATH,"[]".getBytes("utf-8"));
		}
		//注册监听器
		if(brokerCache == null) {
			brokerCache = new NodeCache(this.zkClient, this.BROKER_PATH, false);
			brokerCache.getListenable().addListener(new NodeCacheListener() {
				@Override
				public void nodeChanged() {
					try {
						updateBroker(brokerCache.getCurrentData().getData());
					} catch (IOException e) {
						clusterEventManager.add(new UpdateExceptionEvent(e));
					}
				}
			});
		}
		//启动监听器
		brokerCache.start();

		clusterEventManager.start();

	}

	@Override
	public void afterStart() throws Exception {
		logger.info("MetaManager started successfully.");
	}

	@Override
	public void beforeStop() {

	}

	@Override
	public void doStop() {
		zkClient.close();
		try {
			topicCache.close();
			brokerCache.close();
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


	private void updateBroker(byte[] content) throws IOException {
		if(content == null || content.length == 0){
			return;
		}
		List<Broker> brokers = JsonUtil.readListValue(new String(content),Broker.class);
		updateCluster(brokers);
	}

	private void updateCluster(List<Broker> brokers){
		Map<String,BrokerCluster> current = new HashMap<String, BrokerCluster>();
		Broker source = this.broker;
		Map<String, Broker> currentBrokers = new HashMap<String, Broker>(brokers.size());
		Map<String, BrokerGroup> currentGroups = new HashMap<String, BrokerGroup>(brokers.size());
		BrokerGroup group;
		boolean isMatch = false;
		// 遍历Broker
		for (Broker broker : brokers) {
			// 存放当前Broker
			currentBrokers.put(broker.getName(), broker);
			// 获取分组
			group = currentGroups.get(broker.getGroup());
			if (group == null) {
				// 分组不存在，则创建
				group = new BrokerGroup();
				group.setGroup(broker.getGroup());
				currentGroups.put(broker.getGroup(), group);
			}
			// 添加到分组中
			group.addBroker(broker);

			if (broker.equals(source)) {
				// 等于当前Broker
				source.setPermission(broker.getPermission());
				source.setGroup(broker.getGroup());
				source.setAlias(broker.getAlias());
				source.setDataCenter(broker.getDataCenter());
				source.setSyncMode(broker.getSyncMode());
				source.setRetryType(broker.getRetryType());
				//broker的角色会随着选举发生变化
				//source.setRole(broker.getRole());
				if (broker.getReplicationPort() > 0) {
					source.setReplicationPort(broker.getReplicationPort());
				}
				this.brokerGroup = group;
				isMatch = true;
			}
		}
		if (!isMatch) {
			logger.error(String.format("broker config maybe error,can not find %s", this.broker));
		}

		this.brokers = currentBrokers;
		this.groups = currentGroups;

		this.clusterEventManager.add(new AllBrokerUpdateEvent());

	}
}

package pers.cy.iris.coordinator.cluster;

import pers.cy.iris.commons.model.TopicConfig;
import pers.cy.iris.commons.service.Service;
import pers.cy.iris.commons.util.ArgumentUtil;
import pers.cy.iris.commons.util.JsonUtil;
import pers.cy.iris.commons.util.lock.ZookeeperReadWriteLocks;
import pers.cy.iris.commons.util.bootstrap.ServerType;
import pers.cy.iris.coordinator.CoordinatorConfig;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * 集群信息管理类.
 * /iris/coordinator/live 注册存活节点
 * /iris/lock/topic 抢到此锁有权限更新/iris/topic
 * /iris/topic 保存主题的配置信息
 */
public class ClusterManager extends Service{

	private static final Logger logger = LoggerFactory.getLogger(ClusterManager.class);

	private static final String COORDINATOR_PATH = "/coordinator/live/";

	private static final String TOPIC_PATH = "/topic";

	private CuratorFramework zkClient ;

	private CoordinatorConfig coordinatorConfig;

	// 集群配置信息
	private Map<String, TopicConfig> topics = new HashMap<String, TopicConfig>();

	private ZookeeperReadWriteLocks topicConfigLock;

	@Override
	public void beforeStart() throws Exception {
		ArgumentUtil.isNotNull("zkClient",zkClient);
		ArgumentUtil.isNotNull("coordinatorConfig",coordinatorConfig);

		topicConfigLock = new ZookeeperReadWriteLocks(zkClient,TOPIC_PATH);

	}

	@Override
	public void doStart() throws Exception {
		zkClient.start();
		zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
				.forPath(COORDINATOR_PATH + System.getProperty(ServerType.Coordinator.nameKey()) + "_", System.getProperty(ServerType.Coordinator.nameKey()).getBytes("utf-8"));

		if(null == this.zkClient.checkExists().forPath(this.TOPIC_PATH)) {
			this.zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(this.TOPIC_PATH,"[]".getBytes("utf-8"));
		}

		topicConfigLock.start();

		//阻塞的从zk获取数据
		topics = JsonUtil.readMapValue(new String(topicConfigLock.get(TOPIC_PATH)),String.class,TopicConfig.class);
		topics.put("1",new TopicConfig("11",(short)5,false));
		topics.put("2",new TopicConfig("22",(short)5,true));

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




	private boolean addTopic(String topic){
		if(topics.containsKey(topic)){
				return true;
		}else{
			TopicConfig topicConfig = new TopicConfig();
			topics.put(topic,topicConfig);
		}
		return false;
	}

	private boolean getCluster(String topic){
		return false;
	}
}

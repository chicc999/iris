package pers.cy.iris.client.cli.zookeeper;

import pers.cy.iris.commons.service.Service;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * @Author:cy
 * @Date:Created in 10:39 17/3/16
 * @Destription: 提供zk相关的服务
 */
public class ZookeeperService extends Service{

	private static final String NAME_SPACE = "iris";
	private static final String TOPIC_PATH = "topic_zip";
	private static final String BROKER_PATH = "broker_zip";


	protected CuratorFramework zk;

	private final String connectionString;
	private final int connectionTimeout = 15 * 1000;
	private final int sessionTimeout = 60 * 1000;

	public ZookeeperService(String connectionString) {
		this.connectionString = connectionString;
	}

	@Override
	public void beforeStart() throws Exception {
		ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);
		zk = CuratorFrameworkFactory.builder()
				.connectString(connectionString)
				.retryPolicy(retryPolicy)
				.connectionTimeoutMs(connectionTimeout)
				.sessionTimeoutMs(sessionTimeout)
				.namespace(NAME_SPACE)
				.build();
	}

	@Override
	public void doStart() throws Exception {
		zk.start();
		checkAndCreate(TOPIC_PATH);
		checkAndCreate(BROKER_PATH);

	}

	@Override
	public void afterStart() throws Exception {

	}

	@Override
	public void beforeStop() {

	}

	@Override
	public void doStop() {
		zk.close();
	}

	private void checkAndCreate(String path) throws Exception {
		if(null == this.zk.checkExists().forPath(path)) {
			this.zk.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path,"".getBytes("utf-8"));
		}
	}

}

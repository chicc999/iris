package com.cy.iris.broker.MetaManager;

import com.cy.iris.commons.service.Service;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author:cy
 * @Date:Created in  17/3/23
 * @Destription:
 */
public class MetaManager extends Service{

	private static final Logger logger = LoggerFactory.getLogger(MetaManager.class);

	private CuratorFramework zkClient ;

	private MetaConfig metaConfig;

	@Override
	public void beforeStart() throws Exception {
		if(zkClient == null){
			ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(
					metaConfig.getZookeeperBaseSleepTimeMs(), metaConfig.getZookeeperMaxRetries());
			CuratorFramework zkClient = CuratorFrameworkFactory.builder()
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

package com.cy.iris.coordinator;

import com.cy.iris.commons.network.netty.server.NettyServer;
import com.cy.iris.commons.service.Service;
import com.cy.iris.commons.util.ArgumentUtil;
import com.cy.iris.coordinator.cluster.ClusterManager;
import com.cy.iris.coordinator.handler.CoordinatorHandlerFactory;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by cy on 17/2/14.
 */
public class CoordinatorService extends Service{

	private static final Logger logger = LoggerFactory.getLogger(CoordinatorService.class);

	private CoordinatorConfig coordinatorConfig;

	// Netty服务
	protected NettyServer nettyServer;

	private ClusterManager clusterManager;

	public CoordinatorService() {
	}


	@Override
	public void beforeStart() throws Exception {
		ArgumentUtil.isNotNull("CoordinatorConfig", coordinatorConfig);
		ArgumentUtil.isNotNull("NettyServerConfig", coordinatorConfig.getNettyServerConfig());

		//设置协调器名字
		String ip = null;
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
			logger.info("本机ip为 {}", ip);
		} catch (UnknownHostException e) {
			logger.error("获取本机ip失败", e);
			ip = "unknown";
		}
		coordinatorConfig.setCoordinatorName(ip+":"+coordinatorConfig.getNettyServerConfig().getPort());

		CoordinatorHandlerFactory coordinatorHandlerFactory = new CoordinatorHandlerFactory();
		if(nettyServer == null) {
			nettyServer = new NettyServer(coordinatorConfig.getNettyServerConfig(),null,null,null,coordinatorHandlerFactory);
		}

		if(clusterManager == null) {
			ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);
			CuratorFramework zkClient = CuratorFrameworkFactory.builder()
					.connectString(coordinatorConfig.getConnectionString())
					.retryPolicy(retryPolicy)
					.connectionTimeoutMs(coordinatorConfig.getConnectionTimeout())
					.sessionTimeoutMs(coordinatorConfig.getSessionTimeout())
					.namespace(coordinatorConfig.getNameSpace())
					.build();

			clusterManager = new ClusterManager();
			clusterManager.setZkClient(zkClient);
			clusterManager.setCoordinatorConfig(coordinatorConfig);
		}
	}

	@Override
	public void doStart() throws Exception {
			nettyServer.start();
			clusterManager.start();
	}

	@Override
	public void afterStart() throws Exception {

	}

	@Override
	public void beforeStop() {

	}

	@Override
	public void doStop() {
		nettyServer.stop();
	}


	public void setCoordinatorConfig(CoordinatorConfig coordinatorConfig) {
		this.coordinatorConfig = coordinatorConfig;
	}
}

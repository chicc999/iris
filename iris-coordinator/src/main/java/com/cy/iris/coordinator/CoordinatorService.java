package com.cy.iris.coordinator;

import com.cy.iris.commons.network.netty.server.NettyServer;
import com.cy.iris.commons.service.Service;
import com.cy.iris.commons.util.ArgumentUtil;
import com.cy.iris.coordinator.handler.CoordinatorHandlerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by cy on 17/2/14.
 */
public class CoordinatorService extends Service{

	private CoordinatorConfig coordinatorConfig;

	// Netty服务
	protected NettyServer nettyServer;

	public CoordinatorService() {
	}


	@Override
	public void beforeStart() throws Exception {
		ArgumentUtil.isNotNull("CoordinatorConfig", coordinatorConfig);
		ArgumentUtil.isNotNull("NettyServerConfig", coordinatorConfig.getNettyServerConfig());

		CoordinatorHandlerFactory coordinatorHandlerFactory = new CoordinatorHandlerFactory();
		if(nettyServer == null) {
			nettyServer = new NettyServer(coordinatorConfig.getNettyServerConfig(),null,null,null,coordinatorHandlerFactory);
		}
	}

	@Override
	public void doStart() throws Exception {
			nettyServer.start();
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

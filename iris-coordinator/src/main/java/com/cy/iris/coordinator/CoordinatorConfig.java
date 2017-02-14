package com.cy.iris.coordinator;

import com.cy.iris.commons.network.netty.server.NettyServerConfig;

/**
 * Created by cy on 17/2/14.
 */
public class CoordinatorConfig {

	private NettyServerConfig nettyServerConfig;

	public void setNettyServerConfig(NettyServerConfig nettyServerConfig) {
		this.nettyServerConfig = nettyServerConfig;
	}

	public NettyServerConfig getNettyServerConfig() {
		return nettyServerConfig;
	}
}

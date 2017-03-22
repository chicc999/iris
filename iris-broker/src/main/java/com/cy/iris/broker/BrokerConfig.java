package com.cy.iris.broker;

import com.cy.iris.commons.network.netty.server.NettyServerConfig;

/**
 * @Author:cy
 * @Date:Created in 19:27 17/3/22
 * @Destription:
 */
public class BrokerConfig {
	private NettyServerConfig nettyServerConfig;

	public NettyServerConfig getNettyServerConfig() {
		return nettyServerConfig;
	}

	public void setNettyServerConfig(NettyServerConfig nettyServerConfig) {
		this.nettyServerConfig = nettyServerConfig;
	}
}

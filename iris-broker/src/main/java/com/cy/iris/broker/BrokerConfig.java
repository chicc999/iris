package com.cy.iris.broker;

import com.cy.iris.broker.MetaManager.MetaConfig;
import com.cy.iris.commons.network.netty.server.NettyServerConfig;

/**
 * @Author:cy
 * @Date:Created in 19:27 17/3/22
 * @Destription:
 */
public class BrokerConfig {
	private NettyServerConfig nettyServerConfig;

	private MetaConfig metaConfig;



	public void setNettyServerConfig(NettyServerConfig nettyServerConfig) {
		this.nettyServerConfig = nettyServerConfig;
	}

	public NettyServerConfig getNettyServerConfig() {
		return nettyServerConfig;
	}

	public MetaConfig getMetaConfig() {
		return metaConfig;
	}

	public void setMetaConfig(MetaConfig metaConfig) {
		this.metaConfig = metaConfig;
	}
}

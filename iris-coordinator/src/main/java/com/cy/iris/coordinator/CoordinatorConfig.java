package com.cy.iris.coordinator;

import com.cy.iris.commons.network.netty.server.NettyServerConfig;

/**
 * Created by cy on 17/2/14.
 */
public class CoordinatorConfig {

	private NettyServerConfig nettyServerConfig;

	private String connectionString ;

	private int connectionTimeout = 15 * 1000;

	private int sessionTimeout = 60 * 1000;

	private String nameSpace = "iris";

	public void setNettyServerConfig(NettyServerConfig nettyServerConfig) {
		this.nettyServerConfig = nettyServerConfig;
	}

	public NettyServerConfig getNettyServerConfig() {
		return nettyServerConfig;
	}

	public String getConnectionString() {
		return connectionString;
	}

	public void setConnectionString(String connectionString) {
		this.connectionString = connectionString;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public int getSessionTimeout() {
		return sessionTimeout;
	}

	public void setSessionTimeout(int sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}

	public String getNameSpace() {
		return nameSpace;
	}

	public void setNameSpace(String nameSpace) {
		this.nameSpace = nameSpace;
	}
}

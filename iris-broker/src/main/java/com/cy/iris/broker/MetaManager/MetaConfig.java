package com.cy.iris.broker.MetaManager;

/**
 * @Author:cy
 * @Date:Created in  17/3/23
 * @Destription:
 */
public class MetaConfig {
	private String connectionString;

	private int connectionTimeout = 15 * 1000;

	private int sessionTimeout = 60 * 1000;

	private String nameSpace = "iris";

	private int zookeeperBaseSleepTimeMs = 1000;

	private int zookeeperMaxRetries = 3;

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

	public int getZookeeperBaseSleepTimeMs() {
		return zookeeperBaseSleepTimeMs;
	}

	public void setZookeeperBaseSleepTimeMs(int zookeeperBaseSleepTimeMs) {
		this.zookeeperBaseSleepTimeMs = zookeeperBaseSleepTimeMs;
	}

	public int getZookeeperMaxRetries() {
		return zookeeperMaxRetries;
	}

	public void setZookeeperMaxRetries(int zookeeperMaxRetries) {
		this.zookeeperMaxRetries = zookeeperMaxRetries;
	}
}

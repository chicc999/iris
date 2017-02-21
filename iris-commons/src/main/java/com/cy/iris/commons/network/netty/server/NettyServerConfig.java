package com.cy.iris.commons.network.netty.server;

import com.cy.iris.commons.network.netty.NettyConfig;

/**
 * 服务端网络配置
 */
public class NettyServerConfig extends NettyConfig {

	public static final int DEFAULT_SERVICE_PORT = 60088;
	// IP地址
	private String ip;
	// 端口
	private int port = DEFAULT_SERVICE_PORT;
	// 连接请求最大队列长度，如果队列满时收到连接指示，则拒绝该连接。
	private int backlog = 65536;

	public NettyServerConfig() {
		super();
		// 目前线上机器默认24核
		workerThreads = 24;
		selectorThreads = 8;
		maxOneway = 24;
		maxAsync = 48;
		channelMaxIdleTime = 120 * 1000;
		setEpoll(true);
	}

	public NettyServerConfig(NettyServerConfig config, int port) {
		super(config);
		setIp(config.getIp());
		setPort(port);
		setBacklog(config.getBacklog());
	}

	public NettyServerConfig workerThreads(final int workerThreads) {
		setWorkerThreads(workerThreads);
		return this;
	}

	public NettyServerConfig callbackExecutorThreads(final int callbackExecutorThreads) {
		setServiceExecutorThreads(callbackExecutorThreads);
		return this;
	}

	public NettyServerConfig selectorThreads(final int selectorThreads) {
		setSelectorThreads(selectorThreads);
		return this;
	}

	public NettyServerConfig channelMaxIdleTime(final int channelMaxIdleTime) {
		setChannelMaxIdleTime(channelMaxIdleTime);
		return this;
	}

	public NettyServerConfig reuseAddress(final boolean reuseAddress) {
		setReuseAddress(reuseAddress);
		return this;
	}

	public NettyServerConfig soLinger(final int soLinger) {
		setSoLinger(soLinger);
		return this;
	}

	public NettyServerConfig tcpNoDelay(final boolean tcpNoDelay) {
		setTcpNoDelay(tcpNoDelay);
		return this;
	}

	public NettyServerConfig keepAlive(final boolean keepAlive) {
		setKeepAlive(keepAlive);
		return this;
	}

	public NettyServerConfig soTimeout(final int soTimeout) {
		setSoTimeout(soTimeout);
		return this;
	}

	public NettyServerConfig socketBufferSize(final int socketBufferSize) {
		setSocketBufferSize(socketBufferSize);
		return this;
	}

	public NettyServerConfig epoll(final boolean epoll) {
		setEpoll(epoll);
		return this;
	}

	public NettyServerConfig maxOneway(final int maxOneway) {
		setMaxOneway(maxOneway);
		return this;
	}

	public NettyServerConfig maxAsync(final int maxAsync) {
		setMaxAsync(maxAsync);
		return this;
	}

	public NettyServerConfig frameMaxSize(final int frameMaxSize) {
		setFrameMaxSize(frameMaxSize);
		return this;
	}

	public NettyServerConfig backlog(final int backlog) {
		setBacklog(backlog);
		return this;
	}

	public NettyServerConfig ip(final String ip) {
		setIp(ip);
		return this;
	}

	public NettyServerConfig port(final int port) {
		setPort(port);
		return this;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return this.port;
	}

	public void setPort(int port) {
		if (port > 0 && port <= 65535) {
			this.port = port;
		}
	}

	public int getBacklog() {
		return this.backlog;
	}

	public void setBacklog(int backlog) {
		if (backlog > 0) {
			this.backlog = backlog;
		}
	}

}

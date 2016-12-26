package com.cy.iris.commons.network.netty.client;

import com.cy.iris.commons.exception.ConnectException;
import com.cy.iris.commons.network.handler.CommandHandlerFactory;
import com.cy.iris.commons.network.netty.NettyTransport;
import com.cy.iris.commons.util.ArgumentUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.EventExecutorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InterruptedIOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;

/**
 * Created by cy on 16/12/23.
 */
public class NettyClient extends NettyTransport {

	private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);

	// Netty客户端启动器
	protected Bootstrap bootstrap;

	/**
	 * 构造函数
	 *
	 * @param config 配置
	 */
	public NettyClient(NettyClientConfig config) {
		super(config, null, null, null);
	}

	/**
	 * 构造函数
	 *
	 * @param config      配置
	 * @param ioLoopGroup IO处理的线程池
	 * @param workerGroup 命令处理线程池
	 */
	public NettyClient(NettyClientConfig config, EventLoopGroup ioLoopGroup, EventExecutorGroup workerGroup) {
		super(config, ioLoopGroup, workerGroup, null);
	}

	/**
	 * 构造函数
	 *
	 * @param config      配置
	 * @param ioLoopGroup IO处理的线程池
	 * @param workerGroup 命令处理线程池
	 * @param factory     命令工厂类
	 */
	public NettyClient(NettyClientConfig config, EventLoopGroup ioLoopGroup, EventExecutorGroup workerGroup,
					   CommandHandlerFactory factory) {
		super(config, ioLoopGroup, workerGroup, factory);
	}


	/**
	 * 创建连接，阻塞直到成功或失败
	 *
	 * @param address  地址
	 */
	public ChannelFuture createChannel(String address) throws UnknownHostException {

		return createChannel(com.cy.iris.commons.util.NetUtil.serverNameToISA(address));

	}

	/**
	 * 创建连接，阻塞直到成功或失败
	 *
	 * @param address  地址
	 */
	public Channel createChannelSync(String address) throws UnknownHostException, ConnectException {

		return createChannelSync(com.cy.iris.commons.util.NetUtil.serverNameToISA(address));
	}


	/**
	 * 创建连接
	 * @param address           地址
	 */
	public ChannelFuture createChannel(InetSocketAddress address)  {

		ArgumentUtil.isNotNull(address,"address");

		ChannelFuture channelFuture = this.bootstrap.connect(address);

		return channelFuture;
	}

	/**
	 * 同步创建连接
	 *
	 * @param address
	 * @return
	 * @throws ConnectException
	 */
	public Channel createChannelSync(InetSocketAddress address) throws ConnectException {

		ChannelFuture channelFuture = this.bootstrap.connect(address);
		try {
			channelFuture.sync();

		} catch (InterruptedException e) {
			logger.error("连接远程服务器被终止,逻辑上不应该发生此错误",e);
			throw new ConnectException(e,"连接远程服务器:"+address+"终止,逻辑上不应该发生此错误");
		}

		if (!channelFuture.isSuccess() || channelFuture.channel() == null || !channelFuture.channel().isActive()) {
			throw new ConnectException(channelFuture.cause(),"向address:"+address+"创建连接失败");
		}

		return channelFuture.channel();
	}

	@Override
	public void doStart() throws Exception {
		super.doStart();
		// Netty客户端启动器
		bootstrap = new Bootstrap();
		bootstrap.group(ioLoopGroup).channel(config.isEpoll() ? EpollSocketChannel.class : NioSocketChannel.class)
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, ((NettyClientConfig) config).getConnectionTimeout())
				.option(ChannelOption.TCP_NODELAY,  config.isTcpNoDelay())
				.option(ChannelOption.SO_REUSEADDR, config.isReuseAddress())
				.option(ChannelOption.SO_KEEPALIVE, config.isKeepAlive())
				.option(ChannelOption.SO_LINGER, config.getSoLinger())
				.option(ChannelOption.SO_RCVBUF, config.getSocketBufferSize())
				.option(ChannelOption.SO_SNDBUF, config.getSocketBufferSize())
				.handler(new ClientChannelInitializer());
	}


	public NettyClientConfig getNettyClientConfig() {
		return (NettyClientConfig) config;
	}

}

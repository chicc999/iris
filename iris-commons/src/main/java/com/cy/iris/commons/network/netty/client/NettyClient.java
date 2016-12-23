package com.cy.iris.commons.network.netty.client;

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
	 * @param address           地址
	 */
	public Channel createChannel(String address) {
		if (address == null || address.isEmpty()) {
			throw new IllegalArgumentException("address must not be empty!");
		}
		String[] parts = address.split("[._:]");
		if (parts.length < 5) {
			throw new RuntimeException();
		}
		int port;
		try {
			port = Integer.parseInt(parts[parts.length - 1]);
		} catch (NumberFormatException e) {
			throw new RuntimeException();
		}
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < parts.length - 1; i++) {
			if (i > 0) {
				builder.append('.');
			}
			builder.append(parts[i]);
		}
		String ip = builder.toString();
		try {
			return createChannel(new InetSocketAddress(InetAddress.getByName(ip), port)).channel();
		} catch (UnknownHostException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException();
		} catch (Throwable e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException();
		}
	}


	/**
	 * 创建连接
	 * @param address           地址
	 */
	public ChannelFuture createChannel(SocketAddress address)  {

		ArgumentUtil.isNotNull(address,"address");

		ChannelFuture channelFuture = this.bootstrap.connect(address);

		return channelFuture;
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

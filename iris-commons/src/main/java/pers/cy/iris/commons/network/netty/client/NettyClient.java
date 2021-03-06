package pers.cy.iris.commons.network.netty.client;

import pers.cy.iris.commons.exception.ConnectException;
import pers.cy.iris.commons.network.handler.CommandHandlerFactory;
import pers.cy.iris.commons.network.netty.NettyTransport;
import pers.cy.iris.commons.util.ArgumentUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.EventExecutorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * Netty客户端
 */
public class NettyClient extends NettyTransport {

	private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);

	// Netty客户端启动器
	protected Bootstrap bootStrap;

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
	 * @param address 地址
	 */
	public ChannelFuture createChannel(String address) throws UnknownHostException {

		return createChannel(pers.cy.iris.commons.util.NetUtil.serverNameToISA(address));

	}

	/**
	 * 创建连接，阻塞直到成功或失败
	 *
	 * @param address 地址
	 */
	public Channel createChannelSync(String address) throws UnknownHostException, ConnectException {

		return createChannelSync(pers.cy.iris.commons.util.NetUtil.serverNameToISA(address));
	}


	/**
	 * 创建连接
	 *
	 * @param address 地址
	 */
	public ChannelFuture createChannel(InetSocketAddress address) {

		ArgumentUtil.isNotNull("address", address);

		ChannelFuture channelFuture = this.bootStrap.connect(address);

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
		return createChannelSync(address,0);
	}

	/**
	 * 同步创建连接
	 *
	 * @param address 目标地址
	 * @param connectionTimeout 等待连接建立的最大超时
	 * @return
	 * @throws ConnectException
	 */
	public Channel createChannelSync(InetSocketAddress address, long connectionTimeout) throws ConnectException {

		if (connectionTimeout <= 0) {
			connectionTimeout = ((NettyClientConfig) config).getConnectionTimeout();
		}

		ChannelFuture channelFuture ;
		try {
			channelFuture = this.bootStrap.connect(address);

			channelFuture.await(connectionTimeout);
			if(!channelFuture.await(connectionTimeout)){
				throw new ConnectException("连接超时,"+address.getAddress().getHostAddress()+":"+address.getPort());
			}

			if (!channelFuture.isSuccess() || channelFuture.channel() == null || !channelFuture.channel().isActive()) {
				throw new ConnectException(channelFuture.cause(), "向address:" + address + "创建连接失败");
			}

		} catch (InterruptedException e) {
			throw new ConnectException(e, "连接远程服务器:" + address + "时，线程 " + Thread.currentThread().getName() + " 被被中断");
		}
		return channelFuture.channel();
	}

	@Override
	public void doStart() throws Exception {
		super.doStart();
		// Netty客户端启动器
		bootStrap = new Bootstrap();
		bootStrap.group(ioLoopGroup).channel(config.isEpoll() ? EpollSocketChannel.class : NioSocketChannel.class)
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, ((NettyClientConfig) config).getConnectionTimeout())
				.option(ChannelOption.TCP_NODELAY, config.isTcpNoDelay())
				.option(ChannelOption.SO_REUSEADDR, config.isReuseAddress())
				.option(ChannelOption.SO_KEEPALIVE, config.isKeepAlive())
				.option(ChannelOption.SO_LINGER, config.getSoLinger())
				.option(ChannelOption.SO_RCVBUF, config.getSocketBufferSize())
				.option(ChannelOption.SO_SNDBUF, config.getSocketBufferSize())
				//初始化handler
				.handler(handler());
	}


	public NettyClientConfig getNettyClientConfig() {
		return (NettyClientConfig) config;
	}

}

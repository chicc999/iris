package com.cy.iris.commons.network.netty;


import com.cy.iris.commons.exception.RemotingIOException;
import com.cy.iris.commons.exception.RequestTimeoutException;
import com.cy.iris.commons.network.CommandCallback;
import com.cy.iris.commons.network.ResponseFuture;
import com.cy.iris.commons.network.Transport;
import com.cy.iris.commons.network.handler.CommandHandlerFactory;
import com.cy.iris.commons.network.handler.DefaultConnectionHandler;
import com.cy.iris.commons.network.handler.DefaultDispatcherHandler;
import com.cy.iris.commons.network.handler.DefaultHandlerFactory;
import com.cy.iris.commons.network.protocol.Command;
import com.cy.iris.commons.service.Service;
import com.cy.iris.commons.util.ArgumentUtil;
import com.cy.iris.commons.util.NamedThreadFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.*;

/**
 * Netty传输基类,提供client和server的公共逻辑的抽象.
 */
public abstract class NettyTransport extends Service implements Transport {

	private static final Logger logger = LoggerFactory.getLogger(NettyTransport.class);

	// 异步回调 & 业务处理执行器
	protected ExecutorService serviceExecutor;

	// 传输配置
	protected NettyConfig config;

	// 命令处理器工厂
	protected CommandHandlerFactory factory;

	// IO处理线程池
	protected EventLoopGroup ioLoopGroup;

	// 是否是内部创建的IO处理线程池
	protected boolean createIoLoopGroup;

	// 存放同步和异步命令应答
	protected final Map<Integer, ResponseFuture> futures = new ConcurrentHashMap<Integer, ResponseFuture>(100);

	protected DefaultDispatcherHandler dispatcherHandler;

	protected DefaultConnectionHandler connectionHandler;

	private ScheduledExecutorService clear;

	public NettyTransport(NettyConfig config) {
		this(config, null, null, null);
	}

	public NettyTransport(NettyConfig config, EventLoopGroup ioLoopGroup,
						  ExecutorService serviceExecutor, CommandHandlerFactory factory) {
		this.serviceExecutor = serviceExecutor;
		this.config = config;
		this.ioLoopGroup = ioLoopGroup;
		this.factory = factory;
	}

	@Override
	public Command sync(Channel channel, Command command) throws RemotingIOException, RequestTimeoutException {
		return sync(channel, command, config.getSendTimeout());
	}

	@Override
	public Command sync(Channel channel, Command command, int timeout) throws RemotingIOException, RequestTimeoutException {

		int sendTimeout = timeout <= 0 ? config.getSendTimeout() : timeout;
		// 同步调用
		ResponseFuture future = async(channel, command, null);
		future.setTimeout(sendTimeout);

		Command response;
		try {
			response = future.get(sendTimeout);
		} catch (InterruptedException e) {
			throw new RemotingIOException("线程被中断", e);
		}
		if (!future.isDone()) {
			throw new RequestTimeoutException("请求 requestId=" + command.getRequestId() + " 超时");
		}
		if (!future.isSuccess()) {
			throw new RemotingIOException(future.getCause());
		}
		return response;
	}

	@Override
	public ResponseFuture async(Channel channel, Command command, CommandCallback callback)
			throws RemotingIOException {
		ArgumentUtil.isNotNull(new String[]{"channel", "command", "callback"}, channel, command, callback);

		// 同步调用
		final ResponseFuture responseFuture = new ResponseFuture(channel, command, config.getSendTimeout(), callback);

		futures.put(command.getRequestId(), responseFuture);

		channel.writeAndFlush(command).addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				//写入过程出现异常,通知response,移除requestId,断开连接
				if (!future.isSuccess()) {
					Throwable failCause;
					if (future.cause() == null) {
						failCause = new Throwable("发送请求发生了未知的错误");
					} else {
						failCause = future.cause();
					}
					logger.error("send request error", failCause);
					responseFuture.cancel(failCause);
					futures.remove(responseFuture.getRequestId());
					future.channel().close();
				}
			}
		});
		return responseFuture;
	}

	/**
	 * 启动前
	 *
	 * @throws Exception
	 */
	@Override
	public void beforeStart() throws Exception {

	}

	/**
	 * 启动
	 *
	 * @throws Exception
	 */
	@Override
	public void doStart() throws Exception {
		if (factory == null) {
			factory = new DefaultHandlerFactory();
		}

		if (connectionHandler == null) {
			connectionHandler = new DefaultConnectionHandler();
		}

		if (serviceExecutor == null) {
			//创建业务&回调线程池
			serviceExecutor = new ThreadPoolExecutor(config.getServiceExecutorThreads(),
					config.getServiceExecutorThreads(),0L, TimeUnit.MILLISECONDS,
					new ArrayBlockingQueue<Runnable>(1000),new NamedThreadFactory("ServiceExecutor"));
		}

		if (dispatcherHandler == null) {
			dispatcherHandler = new DefaultDispatcherHandler(factory,serviceExecutor,futures);
		}


		//如果io线程不存在,创建io线程
		if (ioLoopGroup == null) {
			ioLoopGroup = createEventLoopGroup(config.getWorkerThreads(), new NamedThreadFactory("IoLoopGroup"));
			createIoLoopGroup = true;
		}

		clear = Executors.newScheduledThreadPool(1);
		clear.scheduleWithFixedDelay(new ClearTimeoutFutureTask(this, futures), 3000, 1000, TimeUnit.MILLISECONDS);

	}

	/**
	 * 启动后
	 *
	 * @throws Exception
	 */
	@Override
	public void afterStart() throws Exception {

	}

	/**
	 * 停止前
	 */
	@Override
	public void beforeStop() {

	}

	/**
	 * 停止
	 */
	@Override
	public void doStop() {
		if (createIoLoopGroup) {
			ioLoopGroup.shutdownGracefully();
			ioLoopGroup = null;
			createIoLoopGroup = false;
		}
		serviceExecutor.shutdown();
		clear.shutdown();

	}

	/**
	 * 创建事件循环组，尽量共享
	 *
	 * @param threads       线程数
	 * @param threadFactory 线程工厂类
	 * @return 事件组
	 */
	protected EventLoopGroup createEventLoopGroup(int threads, ThreadFactory threadFactory) {
		if (config.isEpoll()) {
			return new EpollEventLoopGroup(threads, threadFactory);
		}
		return new NioEventLoopGroup(threads, threadFactory);
	}

	public void setDispatcherHandler(DefaultDispatcherHandler dispatcherHandler) {
		this.dispatcherHandler = dispatcherHandler;
	}

	public void setConnectionHandler(DefaultConnectionHandler connectionHandler) {
		this.connectionHandler = connectionHandler;
	}
}

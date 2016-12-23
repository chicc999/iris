package com.cy.iris.commons.network.netty;


import com.cy.iris.commons.exception.RemotingIOException;
import com.cy.iris.commons.network.CommandCallback;
import com.cy.iris.commons.network.ResponseFuture;
import com.cy.iris.commons.network.Transport;
import com.cy.iris.commons.network.handler.CommandHandlerFactory;
import com.cy.iris.commons.network.protocol.Command;
import com.cy.iris.commons.service.Service;
import com.cy.iris.commons.util.NamedThreadFactory;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Created by cy on 2016/7/10.
 */
public abstract class NettyTransport extends Service implements Transport {

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

    public NettyTransport(NettyConfig config) {
		this(config,null,null,null);
	}

    public NettyTransport(NettyConfig config, EventLoopGroup ioLoopGroup,
                          ExecutorService serviceExecutor,CommandHandlerFactory factory) {
        this.serviceExecutor = serviceExecutor;
        this.config = config;
        this.ioLoopGroup = ioLoopGroup;
        this.factory = factory;
    }

    @Override
    public Command sync(Channel channel, Command command) throws RemotingIOException {
        return sync(channel,command,config.getSendTimeout());
    }

    @Override
    public Command sync(Channel channel, Command command, int timeout) throws RemotingIOException {

        int sendTimeout = timeout <= 0 ? config.getSendTimeout() : timeout;
        // 同步调用
        ResponseFuture future = async(channel,command,null);

        Command response;
        try {
            response = future.get(sendTimeout);
        } catch (InterruptedException e) {
            throw new RemotingIOException("线程被中断",e);
        }
        return response;
    }

    @Override
    public ResponseFuture async(Channel channel, Command command, CommandCallback callback)
            throws RemotingIOException {
        if (channel == null) {
            throw new IllegalArgumentException("The argument channel must not be null");
        }

        if (command == null) {
            throw new IllegalArgumentException("The argument command must not be null");
        }

        if (callback == null) {
            throw new IllegalArgumentException("The argument callback must not be null");
        }

        // 同步调用
        ResponseFuture future = new ResponseFuture(channel,command,0,callback);

        channel.writeAndFlush(command);
        return future;
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

        if (ioLoopGroup == null) {
            ioLoopGroup = createEventLoopGroup(config.getWorkerThreads(), new NamedThreadFactory("IoLoopGroup"));
            createIoLoopGroup = true;
        }
        serviceExecutor = Executors
                .newFixedThreadPool(config.getCallbackExecutorThreads(), new NamedThreadFactory("AsyncCallback"));

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
    public void beforeStop()  {

    }

    /**
     * 停止
     */
    @Override
    public void doStop()  {
        if (createIoLoopGroup) {
            ioLoopGroup.shutdownGracefully();
            ioLoopGroup = null;
            createIoLoopGroup = false;
        }
        serviceExecutor.shutdown();

    }

    /**
     * 停止后
     */
    @Override
    public void afterStop()  {

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
}

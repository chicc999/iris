package com.cy.iris.commons.network;


import com.cy.iris.commons.exception.RemotingIOException;
import com.cy.iris.commons.exception.RequestTimeoutException;
import com.cy.iris.commons.network.protocol.*;

import io.netty.channel.Channel;

/**
 * 通信接口,提供同步,异步接口
 */
public interface Transport {
	/**
	 * 同步发送，需要应答
	 *
	 * @param channel 通道
	 * @param command 命令
	 * @return 应答命令
	 * @throws RemotingIOException
	 */
	Command sync(Channel channel, Command command) throws RemotingIOException,RequestTimeoutException;

	/**
	 * 同步发送，需要应答
	 *
	 * @param channel 通道
	 * @param command 命令
	 * @param timeout 超时
	 * @return 应答命令
	 * @throws RemotingIOException
	 */
	Command sync(Channel channel, Command command, int timeout) throws RemotingIOException,RequestTimeoutException;

	/**
	 * 异步发送，需要应答
	 *
	 * @param channel  通道
	 * @param command  命令
	 * @param callback 回调
	 * @return 异步执行的结果
	 * @throws RemotingIOException
	 */
	ResponseFuture async(Channel channel, Command command, CommandCallback callback) throws RemotingIOException;
}

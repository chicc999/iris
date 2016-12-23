package com.cy.iris.commons.network.handler;

import com.cy.iris.commons.network.protocol.Command;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ExecutorService;

/**
 * 命令处理器抽象，具体的处理器实现类用于处理特定的命令类型。
 * 因此每种类型的命令应实现对应自己的处理器以实现具体的业务逻辑。
 */
public interface CommandHandler {

	/**
	 * 命令处理方法，用于实现特定类型命令处理的逻辑。
	 *
	 * @param ctx     Netty渠道处理器上下文
	 * @param command 请求命令
	 * @return 处理完请求后返回针对该请求的一个响应命令
	 * @throws Exception 处理过程中发生的所有异常在方法内部需要进行捕获并转化为JMQException抛出
	 */
	Command process(ChannelHandlerContext ctx, Command command) throws Exception;

	/**
	 * 获取处理当前命令的线程执行器
	 *
	 * @param command 发送的请求命令
	 * @return 处理当前命令的线程执行器
	 */
	ExecutorService getExecutorService(Command command);

}
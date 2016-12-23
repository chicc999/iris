package com.cy.iris.commons.network;

import com.cy.iris.commons.network.protocol.*;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 表示异步通信的结果。
 * 它提供了检查通信是否完成的方法，以等待通信的完成，并获取通信的结果。
 * 通信完成后只能使用 get 方法来获取结果，如有必要，通信完成前可以阻塞此方法。
 * 取消则由 cancel 方法来执行。还提供了其他方法，以确定任务是正常完成还是被取消了。
 * 一旦通信完成，就不能再取消通信。
 */
public class ResponseFuture {
	protected static Logger logger = LoggerFactory.getLogger(ResponseFuture.class);
	// 开始事件
	private final long beginTime = System.currentTimeMillis();
	// 请求
	private Command request;
	// 应答
	private Command response;
	// 异常
	private Throwable cause;
	// 超时
	private long timeout;
	// 通道
	private Channel channel;
	// 回调
	private CommandCallback callback;
	// 是否成功
	private boolean isSuccess;
	//是否完成
	private boolean isDone;
	//是否完成
	private boolean isCancel;
	// 回调一次
	private AtomicBoolean onceCallback = new AtomicBoolean(false);
	// 是否释放
	private AtomicBoolean released = new AtomicBoolean(false);
	//是否正在处理
	private final AtomicBoolean isProcessed = new AtomicBoolean(false);
	// 门闩
	private CountDownLatch latch = new CountDownLatch(1);

	/**
	 * 异步调用构造函数
	 *
	 * @param channel   通道
	 * @param request   请求
	 * @param timeout   超时
	 * @param callback  异步调用回调
	 */
	public ResponseFuture(Channel channel, Command request, long timeout, CommandCallback callback) {
		if (request == null) {
			throw new IllegalArgumentException("request can not be null");
		}
		this.channel = channel;
		this.request = request;
		this.timeout = timeout;
		this.callback = callback;
	}

	/**
	 * 异步调用构造函数
	 *
	 * @param channel   通道
	 * @param request   请求
	 * @param timeout   超时
	 */
	public ResponseFuture(Channel channel, Command request, long timeout) {
		if (request == null) {
			throw new IllegalArgumentException("request can not be null");
		}
		this.channel = channel;
		this.request = request;
		this.timeout = timeout;
	}

	public Command getRequest() {
		return request;
	}

	public Command getResponse() {
		return this.response;
	}

	public void setResponse(Command response) {
		this.response = response;
	}

	public Throwable getCause() {
		return this.cause;
	}

	public void setCause(Throwable cause) {
		this.cause = cause;
	}

	public long getTimeout() {
		return timeout;
	}

	public String getRequestId() {
		return this.request.getRequestId();
	}

	public CommandCallback getCallback() {
		return this.callback;
	}

	public long getBeginTime() {
		return beginTime;
	}

	public boolean isSuccess() {
		return this.isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public Channel getChannel() {
		return channel;
	}

	/**
	 * 是否超时
	 *
	 * @return
	 */
	public boolean isTimeout() {
		return System.currentTimeMillis() > beginTime + timeout;
	}


	/**
	 * 等待返回
	 *
	 * @param timeout 超时时间
	 * @return 应答命令
	 * @throws InterruptedException
	 */
	public Command get(long timeout) throws InterruptedException {
		if (latch != null) {
			latch.await(timeout, TimeUnit.MILLISECONDS);
		}
		return response;
	}

	/**
	 * 回调
	 */
	public void callback() {
		if (callback == null) {
			return;
		}
		if (onceCallback.compareAndSet(false, true)) {
			try {
				if (isSuccess()) {
					callback.onSuccess(request, response);
				} else if (cause != null) {
					callback.onException(request, cause);
				} else {
					logger.error("bigbug: success and exception confused! {}", request);
				}
			} catch (Throwable ignored) {
				logger.error("callback error",ignored);
			}
		}
	}

	/**
	 * 确定成功
	 */
	private void onSuccess() {
		setSuccess(true);
		callback();
	}

	/**
	 * 确定失败
	 */
	private void onFailed(Throwable cause) {
		setSuccess(false);
		setCause(cause);
		callback();
	}


	public boolean cancel(Throwable cause) {
		if (isProcessed.getAndSet(true)) {
			return false;
		}
		onFailed(cause);
		isCancel = true;
		latch.countDown();
		return true;
	}

	public boolean done() {
		if (isProcessed.getAndSet(true)) {
			return false;
		}
		isDone = true;
		latch.countDown();
		onSuccess();
		return true;
	}

	/**
	 * 释放资源，不回调
	 *
	 * @return 成功标示
	 */
	public boolean release() {
		return release(null, false);
	}

	/**
	 * 释放资源，并回调
	 *
	 * @param e        异常
	 * @param callback 是否回调
	 * @return 成功标示
	 */
	public boolean release(final Throwable e, final boolean callback) {
		// 确保释放一次
		if (released.compareAndSet(false, true)) {
			// 设置了异常，则不成功
			if (e != null) {
				isSuccess = false;
				cause = e;
			}
			// 释放请求资源
			if (request != null) {
				//request.release();
			}
			// 唤醒同步等待线程
			if (latch != null) {
				latch.countDown();
			}
			// 回调
			if (callback) {
				this.callback();
			}
			// 清空资源引用
			request = null;
			return true;
		}
		return false;
	}

	public boolean isCancelled() {
		return isCancel;
	}

	public boolean isDone() {
		return isDone;
	}

}

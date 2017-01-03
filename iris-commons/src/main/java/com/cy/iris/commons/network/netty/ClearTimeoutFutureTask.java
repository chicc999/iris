package com.cy.iris.commons.network.netty;

import com.cy.iris.commons.exception.RemotingIOException;
import com.cy.iris.commons.network.ResponseFuture;
import com.cy.iris.commons.service.LifeCycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;

/**
 * 清理超时的请求
 */
public class ClearTimeoutFutureTask implements Runnable{

	private static final Logger logger = LoggerFactory.getLogger(ClearTimeoutFutureTask.class);

	LifeCycle parent;

	// 存放同步和异步命令应答
	protected final Map<Integer, ResponseFuture> futures ;

	public ClearTimeoutFutureTask(LifeCycle nettyTransPort, Map<Integer,ResponseFuture> futures) {
		this.parent = nettyTransPort;
		this.futures = futures;
	}

	@Override
	public void run() {
		if(!parent.isStarted()){
			return;
		}

		Iterator<Map.Entry<Integer, ResponseFuture>> it = futures.entrySet().iterator();
		Map.Entry<Integer, ResponseFuture> entry;
		ResponseFuture responseFuture;
		long timeout;
		while (it.hasNext()) {
			entry = it.next();
			responseFuture = entry.getValue();
			timeout = responseFuture.getBeginTime() + responseFuture.getTimeout() + 1000;

			if (timeout <= System.currentTimeMillis()) {
				it.remove();
				if (responseFuture.release()) {
					try {
						responseFuture.cancel(new RemotingIOException("连接"+responseFuture.getChannel()+"的请求"+responseFuture.getRequestId()+"超时"));
					} catch (Throwable e) {
						logger.error("clear timeout response exception", e);
					}
					logger.info(String.format("remove timeout request id=%d begin=%d timeout=%d",
							responseFuture.getRequestId(), responseFuture.getBeginTime(), timeout));
				}
			}
		}
	}
}

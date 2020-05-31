package pers.cy.iris.coordinator.raft.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.cy.iris.commons.service.Service;
import pers.cy.iris.commons.util.ArgumentUtil;
import pers.cy.iris.commons.util.NamedThreadFactory;
import pers.cy.iris.commons.util.eventmanager.EventManager;

import java.util.Random;
import java.util.concurrent.*;

/**
 * @Author:cy
 * @Date:Created in  2020/5/30
 * @Destription: 检查是否需要启动选举的任务
 */
public class ElectionCheckService extends Service{

	private Logger logger = LoggerFactory.getLogger(ElectionCheckService.class);

	private static final int DEFAULT_INTERVAL_MILLIS = 150;
	private static final int MIN_TIMEOUT_MILLIS = 1;

	private ScheduledExecutorService executorService;

	private ScheduledFuture<?> electionTimeoutFuture;

	private final int fixedInterval ;

	private final int randomInterVal ;

	private long electionTimeout;

	private volatile long lastReadTime;

	private EventManager<ElectionEvent> electionEventManager;

	private ElectionTimeoutTask electionTimeoutTask;

	public ElectionCheckService() {
		this.fixedInterval = DEFAULT_INTERVAL_MILLIS;
		this.randomInterVal = DEFAULT_INTERVAL_MILLIS;
	}

	public ElectionCheckService(int fixedInterval, int randomInterVal,TimeUnit unit) {

		if (unit == null) {
			throw new NullPointerException("unit");
		}

		this.fixedInterval = Math.max(fixedInterval, MIN_TIMEOUT_MILLIS);
		this.randomInterVal = Math.max(randomInterVal, MIN_TIMEOUT_MILLIS);
	}

	@Override
	public void beforeStart() throws Exception {
		if(executorService==null){
			executorService = new ScheduledThreadPoolExecutor(1,new NamedThreadFactory("election_Check"));
		}

		lastReadTime = System.nanoTime();

		ArgumentUtil.isNotNull("electionEventManager",electionEventManager);

		this.electionTimeoutTask = new ElectionTimeoutTask();

	}

	@Override
	public void doStart() throws Exception {
		if(!electionEventManager.isStarted()) {
			electionEventManager.start();
		}
	}

	@Override
	public void afterStart() throws Exception {

	}

	@Override
	public void beforeStop() {

	}

	@Override
	public void doStop() throws Exception {
		executorService.shutdown();
	}

	public void notifyReadFromLeader(){
		lastReadTime = System.nanoTime();
	}


	public void newTerm(){
		if(electionTimeoutFuture != null) {
			// 取消上个任期的选举超时检查任务
			this.electionTimeoutFuture.cancel(true);
		}

		// 每个任期重新生成选举超时时间，避免split vote
		this.electionTimeout = TimeUnit.MICROSECONDS.toNanos(
				(long)(fixedInterval + new Random().nextInt(randomInterVal)));

		this.lastReadTime = System.nanoTime();

		executorService.schedule(electionTimeoutTask,electionTimeout,TimeUnit.NANOSECONDS);

	}

	private final class ElectionTimeoutTask implements Runnable {

		@Override
		public void run() {
			if(!isStarted()){
				return;
			}

			try {
				long nextDelay = electionTimeout - (System.nanoTime() - lastReadTime);
				if (nextDelay <= 0) {
					// 没收到leader的心跳或者指令，失联
					// 添加身份切换事件，变为候选人
					electionEventManager.add(new ElectionEvent(ClusterRole.CANDIDATE));
				} else {
					// 中间有接收到过数据，从此节点开始计时
					electionTimeoutFuture = executorService.schedule(this, nextDelay, TimeUnit.NANOSECONDS);
				}
			}catch (Exception e){
				logger.error(e.getMessage(),e);
			}
		}
	}
}

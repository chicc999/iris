package com.cy.iris.commons.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 单线程每毫秒获取一次并缓存,避免大量获取时间的系统调用
 */
public class SystemClock {

	private static final SystemClock instance = new SystemClock();

	private final long precision;
	private final AtomicLong now;
	private ScheduledExecutorService scheduler;

	private SystemClock() {
		this(1L);
	}

	private SystemClock(long precision) {
		this.precision = precision;
		now = new AtomicLong(System.currentTimeMillis());
		scheduler = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
			@Override
			public Thread newThread(Runnable runnable) {
				Thread thread = new Thread(runnable, "System_Clock");
				thread.setDaemon(true);
				return thread;
			}
		});
		scheduler.scheduleAtFixedRate(new Timer(), precision, precision, TimeUnit.MILLISECONDS);
	}

	public static long now() {
		return instance.now.get();
	}

	public static long precision() {
		return instance.precision;
	}

	protected class Timer implements Runnable {

		@Override
		public void run() {
			now.set(System.currentTimeMillis());
		}
	}
}

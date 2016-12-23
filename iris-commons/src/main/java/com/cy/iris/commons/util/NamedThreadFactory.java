package com.cy.iris.commons.util;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程工厂类,主要提供给线程命名以及是否设置为守护线程
 */
public class NamedThreadFactory implements ThreadFactory {
	private AtomicInteger counter = new AtomicInteger(0);

	private String name;
	private boolean daemon;

	public NamedThreadFactory(String name) {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("name is empty");
		}
		this.name = name;
	}

	public NamedThreadFactory(String name, boolean daemon) {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("name is empty");
		}
		this.name = name;
		this.daemon = daemon;
	}

	@Override
	public Thread newThread(Runnable r) {
		Thread thread = new Thread(r);
		thread.setName(name + " - " + counter.incrementAndGet());
		if (daemon) {
			thread.setDaemon(daemon);
		}
		return thread;
	}
}

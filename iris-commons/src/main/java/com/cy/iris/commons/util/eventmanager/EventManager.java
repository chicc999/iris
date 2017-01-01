package com.cy.iris.commons.util.eventmanager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 事件管理器
 *
 *
 * 事件派发器
 * 如果 interval>0 ,则开启事件合并,每次只发布最后一个事件.否则发布每一个事件;
 * 如果 triggerNoEvent==true ,则在没有事件时,也触发空事件 (触发间隔interval+timeout);
 * 如果 idleTime>0 ,触发空闲检测.触发间隔为 (最后一次非空事件触发时间+idleTime).
 */
public class EventManager<E> {
	// 监听器
	protected CopyOnWriteArrayList<EventListener<E>> listeners = new CopyOnWriteArrayList<EventListener<E>>();
	// 事件队列
	protected BlockingQueue<EventOwner> eventsQueue;
	// 线程名称
	protected String name;
	// 事件派发线程
	protected Thread dispatcher;
	// 事件派发处理器
	protected EventDispatcher eventDispatcher ;
	// 启动标示
	protected AtomicBoolean started = new AtomicBoolean(false);
	// 没有事件的也触发监听器
	protected boolean triggerNoEvent;
	// 事件的间隔（毫秒），并合并事件
	protected long interval;
	// 空闲时间
	protected long idleTime;

	// 从队列中获取事件的超时时间,如果队列中没有数据,最多等待此时间
	private long timeout ;

	public EventManager() {
		this(null, 0);
	}

	public EventManager(EventListener<E> listener) {
		this(null, listener, 0);
	}

	public EventManager(String name) {
		this(name, 0);
	}

	public EventManager(String name, int capacity) {
		this.name = name;
		if (capacity > 0) {
			eventsQueue = new ArrayBlockingQueue<EventOwner>(capacity);
		} else {
			eventsQueue = new LinkedBlockingDeque<EventOwner>();
		}
		eventDispatcher = new EventDispatcher();
	}

	public EventManager(String name, EventListener<E> listener) {
		this(name, 0);
		addListener(listener);
	}

	public EventManager(String name, EventListener<E> listener, int capacity) {
		this(name, capacity);
		addListener(listener);
	}

	public EventManager(String name, List<? extends EventListener<E>> listeners) {
		this(name, 0);
		if (listeners != null) {
			for (EventListener<E> listener : listeners) {
				addListener(listener);
			}
		}
	}

	public long getInterval() {
		return interval;
	}

	public void setInterval(long interval) {
		this.interval = interval;
	}

	public long getIdleTime() {
		return idleTime;
	}

	public void setIdleTime(long idleTime) {
		this.idleTime = idleTime;
	}

	public long getTimeout() {
		return this.timeout;
	}

	public void setTimeout(long timeout) {
		if (timeout > 0) {
			this.timeout = timeout;
		}
	}

	public boolean isTriggerNoEvent() {
		return triggerNoEvent;
	}

	public void setTriggerNoEvent(boolean triggerNoEvent) {
		this.triggerNoEvent = triggerNoEvent;
	}

	/**
	 * 开始
	 */
	public void start() {
		if (started.compareAndSet(false, true)) {
			// 清理一下，防止里面有数据
			eventsQueue.clear();
			eventDispatcher.start();
			if (name != null) {
				dispatcher = new Thread(eventDispatcher, name);
			} else {
				dispatcher = new Thread(eventDispatcher);
			}
			dispatcher.setDaemon(true);
			dispatcher.start();
		}
	}

	/**
	 * 结束
	 */
	public void stop() {
		stop(false);
	}

	/**
	 * 结束
	 *
	 * @param gracefully 优雅停止
	 */
	public void stop(boolean gracefully) {
		if (started.compareAndSet(true, false)) {
			if (dispatcher != null) {
				dispatcher.interrupt();
				dispatcher = null;
				eventDispatcher.stop(gracefully);
				eventsQueue.clear();
			}
		}
	}

	/**
	 * 是否启动
	 *
	 * @return 启动标示
	 */
	public boolean isStarted() {
		return started.get();
	}

	/**
	 * 增加监听器
	 *
	 * @param listener
	 */
	public boolean addListener(EventListener<E> listener) {
		if (listener != null) {
			return listeners.addIfAbsent(listener);
		}
		return false;
	}

	/**
	 * 删除监听器
	 *
	 * @param listener
	 */
	public boolean removeListener(EventListener<E> listener) {
		if (listener != null) {
			return listeners.remove(listener);
		}
		return false;
	}

	/**
	 * 获取监听器
	 * @return 所有监听器列表
	 */
	public List<EventListener<E>> getListeners() {
		return listeners;
	}


	/**
	 * 异步发布事件
	 *
	 * @param event 事件
	 */
	public boolean add(final E event) {
		return add(event, null);
	}

	/**
	 * 异步发布事件
	 *
	 * @param event   事件
	 * @param timeout 超时
	 * @param unit    时间单位
	 */
	public boolean add(final E event, final long timeout, final TimeUnit unit) {
		return add(event, null, timeout, unit);
	}

	/**
	 * 异步发布发布事件
	 *
	 * @param event 事件
	 * @param owner 所有者
	 */
	public boolean add(final E event, EventListener<E> owner) {
		if (event == null) {
			return false;
		}
		try {
			eventsQueue.put(new EventOwner(event, owner));
			return true;
		} catch (InterruptedException e) {
			// 让当前线程中断
			Thread.currentThread().interrupt();
			return false;
		}
	}

	/**
	 * 异步发布发布事件
	 *
	 * @param event   事件
	 * @param owner   所有者
	 * @param timeout 超时
	 * @param unit    时间单位
	 */
	public boolean add(final E event, EventListener<E> owner, final long timeout, final TimeUnit unit) {
		if (event == null) {
			return false;
		}
		try {
			return eventsQueue.offer(new EventOwner(event, owner), timeout, unit);
		} catch (InterruptedException e) {
			// 让当前线程中断
			Thread.currentThread().interrupt();
			return false;
		}
	}

	/**
	 * 同步通知事件
	 *
	 * 由添加事件线程直接完成,避免事件派发线程异步派发以及排队触发
	 *
	 * @param event 事件
	 */
	public void inform(E event) throws Exception {
		if (event == null) {
			return;
		}
		for (EventListener<E> listener : listeners) {
			
			listener.onEvent(event);
			
		}
	}

	/**
	 * 空闲事件回调方法.
	 */
	protected void onIdle() {

	}

	/**
	 * 合并事件
	 *
	 * @param events 事件列表
	 */
	protected void publish(List<EventOwner> events) {
		if (events == null || events.isEmpty()) {
			return;
		}
		// 发布最后一个事件
		publish(events.get(events.size() - 1));
	}

	/**
	 * 派发消息
	 *
	 * @param event 事件
	 */
	protected void publish(EventOwner event) {
		// 当triggerNoEvent为真时候，event可以为空
		if (event != null && event.getListener() != null) {
			try {
				event.getListener().onEvent(event.getEvent());
			} catch (Throwable ignored) {
			}
		} else {
			E e = event == null ? null : (E)event.getEvent();
			for (EventListener<E> listener : listeners) {
				try {
					listener.onEvent(e);
				} catch (Throwable ignored) {
				}
			}
		}
	}

	/**
	 * 事件派发线程
	 */
	protected class EventDispatcher implements Runnable {

		private AtomicBoolean started = new AtomicBoolean();
		private AtomicBoolean gracefully = new AtomicBoolean(false);
		private CountDownLatch latch;

		public void start() {
			if (started.compareAndSet(false, true)) {
				latch = new CountDownLatch(1);
				gracefully.set(false);
			}
		}

		public void stop(boolean gracefully) {
			if (started.compareAndSet(true, false)) {
				this.gracefully.set(gracefully);
				if (gracefully) {
					try {
						latch.await();
					} catch (InterruptedException e) {
						// 让当前线程中断
						Thread.currentThread().interrupt();
					}
				}
			}
		}

		/**
		 * 线程是否存活
		 *
		 * @return true 线程已经启动且未被中断
		 */
		protected boolean isAlive() {
			return started.get() && !Thread.currentThread().isInterrupted();
		}

		@Override
		public void run() {
			long lastTime = System.currentTimeMillis();
			long now;
			EventOwner eventOwner;
			while (true) {
				try {
					eventOwner = null;
					// 判断是否关闭
					if (isAlive()) {
						// 阻塞的获取数据,除非timeout或者获取到数据
						eventOwner = eventsQueue.poll(timeout, TimeUnit.MILLISECONDS);
					}
					// 如果派发器已经不在存活,非优雅关闭直接退出,优雅关闭则再试一次看看有没有新增的事件
					if (!isAlive()) {
						if (!gracefully.get()) {
							// 非优雅关闭
							break;
						}
						//优雅关闭，如果当前事件为空，则重新取一次
						if (eventOwner == null) {
							if (!Thread.currentThread().isInterrupted()) {
								eventOwner = eventsQueue.poll(50, TimeUnit.MILLISECONDS);
							} else {
								eventOwner = eventsQueue.poll();
							}
						}
						if (eventOwner == null) {
							// 优雅退出
							break;
						}
					}

					// 当前事件不为空
					if (eventOwner != null) {
						if (idleTime > 0) {
							// 启用空闲检测
							lastTime = System.currentTimeMillis();

						}

						// 合并事件
						if (interval > 0) {
							// 获取当前所有事件
							List<EventOwner> currents = new ArrayList<EventOwner>();
							currents.add(eventOwner);
							while (!eventsQueue.isEmpty()) {
								eventOwner = eventsQueue.poll();
								if (eventOwner != null) {
									currents.add(eventOwner);
								}
							}
							// 合并事件
							publish(currents);
						} else {
							publish(eventOwner);
						}
					} else {
						if (triggerNoEvent) {
							// 出发空事件
							publish((EventOwner) null);
						}
						if (idleTime > 0) {
							// 启用空闲检测
							now = System.currentTimeMillis();
							if (now - lastTime > idleTime) {
								lastTime = now;
								onIdle();
							}
						}
					}
					if (interval > 0 && isAlive()) {
						// 休息间隔时间
						Thread.sleep(interval);
					}
				} catch (InterruptedException e) {
					// 让当前线程中断
					Thread.currentThread().interrupt();
				}
			}
			if (latch != null) {
				latch.countDown();
			}

		}
	}

}

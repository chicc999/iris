package com.cy.iris.commons.service;

/**
 * 生命周期管理
 */
public interface LifeCycle {

	/**
	 * 启动
	 *
	 * @throws Exception
	 */
	void start() throws Exception;

	/**
	 * 停止
	 */
	void stop();

	/**
	 * 是否启动，并且不在关闭状态
	 *
	 * @return 启动标示
	 */
	boolean isStarted();

}

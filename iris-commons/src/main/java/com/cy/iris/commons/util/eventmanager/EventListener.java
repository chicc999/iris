package com.cy.iris.commons.util.eventmanager;

/**
 * 事件监听器
 * 对应事件被触发时调用回调
 */
public interface EventListener<E> {

	/**
	 * 事件
	 *
	 * @param event
	 */
	void onEvent(E event);
}

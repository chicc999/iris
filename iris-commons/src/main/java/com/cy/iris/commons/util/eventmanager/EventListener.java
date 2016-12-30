package com.cy.iris.commons.util.eventmanager;

/**
 * 事件监听器
 *
 * @author hexiaofeng
 * @since 2013-12-09
 */
public interface EventListener<E> {

	/**
	 * 事件
	 *
	 * @param event
	 */
	void onEvent(E event);
}

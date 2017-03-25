package com.cy.iris.commons.model;

import java.io.Serializable;

/**
 * @Author:cy
 * @Date:Created in  17/3/24
 * @Destription: 主题类型
 */
public  enum TopicType implements Serializable {
	/**
	 * 主题
	 */
	TOPIC,
	/**
	 * 广播
	 */
	BROADCAST,
	/**
	 * 顺序队列
	 */
	SEQUENTIAL;

	public static TopicType valueOf(final int ordinal) {
		if (ordinal < 0 || ordinal >= values().length) {
			throw new IndexOutOfBoundsException("Invalid ordinal.");
		}
		return values()[ordinal];
	}
}
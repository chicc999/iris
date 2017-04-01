package com.cy.iris.commons.cluster;

/**
 * @Author:cy
 * @Date:Created in  17/3/30
 * @Destription: 复制方式
 */
public enum SyncMode {

	/**
	 * 异步复制
	 */
	ASYNCHRONOUS,
	/**
	 * 同步复制
	 */
	SYNCHRONOUS;


	public static SyncMode valueOf(int ordinal) {
		if (ordinal < 0 || ordinal >= values().length) {
			throw new IndexOutOfBoundsException("Invalid ordinal.");
		}
		return values()[ordinal];
	}

}
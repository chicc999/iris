package com.cy.iris.commons.model;

/**
 * @Author:cy
 * @Date:Created in  17/3/24
 * @Destription: 偏移量管理方式,由远程服务器管理或者本地管理
 */
public enum OffsetMode {
	REMOTE,
	LOCAL;

	public static OffsetMode valueOf(int ordinal) {
		if (ordinal < 0 || ordinal >= values().length) {
			throw new IndexOutOfBoundsException("Invalid ordinal");
		}
		return values()[ordinal];
	}
}
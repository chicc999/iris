package com.cy.iris.commons.network.protocol;

/**
 * Created by cy on 16/12/27.
 */
public enum HeaderType {
	REQUEST, RESPONSE;

	public static HeaderType valueOf(int ordinal) {
		switch (ordinal) {
			case 0:
				return REQUEST;
			default:
				return RESPONSE;
		}
	}
}

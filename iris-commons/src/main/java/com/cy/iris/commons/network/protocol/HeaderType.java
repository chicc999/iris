package com.cy.iris.commons.network.protocol;

/**
 * Created by cy on 16/12/27.
 */
public enum HeaderType {
	REQUEST("REQUEST"), RESPONSE("RESPONSE");

	private String val;

	HeaderType(String val){
		this.val = val;
	}

	public static HeaderType valueOf(int ordinal) {
		switch (ordinal) {
			case 0:
				return REQUEST;
			default:
				return RESPONSE;
		}
	}

	public static String toString(HeaderType headerType){
		return headerType.val;
	}

}

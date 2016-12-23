package com.cy.iris.commons.exception;

import java.net.SocketTimeoutException;

/**
 * Created by cy on 2016/7/12.
 */
public class ConnectTimeoutException extends SocketTimeoutException {
	public ConnectTimeoutException(String msg) {
		super(msg);
	}

	public ConnectTimeoutException() {
	}
}

package com.cy.iris.commons.exception;

import java.io.IOException;

/**
 * Created by cy on 2016/7/10.
 */
public class RemotingIOException extends IOException {
	public RemotingIOException() {
	}

	public RemotingIOException(String message) {
		super(message);
	}

	public RemotingIOException(String message, Throwable cause) {
		super(message, cause);
	}

	public RemotingIOException(Throwable cause) {
		super(cause);
	}
}

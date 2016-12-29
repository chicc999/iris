package com.cy.iris.commons.exception;

/**
 * Created by cy on 16/12/29.
 */
public class UnknowCommandException extends RuntimeException{
	public UnknowCommandException() {
	}

	public UnknowCommandException(String message) {
		super(message);
	}

	public UnknowCommandException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnknowCommandException(Throwable cause) {
		super(cause);
	}
}

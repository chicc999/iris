package pers.cy.iris.commons.exception;

/**
 * Created by cy on 17/2/10.
 */
public class ServiceTooBusyException extends Exception {
	public ServiceTooBusyException() {
	}

	public ServiceTooBusyException(String message) {
		super(message);
	}

	public ServiceTooBusyException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceTooBusyException(Throwable cause) {
		super(cause);
	}

}

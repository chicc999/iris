package pers.cy.iris.commons.exception;

/**
 * Created by cy on 17/3/9.
 */
public class LockTimeoutException extends Exception{
	public LockTimeoutException() {
	}

	public LockTimeoutException(String message) {
		super(message);
	}

	public LockTimeoutException(String message, Throwable cause) {
		super(message, cause);
	}

	public LockTimeoutException(Throwable cause) {
		super(cause);
	}

}

package pers.cy.iris.commons.exception;

/**
 * @Author:cy
 * @Date:Created in  17/4/22
 * @Destription: 服务不可用异常
 */
public class ServiceNotAvailableException extends Exception {

	private final static String description = "服务不可用";

	public ServiceNotAvailableException() {
		super(description);
	}

	public ServiceNotAvailableException(String detail) {
		super(description + detail);
	}

	public ServiceNotAvailableException(String detail, Throwable cause) {
		super(description + detail, cause);
	}

	public ServiceNotAvailableException(Throwable cause) {
		super(description, cause);
	}
}

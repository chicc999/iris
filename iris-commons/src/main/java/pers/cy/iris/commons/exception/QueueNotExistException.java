package pers.cy.iris.commons.exception;

/**
 * @Author:cy
 * @Date:Created in  17/5/13
 * @Destription:
 */
public class QueueNotExistException extends IrisException {
	private final static String description = "请求错误";

	public QueueNotExistException() {
		super(description);
	}

	public QueueNotExistException(String detail) {
		super(description, detail);
	}

	public QueueNotExistException(String detail, Throwable cause) {
		super(description, detail, cause);
	}
}

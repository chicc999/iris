package pers.cy.iris.commons.exception;

/**
 * @Author:cy
 * @Date:Created in  17/5/19
 * @Destription:
 */
public class ChecksumException extends IrisException {
	private final static String description = "校验和错误";

	public ChecksumException() {
	}

	public ChecksumException(String detail, Throwable cause) {
		super(description, detail, cause);
	}
}

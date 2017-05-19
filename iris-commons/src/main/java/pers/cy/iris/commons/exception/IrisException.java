package pers.cy.iris.commons.exception;

/**
 * @Author:cy
 * @Date:Created in  17/5/13
 * @Destription:  IRIS错误基类
 */
public class IrisException extends Exception{

	public IrisException() {
	}

	public IrisException(String description) {
		super(description);
	}

	public IrisException(String description, String detail) {
		super(description + "，detail：" +detail);
	}

	public IrisException(String description, String detail, Throwable cause) {
		super(description + "，detail：" +detail, cause);
	}
}

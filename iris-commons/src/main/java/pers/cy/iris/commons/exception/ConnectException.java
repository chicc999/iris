package pers.cy.iris.commons.exception;

import java.io.IOException;

/**
 * Created by cy on 2016/7/12.
 */
public class ConnectException extends IOException {

	public ConnectException(Throwable cause,String detail) {
		super(detail,cause);
	}

	public ConnectException() {
	}
}

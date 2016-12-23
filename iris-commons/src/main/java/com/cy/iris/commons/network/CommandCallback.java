package com.cy.iris.commons.network;

import com.cy.iris.commons.network.protocol.*;


/**
 * Created by cy on 2016/7/10.
 */
public interface CommandCallback {
	/**
	 * 成功
	 *
	 * @param request  请求命令
	 * @param response 应答命令
	 */
	void onSuccess(Command request, Command response);

	/**
	 * 出现异常
	 *
	 * @param request 请求命令
	 * @param cause   异常
	 */
	void onException(Command request, Throwable cause);
}

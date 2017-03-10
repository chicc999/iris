package com.cy.iris.client.system;

import com.cy.iris.commons.service.LifeCycle;

import java.io.PrintStream;

/**
 * 系统相关命令
 */
abstract class SystemClient implements LifeCycle{
	protected PrintStream out;
	protected PrintStream err;
	private String cmdStr;
	private String optionStr;

	public SystemClient(String cmdStr, String optionStr) {
		this.out = System.out;
		this.err = System.err;
		this.cmdStr = cmdStr;
		this.optionStr = optionStr;
	}
}

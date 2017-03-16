package com.cy.iris.client.cli.exception;

/**
 * @Author:cy
 * @Date:Created in 15:23 17/3/14
 * @Destription: 未知的命令
 */
public class CommandNotFoundException extends CliException {

	public CommandNotFoundException(String command) {
		super("Command not found: " + command, 127);
	}
}
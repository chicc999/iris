package com.cy.iris.client.cli.command;

import com.cy.iris.client.cli.CliCommand;
import com.cy.iris.client.cli.exception.CliException;
import com.cy.iris.client.cli.exception.CliParseException;

/**
 * @Author:cy
 * @Date:Created in 17:16 17/3/13
 * @Destription:
 */
public class Create extends CliCommand {
	/**
	 * a CLI command with command string and options.
	 * Using System.out and System.err for printing
	 *
	 * @param cmdStr    the string used to call this command
	 * @param optionStr the string used to call this command
	 */
	public Create(String cmdStr, String optionStr) {
		super(cmdStr, optionStr);
	}

	@Override
	public CliCommand parse(String[] cmdArgs) throws CliParseException {
		return null;
	}

	@Override
	public boolean exec() throws CliException {
		return false;
	}
}

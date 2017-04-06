package pers.cy.iris.client.cli.command;

import pers.cy.iris.client.cli.CliCommand;
import pers.cy.iris.client.cli.CommandLine;
import pers.cy.iris.client.cli.Option;
import pers.cy.iris.client.cli.Options;
import pers.cy.iris.client.cli.exception.CliException;
import pers.cy.iris.client.cli.exception.CliParseException;
import pers.cy.iris.client.cli.exception.ParseException;
import pers.cy.iris.client.cli.parser.Parser;
import pers.cy.iris.client.cli.parser.PosixParser;

import static pers.cy.iris.client.cli.Option.UNLIMITED_VALUES;

/**
 * @Author:cy
 * @Date:Created in 17:16 17/3/13
 * @Destription:
 */
public class Create extends CliCommand {

	private static Options options = new Options();
	private String[] args;
	private CommandLine cl;


	/**
	 * a CLI command with command string and options.
	 * Using System.out and System.err for printing
	 *
	 * @param cmdStr    the string used to call this command
	 * @param optionStr the string used to call this command
	 */
	public Create(String cmdStr, String optionStr) {
		super(cmdStr, optionStr);

		Option broker = new Option("b","broker_name", true, "broker的名称,由ip:prot指定");
		options.addOption(broker);
		Option group = new Option("g", "group_name",true, "分组名称");
		group.setArgs(UNLIMITED_VALUES);
		options.addOption(group);
		Option topic = new Option("t", "topic_name",true, "主题名称");
		options.addOption(topic);
		Option app = new Option("app", "app_name",true, "应用名称");
		options.addOption(topic);
	}

	@Override
	public CliCommand parse(String[] cmdArgs) throws CliParseException {
		Parser parser = new PosixParser();
		try {
			cl = parser.parse(options, cmdArgs);
		} catch (ParseException ex) {
			throw new CliParseException(ex);
		}
		args = cl.getArgs();
/*		if(args.length < 2) {
			throw new CliParseException(getUsageStr());
		}*/
		return this;
	}

	@Override
	public boolean exec() throws CliException {
		boolean hasB = cl.hasOption("b");
		boolean hasG = cl.hasOption("g");
		boolean hasT = cl.hasOption("t");
		boolean hasApp = cl.hasOption("app");
		if(!hasB && !hasG && !hasT && !hasApp){
			throw new CliException("create命令后面至少是-b(broker),-g(group),-t(topic),-app(app_name)中的一种");
		}
		return false;
	}
}

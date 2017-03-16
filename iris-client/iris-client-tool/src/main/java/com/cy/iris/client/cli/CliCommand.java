package com.cy.iris.client.cli;

import com.cy.iris.client.cli.exception.CliException;
import com.cy.iris.client.cli.exception.CliParseException;
import com.cy.iris.client.cli.zookeeper.ZookeeperService;
import org.apache.curator.framework.CuratorFramework;

import java.io.PrintStream;
import java.util.Map;

/**
 * @Author:cy
 * @Destription: 命令行命令基类
 * @Date:Created in 16:28 17/3/13
 */
abstract public class CliCommand {
	protected ZookeeperService zk;
	protected PrintStream out;
	protected PrintStream err;
	private String cmdStr;
	private String optionStr;

	/**
	 * a CLI command with command string and options.
	 * Using System.out and System.err for printing
	 * @param cmdStr the string used to call this command
	 * @param optionStr the string used to call this command
	 */
	public CliCommand(String cmdStr, String optionStr) {
		this.out = System.out;
		this.err = System.err;
		this.cmdStr = cmdStr;
		this.optionStr = optionStr;
	}

	/**
	 * Set out printStream (useable for testing)
	 * @param out
	 */
	public void setOut(PrintStream out) {
		this.out = out;
	}

	/**
	 * Set err printStream (useable for testing)
	 * @param err
	 */
	public void setErr(PrintStream err) {
		this.err = err;
	}

	/**
	 * set the zookeper instance
	 * @param zk the ZooKeeper instance.
	 */
	public void setZk(ZookeeperService zk) {
		this.zk = zk;
	}

	/**
	 * get the string used to call this command
	 * @return
	 */
	public String getCmdStr() {
		return cmdStr;
	}

	/**
	 * get the option string
	 * @return
	 */
	public String getOptionStr() {
		return optionStr;
	}

	/**
	 * get a usage string, contains the command and the options
	 * @return
	 */
	public String getUsageStr() {
		return cmdStr + " " + optionStr;
	}

	/**
	 * add this command to a map. Use the command string as key.
	 * @param cmdMap
	 */
	public void addToMap(Map<String, CliCommand> cmdMap) {
		cmdMap.put(cmdStr, this);
	}

	/**
	 * parse the command arguments
	 * @param cmdArgs
	 * @return this CliCommand
	 * @throws CliParseException
	 */
	abstract public CliCommand parse(String cmdArgs[]) throws CliParseException;

	/**
	 *
	 * @return
	 * @throws CliException
	 */
	abstract public boolean exec() throws CliException;
}

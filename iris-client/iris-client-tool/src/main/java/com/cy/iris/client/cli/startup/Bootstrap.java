package com.cy.iris.client.cli.startup;

import com.cy.iris.client.cli.CliCommand;
import com.cy.iris.client.cli.CommandOptions;
import com.cy.iris.client.cli.command.Create;
import com.cy.iris.client.cli.exception.CliException;
import com.cy.iris.client.cli.exception.CommandNotFoundException;
import com.cy.iris.client.cli.zookeeper.ZookeeperService;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.imps.CuratorFrameworkImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * @Author:cy
 * @Date:Created in 17:54 17/3/13
 * @Destription: 启动类
 */
public class Bootstrap {

	private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);

	private ZookeeperService zkClinet ;

	private CommandOptions commandOptions = new CommandOptions();

	protected HashMap<Integer,String> history = new HashMap<Integer,String>( );
	protected int commandCount = 0;

	private static final Map<String,String> commandMap = new HashMap<String,String>( );
	private static final Map<String,CliCommand> commandMapCli =
			new HashMap<String,CliCommand>( );

	protected int exitCode = 0;

	public static void main(String[] args) throws IOException, CliException {
		Bootstrap bootstrap = new Bootstrap();

		bootstrap.init();

	}

	private void init() throws IOException {
		logger.info("开始启动");

		commandMap.put("connect", "host:port");
		commandMap.put("history", "");
		commandMap.put("redo", "cmdno");
		commandMap.put("quit", "");

		Create create = new Create("create","[-b] [-t] [-g] [-app] value ");
		commandMapCli.put(create.getCmdStr(),create);

		// add all to commandMap
		for (Map.Entry<String, CliCommand> entry : commandMapCli.entrySet()) {
			commandMap.put(entry.getKey(), entry.getValue().getOptionStr());
		}

		try {
			zkClinet.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("请输入指令:");
		BufferedReader br =
				new BufferedReader(new InputStreamReader(System.in));

		String line;
		while ((line = br.readLine()) != null) {
			executeLine(line);
		}
	}

	private void executeLine(String line)  {
		if (!line.equals("")) {
			//记录历史操作
			history.put(commandCount,line);
			commandCount++;
			//解析输入行
			commandOptions.parseCommand(line);

			try {
				parseCommand();
				exitCode = 0;
			} catch (CliException e) {
				exitCode = e.getExitCode();
				System.err.println(e.getMessage());
				e.printStackTrace();
			}

		}
	}

	private void parseCommand() throws CliException {
		String[] args = commandOptions.getArgArray();
		String cmd = commandOptions.getCommand();

		if (args.length < 1) {
			usage();
			throw new CliException("No command entered");
		}

		if (!commandMap.containsKey(cmd)) {
			usage();
			throw new CommandNotFoundException("Command not found " + cmd);
		}

		if (cmd.equals("quit")) {
			zkClinet.stop();
			System.exit(exitCode);
		}else if(cmd.equals("history")){
			for (int i = commandCount - 10; i <= commandCount; ++i) {
				if (i < 0) continue;
				System.out.println(i + " - " + history.get(i));
			}
		}

		CliCommand cliCmd = commandMapCli.get(cmd);
		if(cliCmd != null) {
			cliCmd.setZk(zkClinet);
			cliCmd.parse(args).exec();
		} else if (!commandMap.containsKey(cmd)) {
			usage();
		}
	}

	private void usage() {
		System.err.println("按如下规则使用");
		System.err.println("ZooKeeper -server host:port cmd args");
		List<String> cmdList = new ArrayList<String>(commandMap.keySet());
		Collections.sort(cmdList);
		for (String cmd : cmdList) {
			System.err.println("\t"+cmd+ " " + commandMap.get(cmd));
		}
	}
}

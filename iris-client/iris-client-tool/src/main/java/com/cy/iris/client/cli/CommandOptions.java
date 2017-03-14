package com.cy.iris.client.cli;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @Author:cy
 * @Date:Created in 11:30 17/3/14
 * @Destription:
 */
public class CommandOptions {

	private Map<String,String> options = new HashMap<String,String>();
	private List<String> cmdArgs = null;
	private String command = null;
	public static final Pattern ARGS_PATTERN = Pattern.compile("\\s*([^\"\']\\S*|\"[^\"]*\"|'[^']*')\\s*");
	public static final Pattern QUOTED_PATTERN = Pattern.compile("^([\'\"])(.*)(\\1)$");

	public CommandOptions() {
		options.put("server", "localhost:2181");
		options.put("timeout", "30000");
	}

	public String getOption(String opt) {
		return options.get(opt);
	}

	public String getCommand( ) {
		return command;
	}

	public String getCmdArgument( int index ) {
		return cmdArgs.get(index);
	}

	public int getNumArguments( ) {
		return cmdArgs.size();
	}

	public String[] getArgArray() {
		return cmdArgs.toArray(new String[0]);
	}


}

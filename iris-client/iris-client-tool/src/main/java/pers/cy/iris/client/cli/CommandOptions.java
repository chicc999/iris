package pers.cy.iris.client.cli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author:cy
 * @Date:Created in 11:30 17/3/14
 * @Destription:
 */
public class CommandOptions {

	private static final Logger logger = LoggerFactory.getLogger(CommandOptions.class);

	private Map<String,String> options = new HashMap<String,String>();
	private List<String> cmdArgs = null;
	private String command = null;

	//将参数分隔
	public static final Pattern ARGS_PATTERN = Pattern.compile("\\s*([^\"\']\\S*|\"[^\"]*\"|'[^']*')\\s*");
	//匹配单、双引号中的内容
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

	/**
	 * 将输入解析为命令+参数两部分
	 * @param cmdstring "cmd arg1 arg2..etc"
	 * @return true 转化成功
	 */
	public boolean parseCommand( String cmdstring ) {
		Matcher matcher = ARGS_PATTERN.matcher(cmdstring);

		List<String> args = new LinkedList<String>();
		while (matcher.find()) {
			String value = matcher.group(1);
			if (QUOTED_PATTERN.matcher(value).matches()) {
				// Strip off the surrounding quotes
				value = value.substring(1, value.length() - 1);
			}
			args.add(value);
		}
		if (args.isEmpty()){
			return false;
		}
		command = args.get(0);
		cmdArgs = args;

		logger.debug("解析成功,command:{} , args:{}",command,cmdArgs.toString());

		return true;
	}
}

package com.cy.iris.client.cli.parser;

import com.cy.iris.client.cli.CommandLine;
import com.cy.iris.client.cli.Options;
import com.cy.iris.client.cli.exception.ParseException;

/**
 * @Author:cy
 * @Date:Created in 14:07 17/3/14
 * @Destription: CommandLineParser接口的实现类要能根据指定的Options解析
 * 生成返回一个CommandLine
 */
public interface CommandLineParser
{
	/**
	 * 根据指定的options解析参数.
	 *
	 * @param options the specified Options
	 * @param arguments the command line arguments
	 * @return the list of atomic option and value tokens
	 *
	 * @throws ParseException 如果解析中遇到问题.
	 */
	CommandLine parse(Options options, String[] arguments) throws ParseException;


	/**
	 * Parse the arguments according to the specified options.
	 *
	 * @param options the specified Options
	 * @param arguments the command line arguments
	 * @param stopAtNonOption specifies whether to continue parsing the
	 * arguments if a non option is encountered.
	 *
	 * @return the list of atomic option and value tokens
	 * @throws ParseException if there are any problems encountered
	 * while parsing the command line tokens.
	 */
	CommandLine parse(Options options, String[] arguments, boolean stopAtNonOption) throws ParseException;

}

package pers.cy.iris.client.cli.exception;

import pers.cy.iris.client.cli.Option;

/**
 * @Author:cy
 * @Date:Created in 14:09 17/3/14
 * @Destription: 当一个选项需要参数但用户没有输入时抛出
 */
public class MissingArgumentException extends ParseException
{
	/** 需要参数的选项 */
	private Option option;


	public MissingArgumentException(String message)
	{
		super(message);
	}


	public MissingArgumentException(Option option)
	{
		this("Missing argument for option: " + option.getKey());
		this.option = option;
	}

	public Option getOption()
	{
		return option;
	}
}

package com.cy.iris.client.cli.exception;

/**
 * @Author:cy
 * @Date:Created in 16:39 17/3/13
 * @Destription: 解析命令行出错的异常基类
 */
public class ParseException extends Exception
{
	public ParseException(String message)
	{
		super(message);
	}
}
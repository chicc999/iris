package com.cy.iris.client.cli.exception;


/**
 * 无法识别的选项异常
 */
public class UnrecognizedOptionException extends ParseException
{
	/** 无法识别的选项 */
	private String option;


	public UnrecognizedOptionException(String message)
	{
		super(message);
	}


	public UnrecognizedOptionException(String message, String option)
	{
		this(message);
		this.option = option;
	}

	public String getOption()
	{
		return option;
	}
}

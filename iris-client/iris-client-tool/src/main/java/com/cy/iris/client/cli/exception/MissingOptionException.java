package com.cy.iris.client.cli.exception;



import java.util.Iterator;
import java.util.List;

/**
 * @Author:cy
 * @Date:Created in 14:09 17/3/14
 * @Destription: 当某个命令一定需要某个参数,参数缺少时抛出
 */
public class MissingOptionException extends ParseException
{
	/** The list of missing options */
	private List missingOptions;


	public MissingOptionException(String message)
	{
		super(message);
	}


	public MissingOptionException(List missingOptions)
	{
		this(createMessage(missingOptions));
		this.missingOptions = missingOptions;
	}

	public List getMissingOptions()
	{
		return missingOptions;
	}

	private static String createMessage(List missingOptions)
	{
		StringBuffer buff = new StringBuffer("Missing required option");
		buff.append(missingOptions.size() == 1 ? "" : "s");
		buff.append(": ");

		Iterator it = missingOptions.iterator();
		while (it.hasNext())
		{
			buff.append(it.next());
			if (it.hasNext())
			{
				buff.append(", ");
			}
		}

		return buff.toString();
	}
}

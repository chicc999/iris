package com.cy.iris.client.cli;

/**
 * @Author:cy
 * @Date:Created in 16:53 17/3/13
 * @Destription:
 */
/**
 * Contains useful helper methods for classes within this package.
 *
 * @author John Keyes (john at integralsource.com)
 * @version $Revision: 680644 $, $Date: 2008-07-29 01:13:48 -0700 (Tue, 29 Jul 2008) $
 */
public class Util
{
	/**
	 * Remove the hyphens from the begining of <code>str</code> and
	 * return the new String.
	 *
	 * @param str The string from which the hyphens should be removed.
	 *
	 * @return the new String.
	 */
	public static String stripLeadingHyphens(String str)
	{
		if (str == null)
		{
			return null;
		}
		if (str.startsWith("--"))
		{
			return str.substring(2, str.length());
		}
		else if (str.startsWith("-"))
		{
			return str.substring(1, str.length());
		}

		return str;
	}

	/**
	 * Remove the leading and trailing quotes from <code>str</code>.
	 * E.g. if str is '"one two"', then 'one two' is returned.
	 *
	 * @param str The string from which the leading and trailing quotes
	 * should be removed.
	 *
	 * @return The string without the leading and trailing quotes.
	 */
	public static String stripLeadingAndTrailingQuotes(String str)
	{
		if (str.startsWith("\""))
		{
			str = str.substring(1, str.length());
		}
		if (str.endsWith("\""))
		{
			str = str.substring(0, str.length() - 1);
		}
		return str;
	}
}

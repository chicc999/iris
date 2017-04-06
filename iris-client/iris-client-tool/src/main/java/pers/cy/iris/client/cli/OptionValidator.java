package pers.cy.iris.client.cli;

/**
 * @Author:cy
 * @Destription:验证选项缩写是否符合规范
 * @Date:Created in 15:23 17/3/13
 */
class OptionValidator
{
	/**
	 * 验证缩写是否合法.规则如下:
	 * 1.opt不为NULL
	 * 2.单独的字符,不为' ','?','@'或者字母
	 * 3.多个字符仅包含java中的可辨识字符
	 * @param opt 待验证的参数
	 * @throws IllegalArgumentException 参数不合法
	 */
	static void validateOption(String opt) throws IllegalArgumentException
	{
		if (opt == null)
		{
			return;
		}

		// 验证单个字符
		else if (opt.length() == 1)
		{
			char ch = opt.charAt(0);

			if (!isValidOpt(ch))
			{
				throw new IllegalArgumentException("illegal option value '" + ch + "'");
			}
		}

		// 验证多个字符
		else
		{
			char[] chars = opt.toCharArray();

			for (int i = 0; i < chars.length; i++)
			{
				if (!isValidChar(chars[i]))
				{
					throw new IllegalArgumentException("opt contains illegal character value '" + chars[i] + "'");
				}
			}
		}
	}

	private static boolean isValidOpt(char c)
	{
		return isValidChar(c) || c == ' ' || c == '?' || c == '@';
	}

	private static boolean isValidChar(char c)
	{
		return Character.isJavaIdentifierPart(c);
	}
}

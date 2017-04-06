package pers.cy.iris.commons.util;


import io.netty.util.internal.StringUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * 参数校验辅助类
 */
public class ArgumentUtil {
	/**
	 * 检测参数是否为null,如果为null则抛出空指针异常
	 * 否则返回参数
	 */
	public static <T> T isNotNull(String text, T arg) {
		if (arg == null) {
			throw new NullPointerException(text + " is null.");
		}
		return arg;
	}

	/**
	 * 检测若干参数是否为null,如果为null则抛出空指针异常
	 * 否则返回参数
	 */
	public static <T> void isNotNull(String[] text, T... arg) {
		if (text.length != arg.length) {
			throw new IllegalArgumentException("text[] 与 args 长度不等");
		}

		for (int i = 0; i < arg.length; i++)
			if (arg == null) {
				throw new NullPointerException(text[i] + "is null.");
			}
	}

	/**
	 * 检验参数是否为null或者空字符,如果是则抛出异常
	 *
	 * @param arg
	 */
	public static boolean isNotBlank(String arg) {
		if (StringUtils.isBlank(arg)) {
			throw new IllegalArgumentException(arg + "is Illegal argument.");
		}
		return true;
	}


}

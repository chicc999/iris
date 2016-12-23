package com.cy.iris.commons.util;

/**
 * 参数校验辅助类
 */
public class ArgumentUtil {
	/**
	 * 检测参数是否为null,如果为null则抛出空指针异常
	 * 否则返回参数
	 */
	public static <T> T isNotNull(T arg, String text) {
		if (arg == null) {
			throw new NullPointerException(text + "is null");
		}
		return arg;
	}
}

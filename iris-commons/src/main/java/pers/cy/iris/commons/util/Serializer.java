package pers.cy.iris.commons.util;

import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

/**
 * 序列化辅助工具
 */
public class Serializer {
	/**
	 * 写字符串
	 *
	 * @param value 字符串
	 * @param out   输出缓冲区
	 */
	public static void write(final String value, final ByteBuf out) {

		ArgumentUtil.isNotNull("out", out);

		byte[] bytes = value.getBytes(Charset.forName("UTF-8"));

		if (bytes.length > Integer.MAX_VALUE) {
			throw new IllegalArgumentException("too large String");
		}
		//value必须小于2G
		out.writeInt(bytes.length);

		out.writeBytes(bytes);

	}

	/**
	 * 读字符串
	 *
	 * @param in 输入缓冲区
	 */
	public static String read(final ByteBuf in) {

		ArgumentUtil.isNotNull("in", in);

		int len = in.readInt();

		if (len > Integer.MAX_VALUE || len < 0) {
			throw new IllegalArgumentException("illegal string length: " + len);
		}

		byte[] bytes = new byte[len];
		in.readBytes(bytes);
		String value = new String(bytes, Charset.forName("UTF-8"));

		return value;

	}
}

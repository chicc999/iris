package pers.cy.iris.commons.util;

import io.netty.buffer.ByteBuf;
import pers.cy.iris.commons.model.message.Message;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * 序列化辅助工具
 */
public class Serializer {

	/**
	 * 写长度小于等于Integer.MAX_VALUE的字符串
	 *
	 * @param value 字符串
	 * @param out   输出缓冲区
	 */
	public static void writeIntString(final String value, final ByteBuf out) {

		ArgumentUtil.isNotBlank(value);
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
	 * 读字符串,长度小于Integer.MAX_VALUE
	 *
	 * @param in 输入缓冲区
	 */
	public static String readIntString(final ByteBuf in) {

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

	/**
	 * 写字符串,小于Short.MAX_VALUE个字节
	 *
	 * @param value 字符串
	 * @param out   输出缓冲区
	 */
	public static void writeShortString(final String value, final ByteBuf out) {

		ArgumentUtil.isNotNull("value", value);
		ArgumentUtil.isNotNull("out", out);

		byte[] bytes = value.getBytes(Charset.forName("UTF-8"));

		if (bytes.length > Short.MAX_VALUE) {
			throw new IllegalArgumentException("too large String");
		}
		//value必须小于32767
		out.writeShort(bytes.length);

		out.writeBytes(bytes);

	}

	/**
	 * 读字符串,长度小于等于Short.MAX_VALUE
	 *
	 * @param in 输入缓冲区
	 */
	public static String readShortString(final ByteBuf in) {

		ArgumentUtil.isNotNull("in", in);

		int len = in.readUnsignedShort();

		if (len > Short.MAX_VALUE || len < 0) {
			throw new IllegalArgumentException("illegal string length: " + len);
		}

		byte[] bytes = new byte[len];
		in.readBytes(bytes);
		String value = new String(bytes, Charset.forName("UTF-8"));

		return value;

	}

	/**
	 * 写字符串,小于等于255个字节
	 *
	 * @param value 字符串
	 * @param out   输出缓冲区
	 */
	public static void writeByteString(final String value, final ByteBuf out) {

		ArgumentUtil.isNotNull("value", value);
		ArgumentUtil.isNotNull("out", out);

		byte[] bytes = value.getBytes(Charset.forName("UTF-8"));

		if (bytes.length > Byte.MAX_VALUE) {
			throw new IllegalArgumentException("too large String");
		}
		//value必须小于255
		out.writeByte(bytes.length);

		out.writeBytes(bytes);

	}

	/**
	 * 读字符串,小于等于255个字节
	 *
	 * @param in 输入缓冲区
	 */
	public static String readByteString(final ByteBuf in) {

		ArgumentUtil.isNotNull("in", in);

		int len = in.readUnsignedByte();

		if (len > Byte.MAX_VALUE || len < 0) {
			throw new IllegalArgumentException("illegal string length: " + len);
		}

		byte[] bytes = new byte[len];
		in.readBytes(bytes);
		String value = new String(bytes, Charset.forName("UTF-8"));

		return value;

	}



}

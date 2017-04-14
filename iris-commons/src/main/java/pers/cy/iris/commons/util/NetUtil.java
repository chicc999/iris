package pers.cy.iris.commons.util;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * 网络辅助类
 */
public class NetUtil {

	/**
	 * 从serverName获取对应的InetSocketAddress.
	 * serverName格式为xxx_xxx_xxx_xxx,如10_9_1_133
	 *
	 * @param serverName serverName格式的字符串
	 * @return inetSocketAddress
	 * @throws UnknownHostException 如果遇到无法转换的serverName
	 * @see java.net.InetSocketAddress
	 */
	public static InetSocketAddress serverNameToISA(String serverName) throws UnknownHostException {

		ArgumentUtil.isNotBlank(serverName);

		String[] parts = serverName.split("_");
		if (parts.length < 5) {
			throw new RuntimeException();
		}
		int port;
		try {
			port = Integer.parseInt(parts[parts.length - 1]);
		} catch (NumberFormatException e) {
			throw new UnknownHostException();
		}
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < parts.length - 1; i++) {
			if (i > 0) {
				builder.append('.');
			}
			builder.append(parts[i]);
		}
		String ip = builder.toString();

		InetSocketAddress isa = new InetSocketAddress(InetAddress.getByName(ip), port);

		return isa;
	}

	/**
	 * 把字节数组转化成16进制字符串
	 *
	 * @param address 字节数组
	 * @return 16进制
	 */
	public static String toHex(byte[] address) {
		StringBuilder builder = new StringBuilder();
		appendHex(address, builder);
		return builder.toString();
	}

	/**
	 * 把字节数组转化成16进制字符串
	 *
	 * @param address 字节数组
	 * @param builder 字符串构造器
	 */
	public static void appendHex(byte[] address, StringBuilder builder) {
		if (address == null || address.length < 4) {
			throw new IllegalArgumentException("serverName is invalid");
		}
		if (builder == null) {
			throw new IllegalArgumentException("builder is invalid");
		}
		String hex;
		int pos = 0;

		for (int i = 0; i < 4; i++) {
			hex = Integer.toHexString(address[pos++] & 0xFF).toUpperCase();
			if (hex.length() == 1) {
				builder.append('0').append(hex);
			} else {
				builder.append(hex);
			}
		}

	}

}

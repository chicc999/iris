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
	 * @param address serverName格式的字符串
	 * @return inetSocketAddress
	 * @throws UnknownHostException 如果遇到无法转换的serverName
	 * @see java.net.InetSocketAddress
	 */
	public static InetSocketAddress serverNameToISA(String address) throws UnknownHostException {

		ArgumentUtil.isNotBlank(address);

		String[] parts = address.split("_");
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

}

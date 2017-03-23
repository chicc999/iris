package com.cy.iris.commons.util.bootstrap;

import com.cy.iris.commons.network.netty.server.NettyServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

/**
 * @Author:cy
 * @Date:Created in 09:36 17/3/23
 * @Destription:
 */
public class PropertiesConfigureUtil {

	private static final Logger logger = LoggerFactory.getLogger(PropertiesConfigureUtil.class);

	public static void configureServerName(String[] args, ServerType serverType) {
		//设置协调器名字
		String ip = null;
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
			logger.info("本机ip为 {}", ip);
		} catch (UnknownHostException e) {
			logger.error("获取本机ip失败", e);
			ip = "unknown";
		}

		//端口优先通过参数传入,其次从配置文件读取
		int port;
		if (args.length >= 1) {
			try {
				port = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				logger.error("第一个参数必须是端口号且为整数", e);
				throw e;
			}
		} else {
			port = getPortByConfig(serverType.propertiesPath());
		}
		if (port <= 0 || port > 65535) {
			port = NettyServerConfig.DEFAULT_SERVICE_PORT;
		}
		System.setProperty(serverType.nameKey(), ip + ":" + port);
		System.setProperty(serverType.portKey(), String.valueOf(port));
	}

	/**
	 * 从配置文件获取端口
	 *
	 * @return 端口号
	 * @throws IOException
	 */
	public static int getPortByConfig(String filePath) {
		InputStream stream = null;
		int port = -1;
		Properties properties;
		// 加载应用路径配置文件
		try {
			stream = PropertiesConfigureUtil.class.getClassLoader().getResourceAsStream(filePath);
			if (stream == null) {
				throw new RuntimeException("Load configuration file " + filePath + " error.");
			}
			properties = new Properties();
			properties.load(stream);
			// 读取端口
			port = Integer.parseInt(properties.getProperty("netty.server.port"));
		} catch (NumberFormatException ignored) {
			// 端口没有配置
		} catch (IOException ignored) {
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					logger.error("流关闭出错", e);
				}
			}
		}
		return port;
	}

}





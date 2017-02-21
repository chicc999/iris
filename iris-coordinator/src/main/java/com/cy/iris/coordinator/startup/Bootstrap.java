package com.cy.iris.coordinator.startup;


import com.cy.iris.commons.network.netty.server.NettyServerConfig;
import com.cy.iris.coordinator.CoordinatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;


/**
 * 协调者启动器
 */
public class Bootstrap {
	private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);

	private static final String COORDINATOR_PROPERTOES = "coordinator.properties";

	public static final String COORDINATOR_NAME = "coordinator.name";

	public static final String COORDINATOR_PORT = "coordinator.port";

	public static void main(String[] args) {

		//设置协调器名称
		configureCoordinatorName(args);

		//加载spring配置,启动服务器
		ApplicationContext ctx;
		try {
			logger.info("load spring xml...");

			ctx = new ClassPathXmlApplicationContext("spring-*.xml");
		} catch (Exception e) {
			logger.error("load config error.", e);
			return;
		}

		final CoordinatorService coordinatorService = (CoordinatorService)ctx.getBean("coordinatorService");

		try {
			coordinatorService.start();
		} catch (Exception e) {
			logger.error("启动失败",e);
		}


		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				logger.info("system is being shutdown.");
				if (coordinatorService.isStarted()) {
					coordinatorService.stop();
				}
			}
		});
	}

	private static void configureCoordinatorName(String[] args){
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
		if(args.length >= 1){
			try {
				port = Integer.parseInt(args[0]);
			}catch (NumberFormatException e){
				logger.error("第一个参数必须是端口号且为整数",e);
				throw e;
			}
		}else{
			port = getPortByConfig();
		}
		if(port<=0 || port > 65535){
			port = NettyServerConfig.DEFAULT_SERVICE_PORT;
		}
		System.setProperty(COORDINATOR_NAME, ip+":"+port);
		System.setProperty(COORDINATOR_PORT,String.valueOf(port));
	}

	/**
	 * 从配置文件获取端口
	 *
	 * @return 端口号
	 * @throws IOException
	 */
	private static int getPortByConfig() {
		InputStream stream = null;
		int port = -1;
		Properties properties;
		// 加载应用路径配置文件
		try {
			stream = Bootstrap.class.getClassLoader().getResourceAsStream(COORDINATOR_PROPERTOES);
			if (stream == null) {
				throw new RuntimeException("Load configuration file "  + COORDINATOR_PROPERTOES + " error.");
			}
			properties = new Properties();
			properties.load(stream);
			// 读取端口
			port = Integer.parseInt(properties.getProperty("netty.server.port"));
		} catch (NumberFormatException ignored) {
			// 端口没有配置
		} catch (IOException ignored) {
		} finally {
			if(stream != null){
				try {
					stream.close();
				} catch (IOException e) {
					logger.error("流关闭出错",e);
				}
			}
		}
		return port;
	}
}

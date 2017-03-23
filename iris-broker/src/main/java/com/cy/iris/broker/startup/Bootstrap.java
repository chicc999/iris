package com.cy.iris.broker.startup;

import com.cy.iris.commons.network.netty.server.NettyServerConfig;
import com.cy.iris.commons.util.properties.PropertiesConfigureUtil;
import com.cy.iris.commons.util.properties.ServerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Author:cy
 * @Date:Created in 09:32 17/3/23
 * @Destription:
 */
public class Bootstrap {
	private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);

	public static void main(String[] args) {

		//设置broker名称
		PropertiesConfigureUtil.configureServerName(args, ServerType.Broker);

		//加载spring配置,启动服务器
		ApplicationContext ctx;
		try {
			logger.info("load spring xml...");

			ctx = new ClassPathXmlApplicationContext("spring-*.xml");
		} catch (Exception e) {
			logger.error("load config error.", e);
			return;
		}

//		final CoordinatorService coordinatorService = (CoordinatorService)ctx.getBean("coordinatorService");
//
//		try {
//			coordinatorService.start();
//		} catch (Exception e) {
//			logger.error("启动失败",e);
//		}
//
//
//		Runtime.getRuntime().addShutdownHook(new Thread() {
//			@Override
//			public void run() {
//				logger.info("system is being shutdown.");
//				if (coordinatorService.isStarted()) {
//					coordinatorService.stop();
//				}
//			}
//		});
	}


}

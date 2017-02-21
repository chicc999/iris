package com.cy.iris.coordinator.startup;


import com.cy.iris.coordinator.CoordinatorConfig;
import com.cy.iris.coordinator.CoordinatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 协调者启动器
 */
public class Bootstrap {
	private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);

	public static void main(String[] args) {

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
}

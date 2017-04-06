package pers.cy.iris.commons.util.bootstrap;

import pers.cy.iris.commons.service.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Author:cy
 * @Date:Created in  17/3/23
 * @Destription:
 */
public class BootstrapUtil {
	private static final Logger logger = LoggerFactory.getLogger(BootstrapUtil.class);

	public static Service loadClass(String serviceName){
		//加载spring配置,启动服务器
		ApplicationContext ctx = null;
		try {
			logger.info("load spring xml...");

			ctx = new ClassPathXmlApplicationContext("spring-*.xml");
		} catch (Exception e) {
			logger.error("load config error.", e);
			System.exit(-1);
		}

		final Service service = (Service) ctx.getBean(serviceName);
		return service;
	}

	public static void start(final Service service){
		try {
			service.start();
		} catch (Exception e) {
			logger.error("启动失败",e);
			System.exit(-1);
		}
	}


	public static void addShutdownHook(final Service service){
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				logger.info("system is being shutdown.");
				if (service.isStarted()) {
					service.stop();
				}
			}
		});
	}
}

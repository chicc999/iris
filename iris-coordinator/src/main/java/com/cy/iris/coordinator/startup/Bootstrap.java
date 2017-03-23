package com.cy.iris.coordinator.startup;

import com.cy.iris.commons.service.Service;
import com.cy.iris.commons.util.bootstrap.BootstrapUtil;
import com.cy.iris.commons.util.bootstrap.PropertiesConfigureUtil;
import com.cy.iris.commons.util.bootstrap.ServerType;
import com.cy.iris.coordinator.CoordinatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * 协调者启动器
 */
public class Bootstrap {
	private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);

	public static void main(String[] args) {

		//设置协调器名称
		PropertiesConfigureUtil.configureServerName(args, ServerType.Coordinator);

		//加载配置文件 & 初始化类
		Service coordinatorService = BootstrapUtil.loadClass("coordinatorService");

		//启动服务
		BootstrapUtil.start(coordinatorService);

		//注册虚拟机关闭时的钩子
		BootstrapUtil.addShutdownHook(coordinatorService);
	}

}

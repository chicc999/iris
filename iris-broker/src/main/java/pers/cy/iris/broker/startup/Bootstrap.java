package pers.cy.iris.broker.startup;

import pers.cy.iris.commons.service.Service;
import pers.cy.iris.commons.util.bootstrap.BootstrapUtil;
import pers.cy.iris.commons.util.bootstrap.PropertiesConfigureUtil;
import pers.cy.iris.commons.util.bootstrap.ServerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

		//加载配置文件 & 初始化类
		Service brokerService = BootstrapUtil.loadClass("brokerService");

		//启动服务
		BootstrapUtil.start(brokerService);

		//注册虚拟机关闭时的钩子
		BootstrapUtil.addShutdownHook(brokerService);
	}


}

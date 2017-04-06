package pers.cy.iris.broker;

import pers.cy.iris.broker.MetaManager.MetaManager;
import pers.cy.iris.commons.network.handler.DefaultHandlerFactory;
import pers.cy.iris.commons.network.netty.server.NettyServer;
import pers.cy.iris.commons.service.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author:cy
 * @Date:Created in 18:36 17/3/22
 * @Destription:
 */
public class BrokerService extends Service{

	private static final Logger logger = LoggerFactory.getLogger(BrokerService.class);

	private BrokerConfig brokerConfig;

	private MetaManager metaManager;

	private NettyServer nettyServer;

	@Override
	public void beforeStart() throws Exception {
		if(metaManager == null){
			metaManager = new MetaManager();
			metaManager.setMetaConfig(brokerConfig.getMetaConfig());
		}

		if(nettyServer == null) {
			nettyServer = new NettyServer(brokerConfig.getNettyServerConfig(),null,null,null, new DefaultHandlerFactory());
		}

	}

	@Override
	public void doStart() throws Exception {
		metaManager.start();
		nettyServer.start();
	}

	@Override
	public void afterStart() throws Exception {

	}

	@Override
	public void beforeStop() {

	}

	@Override
	public void doStop() {

	}

	public BrokerConfig getBrokerConfig() {
		return brokerConfig;
	}

	public void setBrokerConfig(BrokerConfig brokerConfig) {
		this.brokerConfig = brokerConfig;
	}
}

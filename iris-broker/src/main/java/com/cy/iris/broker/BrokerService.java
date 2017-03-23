package com.cy.iris.broker;

import com.cy.iris.broker.MetaManager.MetaManager;
import com.cy.iris.commons.service.Service;
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

	@Override
	public void beforeStart() throws Exception {
		if(metaManager == null){
			metaManager = new MetaManager();
			metaManager.setMetaConfig(brokerConfig.getMetaConfig());
		}

	}

	@Override
	public void doStart() throws Exception {
		metaManager.start();
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

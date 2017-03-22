package com.cy.iris.broker;

import com.cy.iris.commons.service.Service;

/**
 * @Author:cy
 * @Date:Created in 18:36 17/3/22
 * @Destription:
 */
public class BrokerService extends Service{

	private BrokerConfig brokerConfig;
	@Override
	public void beforeStart() throws Exception {

	}

	@Override
	public void doStart() throws Exception {

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

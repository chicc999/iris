package com.cy.iris.coordinator.cluster;

import com.cy.iris.commons.service.Service;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by cy on 17/2/16.
 */
public class ClusterManager extends Service{

	private static final Logger logger = LoggerFactory.getLogger(ClusterManager.class);

	private CuratorFramework zkClient ;

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


}

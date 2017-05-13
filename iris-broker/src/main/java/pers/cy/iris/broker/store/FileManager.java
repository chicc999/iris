package pers.cy.iris.broker.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.cy.iris.commons.service.Service;

/**
 * @Author:cy
 * @Date:Created in  17/5/13
 * @Destription:
 */
public class FileManager extends Service {

	private static Logger logger = LoggerFactory.getLogger(FileManager.class);

	// 存储配置
	private DiskFileStoreConfig storeConfig;

	public void setStoreConfig(DiskFileStoreConfig storeConfig) {
		this.storeConfig = storeConfig;
	}

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

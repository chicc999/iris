package pers.cy.iris.broker.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.cy.iris.commons.model.message.StoreMessage;
import pers.cy.iris.commons.service.Service;

/**
 * @Author:cy
 * @Date:Created in  17/4/17
 * @Destription: 硬盘文件存储系统
 */
public class DiskFileStore extends Service implements Store {

	private static Logger logger = LoggerFactory.getLogger(DiskFileStore.class);

	private DiskFileStoreConfig  storeConfig ;

	public DiskFileStore() {
	}

	public DiskFileStore(DiskFileStoreConfig storeConfig) {
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

	public DiskFileStoreConfig getStoreConfig() {
		return storeConfig;
	}

	public void setStoreConfig(DiskFileStoreConfig storeConfig) {
		this.storeConfig = storeConfig;
	}

	@Override
	public PutResult putMessage(StoreMessage message) {
		return null;
	}
}

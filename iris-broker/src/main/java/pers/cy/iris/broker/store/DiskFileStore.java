package pers.cy.iris.broker.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.cy.iris.commons.exception.IrisException;
import pers.cy.iris.commons.exception.QueueNotExistException;
import pers.cy.iris.commons.exception.ServiceNotAvailableException;
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

	private OffsetManager offsetManager;

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

	public void setOffsetManager(OffsetManager offsetManager) {
		this.offsetManager = offsetManager;
	}

	@Override
	public PutResult putMessage(StoreMessage message) throws ServiceNotAvailableException {
		logger.debug("put message {}",message.getMessageId());
		if (message == null) {
			return null;
		}
		if (!isStarted() ) {
			throw new ServiceNotAvailableException("DiskFileStore state: " + this.serviceState.get());
		}

		PutResult result = new PutResult();

		try{
			short queueId = getQueueId(message);
		}catch (Exception e){

		}

		return null;
	}

	/**
	 * 获取待存储的队列ID.
	 * @param message
	 * @return
	 */
	private short getQueueId(StoreMessage message) throws IrisException{
		short count = getQueueCount(message.getTopic());
		if (count <= 0) {
			// 消费队列不存在
			throw new QueueNotExistException("topic : " + message.getTopic());
		}
		return 0;
	}

	/**
	 * 获取topic在本broker的队列数量
	 * @param topic 主题名称
	 * @return queueId
	 * @throws QueueNotExistException
	 */
	private short getQueueCount(String topic)throws QueueNotExistException{
		return offsetManager.getQueueCount(topic);
	}
}

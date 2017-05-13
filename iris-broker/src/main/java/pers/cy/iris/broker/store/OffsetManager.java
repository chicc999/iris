package pers.cy.iris.broker.store;

import pers.cy.iris.commons.exception.QueueNotExistException;
import pers.cy.iris.commons.service.Service;

/**
 * @Author:cy
 * @Date:Created in  17/5/13
 * @Destription: 服务端偏移量管理
 */
public class OffsetManager extends Service{
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

	public short getQueueCount(String topic)throws QueueNotExistException {
		return 0;
	}
}

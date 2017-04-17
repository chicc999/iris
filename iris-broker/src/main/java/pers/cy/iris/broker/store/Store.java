package pers.cy.iris.broker.store;

import pers.cy.iris.commons.model.message.StoreMessage;
import pers.cy.iris.commons.service.LifeCycle;

/**
 * @Author:cy
 * @Date:Created in  17/4/17
 * @Destription: 存储接口
 */

public interface Store extends LifeCycle {

	/**
	 * 生产消息
	 *
	 * @param message 消息
	 */
	PutResult putMessage(StoreMessage message) ;


}
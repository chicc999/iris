package pers.cy.iris.broker.store;

import pers.cy.iris.commons.exception.ServiceNotAvailableException;
import pers.cy.iris.commons.model.message.StoreMessage;
import pers.cy.iris.commons.service.LifeCycle;

/**
 * @Author:cy
 * @Date:Created in  17/4/17
 * @Destription: 存储接口
 */

public interface Store extends LifeCycle {

	/**
	 * 返回队列最大偏移量
	 *
	 * @param topic   主题
	 * @param queueId 队列序号
	 * @return 队列最大偏移量
	 */
	long getQueueMaxOffset(String topic, short queueId);


	/**
	 * 返回队列最小偏移量
	 *
	 * 注意此时如果日志文件已经被删除，最小偏移量对应的消息实际内容可能已经被删除
	 *
	 * @param topic   主题
	 * @param queueId 队列序号
	 * @return 队列最小偏移量
	 */
	long getQueueMinOffset(String topic, short queueId);

	/**
	 * 返回日志最大偏移量
	 *
	 * @return 日志最大偏移量
	 */
	long getJournalMaxOffset();

	/**
	 * 返回日志最小偏移量
	 *
	 * @return 日志最小偏移量
	 */
	long getJournalMinOffset();




	/**
	 * 生产消息
	 *
	 * @param message 消息
	 */
	PutResult putMessage(StoreMessage message) throws ServiceNotAvailableException;


}
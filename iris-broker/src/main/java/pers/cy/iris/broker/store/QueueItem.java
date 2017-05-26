package pers.cy.iris.broker.store;

/**
 * @Author:cy
 * @Date:Created in  17/5/25
 * @Destription:
 */
public class QueueItem {
	//QueueItem固定长度
	public static final int CONSUMER_QUEUE_ITEM_SIZE = 8 + 4 + 2 + 8;
}

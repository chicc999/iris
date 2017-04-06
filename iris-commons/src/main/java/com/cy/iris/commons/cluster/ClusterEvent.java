package pers.cy.iris.commons.cluster;

/**
 * @Author:cy
 * @Date:Created in  17/3/29
 * @Destription: 集群事件
 */
public abstract class ClusterEvent {

	// 类型
	protected ClusterEventType type;

	public ClusterEventType getType() {
		return this.type;
	}

	public enum ClusterEventType {
		/**
		 * topic信息通知
		 */
		TOPIC_UPDATE,
		/**
		 * 重试服务变更
		 */
		RETRY_CHANGE,
		/**
		 * 所有topic全部更新
		 */
		ALL_TOPIC_UPDATE,
		/**
		 * 所有broker全部更新
		 */
		ALL_BROKER_UPDATE,
		/**
		 * 所有从消费全部更新
		 */
		ALL_SLAVECONSUME_UPDATE,
		/**
		 * 更新数据出现异常
		 */
		UPDATE_EXCEPTION
	}

}
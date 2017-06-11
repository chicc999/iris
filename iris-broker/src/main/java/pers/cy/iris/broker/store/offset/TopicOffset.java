package pers.cy.iris.broker.store.offset;

import pers.cy.iris.commons.util.SystemClock;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Author:cy
 * @Date:Created in  17/5/25
 * @Destription: 主题偏移量管理
 */
public class TopicOffset {
	// 主题
	private transient String topic;
	// 队列偏移量
	private ConcurrentMap<Short/*queue id*/, QueueOffset> offsets = new ConcurrentHashMap<Short, QueueOffset>();
	// 消费者订阅时间
	private ConcurrentMap<String/*consumerName*/, Long/*add consumer time*/> consumers = new ConcurrentHashMap<String, Long>();

	public TopicOffset() {
	}

	public TopicOffset(String topic) {
		this.topic = topic;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public ConcurrentMap<Short, QueueOffset> getOffsets() {
		return offsets;
	}

	public void setOffsets(ConcurrentMap<Short, QueueOffset> offsets) {
		this.offsets = offsets;
	}

	/**
	 * 获取队列偏移量
	 *
	 * @param queueId
	 */
	public QueueOffset getOffset(final short queueId) {
		return offsets.get(queueId);
	}

	/**
	 * 获取队列偏移量，不存在则创建
	 *
	 * @param queueId 队列ID
	 * @return 队列偏移量
	 */
	public QueueOffset getAndCreateOffset(final short queueId) {
		QueueOffset offset = offsets.get(queueId);
		if (offset == null) {
			offset = new QueueOffset(topic, queueId);
			QueueOffset old = offsets.putIfAbsent(queueId, offset);
			if (old != null) {
				offset = old;
			}
		}
		return offset;
	}

	/**
	 * 应答位置
	 *
	 * @param queueId  队列ID
	 * @param consumer 消费者
	 * @param offset   位置
	 */
	public void acknowledge(final short queueId, final String consumer, final long offset) {
		getAndCreateOffset(queueId).acknowledge(consumer, offset);
	}

	/**
	 * 重置应答位置
	 *
	 * @param queueId  队列ID
	 * @param consumer 消费者
	 * @param offset   位置
	 */
	public void resetAckOffset(final short queueId, final String consumer, final long offset) {
		getAndCreateOffset(queueId).resetAckOffset(consumer, offset);
	}

	/**
	 * 是否有订阅者
	 *
	 * @return
	 */
	public boolean hasConsumer() {
		for (QueueOffset queueOffset : offsets.values()) {
			if (!queueOffset.getOffsets().isEmpty()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 更新偏移量
	 *
	 * @param topicOffset 偏移量
	 */
	public void updateOffset(final TopicOffset topicOffset) {
		if (topicOffset == null) {
			return;
		}
		for (Map.Entry<String, Long> entry : topicOffset.consumers.entrySet()) {
			this.consumers.putIfAbsent(entry.getKey(), entry.getValue());
		}

		for (Map.Entry<Short, QueueOffset> entry : topicOffset.offsets.entrySet()) {
			QueueOffset src = entry.getValue();
			QueueOffset target = getAndCreateOffset(entry.getKey());
			target.updateOffset(src);
		}
	}

	public void addConsumer(String consumer) {
		consumers.putIfAbsent(consumer, SystemClock.currentTimeMillis());
	}

	public void removeConsumer(String consumer) {
		consumers.remove(consumer);
	}

	public ConcurrentMap<String, Long> getConsumers() {
		return consumers;
	}

	public void setConsumers(ConcurrentMap<String, Long> consumers) {
		this.consumers = consumers;
	}
}

package com.cy.iris.commons.model;

import com.cy.iris.commons.cluster.ClusterRole;
import com.cy.iris.commons.util.retry.RetryPolicy;

import java.io.Serializable;
import java.util.*;

/**
 * @Author:cy
 * @Date:Created in  17/3/24
 * @Destription: 主题配置
 */
public class TopicConfig implements Serializable{
	private static final long serialVersionUID = 5674581453999776697L;

	public final static int TopicGeneral = 2;

	public static final char APP_NAME_SPLITOR = '.';

	// 主题
	private String topic;
	// 重要性 (0=非常重要，1=重要，2=一般), null=一般
	private Integer importance;
	// 分组
	private Set<String> groups = new HashSet<String>();
	// 生产者信息
	private Map<String, ProducerPolicy> producers = new HashMap<String, ProducerPolicy>();
	// 消费者信息
	private Map<String, ConsumerPolicy> consumers = new HashMap<String, ConsumerPolicy>();
	// 是否需要归档。默认不需要归档
	private boolean archive = false;
	// 需要队列的大小
	private short queues;
	// 类型
	private TopicType type;

	public TopicConfig() {
	}

	public TopicConfig(String topic, short queues, boolean archive) {
		this.topic = topic;
		this.queues = queues;
		this.archive = archive;
	}

	/**
	 * 获取配置的app名
	 *
	 * @param srcApp 原始APP
	 * @return 配置的APP
	 */
	public static String getApp(final String srcApp) {
		if (srcApp != null) {
			int pos = srcApp.lastIndexOf(APP_NAME_SPLITOR);
			if (pos > 0) {
				return srcApp.substring(0, pos);
			}
		}
		return srcApp;
	}

	/**
	 * 主题配置转换
	 *
	 * @param topic 主题
	 * @return 主题配置
	 */
	public static TopicConfig toTopicConfig(Topic topic) {
		if (topic == null) {
			return null;
		}
		TopicConfig config = new TopicConfig();
		config.setTopic(topic.getCode());
		config.setArchive(topic.isArchive());
		config.setQueues((short) topic.getQueues());
		config.setType(topic.getType());
		config.setImportance(topic.getImportance());
		return config;
	}

	/**
	 * 根据数据库配置转换成消费策略
	 *
	 * @param topic    主题
	 * @param consumer 消费配置
	 * @return 消费策略
	 */
	public static ConsumerPolicy toPolicy(Topic topic, Consumer consumer) {
		if (consumer == null) {
			return null;
		}
		ConsumerPolicy consumerPolicy = new ConsumerPolicy();
		consumerPolicy.setAckTimeout(consumer.getAckTimeout() <= 0 ? null : consumer.getAckTimeout());
		consumerPolicy.setArchive(topic.isArchive() && !consumer.isArchive() ? false : null);
		consumerPolicy.setBatchSize(consumer.getBatchSize() <= 0 ? null : (short) consumer.getBatchSize());
		consumerPolicy.setNearby(consumer.isNearby() ? true : null);
		consumerPolicy.setPaused(consumer.isPaused() ? true : null);
		consumerPolicy.setRetry(consumer.isRetry() ? null : false);
		consumerPolicy.setSeq(topic.getType() == TopicType.SEQUENTIAL ? true : null);
		consumerPolicy.setRole(ClusterRole.MASTER.equals(consumer.getRole()) ? null : consumer.getRole());
		consumerPolicy.setOffsetMode(OffsetMode.REMOTE.equals(consumer.getOffsetMode()) ? null : consumer.getOffsetMode());
		consumerPolicy.setSelector(consumer.getSelector());
		consumerPolicy.setConcurrentConsume(consumer.isConcurrentConsume() ? true : null);
		consumerPolicy.setDelay(consumer.getDelay() > 0 ? consumer.getDelay() : null);
		consumerPolicy.setPrefetchSize(consumer.getPrefetchSize() > 0 ? consumer.getPrefetchSize() : null);
		RetryPolicy retryPolicy = new RetryPolicy();
		retryPolicy.setRetryDelay(consumer.getRetryDelay() <= 0 ? null : consumer.getRetryDelay());
		retryPolicy.setMaxRetryDelay(consumer.getMaxRetryDelay() <= 0 ? null : consumer.getMaxRetryDelay());
		retryPolicy.setMaxRetrys(consumer.getMaxRetrys() <= 0 ? null : consumer.getMaxRetrys());
		retryPolicy.setExpireTime(consumer.getExpireTime() <= 0 ? null : consumer.getExpireTime());
		retryPolicy.setUseExponentialBackOff(consumer.isUseExponentialBackOff() ? null : false);
		retryPolicy.setBackOffMultiplier(consumer.getBackOffMultiplier() <= 0 ? null : consumer.getBackOffMultiplier());
		if (retryPolicy.getRetryDelay() != null || retryPolicy.getMaxRetryDelay() != null || retryPolicy
				.getMaxRetrys() != null || retryPolicy.getUseExponentialBackOff() != null || retryPolicy
				.getBackOffMultiplier() != null || retryPolicy.getExpireTime() != null) {
			consumerPolicy.setRetryPolicy(retryPolicy);
		}
		return consumerPolicy;
	}

	/**
	 * 根据数据库配置转换成生产策略
	 *
	 * @param topic    主题
	 * @param producer 生产配置
	 * @return 生产策略
	 */
	public static ProducerPolicy toPolicy(Topic topic, Producer producer) {
		if (producer == null) {
			return null;
		}
		ProducerPolicy policy = new ProducerPolicy();
		policy.setSeq(topic.getType() == TopicType.SEQUENTIAL ? true : null);
		policy.setNearby(producer.isNearby() ? true : null);
		policy.setSingle(producer.isSingle() ? true : null);
		policy.setWeight(producer.weights());
		return policy;
	}

	/**
	 * 转换为主题配置
	 *
	 * @param topics    主题
	 * @param producers 生产策略
	 * @param consumers 消费策略
	 * @param groups    分组
	 * @return 主题配置列表
	 * @throws Exception
	 */
	public static List<TopicConfig> toTopicConfigs(List<Topic> topics, List<Producer> producers,
												   List<Consumer> consumers, List<TopicGroup> groups) throws Exception {
		if (topics == null || topics.isEmpty()) {
			return new ArrayList<TopicConfig>();
		}
		Map<Long, TopicConfig> configMap = new HashMap<Long, TopicConfig>();
		Map<Long, Topic> topicMap = new HashMap<Long, Topic>();
		for (Topic topic : topics) {
			topicMap.put(topic.getId(), topic);
			configMap.put(topic.getId(), toTopicConfig(topic));
		}

		TopicConfig config;
		Topic topic;
		for (Producer producer : producers) {
			config = configMap.get(producer.getTopicId());
			topic = topicMap.get(producer.getTopicId());
			if (config != null) {
				config.addProducerPolicy(producer.getApp(), toPolicy(topic, producer));
			}
		}
		for (Consumer consumer : consumers) {
			config = configMap.get(consumer.getTopicId());
			topic = topicMap.get(consumer.getTopicId());
			if (config != null) {
				config.addConsumerPolicy(consumer.getApp(), toPolicy(topic, consumer));
			}
		}
		for (TopicGroup group : groups) {
			config = configMap.get(group.getTopicId());
			if (config != null) {
				config.getGroups().add(group.getGroup());
			}
		}
		return new ArrayList<TopicConfig>(configMap.values());
	}

	public short getQueues() {
		return queues;
	}

	public void setQueues(short queues) {
		this.queues = queues;
	}

	public boolean isArchive() {
		return archive;
	}

	public void setArchive(boolean archive) {
		this.archive = archive;
	}

	public boolean checkSequential() {
		return TopicType.SEQUENTIAL == type;
	}

	//policy 不为空的前提下默认允许多个发送者
	public boolean singleProducer(String app) {
		ProducerPolicy policy = producers.get(app);
		return policy == null ? false : (policy.getSingle() == null ? false : policy.getSingle());
	}

	public boolean isBroadcastTopic() {
		return TopicType.BROADCAST == type;
	}

	public boolean isLocalManageOffsetConsumer(String consumer) {
		//TODO 通过消费策略可以设置
		if (isBroadcastTopic() || isConsumerByLocalOffset(consumer)) {
			return true;
		}
		return false;
	}

	public boolean isConsumerByLocalOffset(String consumer) {
		if (consumers == null || consumers.isEmpty()) {
			return false;
		}
		ConsumerPolicy consumerPolicy;
		consumerPolicy = consumers.get(consumer);
		if (consumerPolicy == null) {
			return false;
		}
		if (consumerPolicy.getOffsetMode() != null && consumerPolicy.getOffsetMode() == OffsetMode.LOCAL) {
			return true;
		} else {
			return false;
		}
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public int getImportance() {
		return (importance == null) ? TopicGeneral : importance;
	}

	public void setImportance(Integer importance) {
		this.importance = importance;
	}

	public TopicType getType() {
		if (type == null) {
			return TopicType.TOPIC;
		}
		return type;
	}

	public void setType(TopicType type) {
		this.type = type;
	}

	public Map<String, ProducerPolicy> getProducers() {
		return producers;
	}

	public void setProducers(Map<String, ProducerPolicy> producers) {
		this.producers = producers;
	}

	public ProducerPolicy getProducerPolicy(String app) {
		return producers.get(app);
	}

	public ConsumerPolicy getConsumerPolicy(String app) {
		return consumers.get(app);
	}

	public Map<String, ConsumerPolicy> getConsumers() {
		return consumers;
	}

	public void setConsumers(Map<String, ConsumerPolicy> consumers) {
		this.consumers = consumers;
	}

	public Set<String> getGroups() {
		return groups;
	}

	public void setGroups(Set<String> groups) {
		this.groups = groups;
	}

	public void addProducerPolicy(String app, ProducerPolicy policy) {
		if (app == null || app.isEmpty()) {
			return;
		}
		producers.put(app, policy);
	}

	public void addConsumerPolicy(String app, ConsumerPolicy policy) {
		if (app == null || app.isEmpty()) {
			return;
		}
		consumers.put(app, policy);
	}

	public boolean isProducer(final String app) {
		return producers.containsKey(getApp(app));
	}

	public boolean isConsumer(final String app) {
		return consumers.containsKey(getApp(app));
	}

	public boolean containGroup(final String group) {
		return groups.contains(group);
	}

	/**
	 * 是否可以生产消息
	 *
	 * @param producer 生产者系统
	 * @return 可以生产消息标示
	 */
	public boolean isWritable(final String producer) {
		return producers.containsKey(getApp(producer)) && !groups.isEmpty();
	}

	/**
	 * 是否可以消费消息
	 *
	 * @param consumer 消费者系统
	 * @return 可以消费消息标示
	 */
	public boolean isReadable(final String consumer) {
		ConsumerPolicy consumerPolicy = consumers.get(getApp(consumer));
		return consumerPolicy != null && !consumerPolicy.isPaused() && !groups.isEmpty();
	}

	/**
	 * 是否开启归档
	 *
	 * @param consumer 消费者
	 * @return 归档标示
	 */
	public boolean isArchive(final String consumer) {
		ConsumerPolicy consumerPolicy = consumers.get(getApp(consumer));
		if (consumerPolicy != null) {
			return isArchive() && (consumerPolicy.getArchive() == null || consumerPolicy.getArchive());
		} else {
			return isArchive();
		}
	}
}
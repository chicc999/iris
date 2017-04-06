package pers.cy.iris.commons.model;

import pers.cy.iris.commons.cluster.ClusterRole;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @Author:cy
 * @Date:Created in  17/3/24
 * @Destription: 消费者
 */
public class Consumer extends BaseModel {
	// 主题ID
	private long topicId;
	// 主题
	private String topic;
	// 应用代码
	private String app;
	// 是否就近发送
	private boolean nearby;
	// 是否暂停消费
	private boolean paused;
	// 是否需要归档,默认不归档
	private boolean archive;
	// 是否启用重试服务
	private boolean retry = true;
	// 延迟时间
	@Max(3600000)
	private int delay;
	// 过滤器
	private String selector;
	// 消费节点角色
	private ClusterRole role = ClusterRole.MASTER;
	// 偏移量管理类型
	private OffsetMode offsetMode = OffsetMode.REMOTE;
	// 应答超时时间
	@Min(0)
	private int ackTimeout;
	// 批量大小
	@Min(0)
	@Max(127)
	private int batchSize;
	// 最大重试次数(无限制)
	@Min(0)
	private int maxRetrys;
	// 最大重试间隔(默认5分钟)
	@Min(0)
	private int maxRetryDelay;
	// 重试间隔
	@Min(0)
	private int retryDelay;
	// 指数增加间隔时间
	private boolean useExponentialBackOff = true;
	// 指数系数
	@Min(0)
	private double backOffMultiplier;
	// 过期时间（默认3天）
	@Min(0)
	private int expireTime;
	//单个queue并行消费
	private boolean concurrentConsume = false;
	//并行消费单个queue最大并行消息数
	private int prefetchSize;

	public long getTopicId() {
		return topicId;
	}

	public void setTopicId(long topicId) {
		this.topicId = topicId;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getApp() {
		return app;
	}

	public void setApp(String app) {
		this.app = app;
	}

	public boolean isNearby() {
		return nearby;
	}

	public void setNearby(boolean nearby) {
		this.nearby = nearby;
	}

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	public boolean isArchive() {
		return archive;
	}

	public void setArchive(boolean archive) {
		this.archive = archive;
	}

	public boolean isRetry() {
		return retry;
	}

	public void setRetry(boolean retry) {
		this.retry = retry;
	}

	public String getSelector() {
		return selector;
	}

	public void setSelector(String selector) {
		this.selector = selector;
	}

	public ClusterRole getRole() {
		return role;
	}

	public void setRole(ClusterRole role) {
		this.role = role;
	}

	public void setOffsetMode(OffsetMode offsetMode) {
		this.offsetMode = offsetMode;
	}

	public OffsetMode getOffsetMode() {
		return offsetMode;
	}

	public int getAckTimeout() {
		return ackTimeout;
	}

	public void setAckTimeout(int ackTimeout) {
		this.ackTimeout = ackTimeout;
	}

	public int getBatchSize() {
		return batchSize;
	}

	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}

	public int getMaxRetrys() {
		return maxRetrys;
	}

	public void setMaxRetrys(int maxRetrys) {
		this.maxRetrys = maxRetrys;
	}

	public int getMaxRetryDelay() {
		return maxRetryDelay;
	}

	public void setMaxRetryDelay(int maxRetryDelay) {
		this.maxRetryDelay = maxRetryDelay;
	}

	public int getRetryDelay() {
		return retryDelay;
	}

	public void setRetryDelay(int retryDelay) {
		this.retryDelay = retryDelay;
	}

	public boolean isUseExponentialBackOff() {
		return useExponentialBackOff;
	}

	public void setUseExponentialBackOff(boolean useExponentialBackOff) {
		this.useExponentialBackOff = useExponentialBackOff;
	}

	public double getBackOffMultiplier() {
		return backOffMultiplier;
	}

	public void setBackOffMultiplier(double backOffMultiplier) {
		this.backOffMultiplier = backOffMultiplier;
	}

	public int getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(int expireTime) {
		this.expireTime = expireTime;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public boolean isConcurrentConsume() {
		return concurrentConsume;
	}

	public void setConcurrentConsume(boolean concurrentConsume) {
		this.concurrentConsume = concurrentConsume;
	}

	public int getPrefetchSize() {
		return prefetchSize;
	}

	public void setPrefetchSize(int prefetchSize) {
		this.prefetchSize = prefetchSize;
	}
}

package pers.cy.iris.commons.model;

import pers.cy.iris.commons.cluster.ClusterRole;
import pers.cy.iris.commons.util.retry.RetryPolicy;

import java.io.Serializable;

/**
 * @Author:cy
 * @Date:Created in  17/3/24
 * @Destription: 消费策略
 */
public class ConsumerPolicy implements Serializable {
	private static final long serialVersionUID = -2136366501297870464L;
	// 应用
	private transient String app;
	// 就近发送
	private Boolean nearby;
	// 是否暂停消费
	private Boolean paused;
	// 是否需要归档,默认归档
	private Boolean archive;
	// 是否需要重试，默认重试
	private Boolean retry;
	// 顺序消费
	private Boolean seq;
	// 过滤器
	private String selector;
	// 消费节点角色 null表示master
	private ClusterRole role;
	// 消息偏移量管理 null 表示remote
	private OffsetMode offsetMode;
	// 重试策略
	private RetryPolicy retryPolicy;
	// 应答超时时间
	private Integer ackTimeout;
	// 批量大小
	private Short batchSize;
	//预取大小
	private Integer prefetchSize;
	//并行消费
	private Boolean concurrentConsume;
	//延迟消费
	private Integer delay;

	public ConsumerPolicy() {
	}

	public ConsumerPolicy(String app) {
		this.app = app;
	}

	public String getApp() {
		return app;
	}

	public boolean isNearby() {
		return nearby != null && nearby;
	}

	public Boolean getNearby() {
		return nearby;
	}

	public void setNearby(Boolean nearby) {
		this.nearby = nearby;
	}

	public boolean isPaused() {
		return paused != null && paused;
	}

	public Boolean getPaused() {
		return paused;
	}

	public void setPaused(Boolean paused) {
		this.paused = paused;
	}

	public Boolean getArchive() {
		return archive;
	}

	public void setArchive(Boolean archive) {
		this.archive = archive;
	}

	public boolean isRetry() {
		return retry == null || retry;
	}

	public Boolean getRetry() {
		return retry;
	}

	public void setRetry(Boolean retry) {
		this.retry = retry;
	}


	public boolean checkSequential() {
		return seq != null && seq;
	}

	public Boolean getSeq() {
		return seq;
	}

	public void setSeq(Boolean seq) {
		this.seq = seq;
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

	public OffsetMode getOffsetMode() {
		return offsetMode;
	}

	public void setOffsetMode(OffsetMode offsetMode) {
		this.offsetMode = offsetMode;
	}

	public RetryPolicy getRetryPolicy() {
		return retryPolicy;
	}

	public void setRetryPolicy(RetryPolicy retryPolicy) {
		this.retryPolicy = retryPolicy;
	}

	public Integer getAckTimeout() {
		return ackTimeout;
	}

	public void setAckTimeout(Integer ackTimeout) {
		this.ackTimeout = ackTimeout;
	}

	public Short getBatchSize() {
		return batchSize;
	}

	public void setBatchSize(Short batchSize) {
		this.batchSize = batchSize;
	}

	public Integer getPrefetchSize() {
		return prefetchSize;
	}

	public void setPrefetchSize(Integer prefetchSize) {
		this.prefetchSize = prefetchSize;
	}

	public Boolean getConcurrentConsume() {
		return concurrentConsume;
	}

	public void setConcurrentConsume(Boolean concurrentConsume) {
		this.concurrentConsume = concurrentConsume;
	}

	public Integer getDelay() {
		return delay;
	}

	public void setDelay(Integer delay) {
		this.delay = delay;
	}
}


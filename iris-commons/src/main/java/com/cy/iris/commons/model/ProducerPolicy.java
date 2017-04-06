package pers.cy.iris.commons.model;

import java.io.Serializable;
import java.util.Map;

/**
 * @Author:cy
 * @Date:Created in  17/3/24
 * @Destription: 生产者策略
 */
public class ProducerPolicy implements Serializable {

	private static final long serialVersionUID = 8851774341411309121L;
	// 应用
	private transient String app;
	// 就近发送
	private Boolean nearby;
	// 是否顺序
	private Boolean seq;
	// 是否允许多个发送者
	private Boolean single;
	// 生产者权重 <group,weight>
	private Map<String, Short> weight;

	public ProducerPolicy() {
	}

	public ProducerPolicy(String app) {
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

	public Map<String, Short> getWeight() {
		return weight;
	}

	public void setWeight(Map<String, Short> weight) {
		this.weight = weight;
	}

	public void setApp(String app) {
		this.app = app;
	}

	public Boolean getSeq() {
		return seq;
	}

	public void setSeq(Boolean seq) {
		this.seq = seq;
	}

	public Boolean getSingle() {
		return single;
	}

	public void setSingle(Boolean single) {
		this.single = single;
	}
}

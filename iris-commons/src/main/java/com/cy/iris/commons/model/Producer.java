package com.cy.iris.commons.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author:cy
 * @Date:Created in  17/3/24
 * @Destription: 生产者
 */
public class Producer extends BaseModel {

	// 主题ID
	private long topicId;
	// 主题
	private String topic;
	// 应用代码
	private String app;
	// 是否就近发送
	private boolean nearby;
	//集群实例发送权重
	private String weight;
	//单个发送者
	private boolean single = false;

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

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public boolean isSingle() {
		return single;
	}

	public void setSingle(boolean single) {
		this.single = single;
	}

	/**
	 * 获取权重
	 *
	 * @return 权重
	 */
	public Map<String, Short> weights() {
		if (weight == null || weight.isEmpty()) {
			return null;
		}
		Map<String, Short> map = new HashMap<String, Short>();
		String[] values = weight.split(",");
		String[] parts;
		for (String value : values) {
			parts = value.split(":");
			if (parts.length >= 2) {
				try {
					map.put(parts[0], Short.parseShort(parts[1]));
				} catch (NumberFormatException e) {
				}
			}
		}

		return map;
	}
}

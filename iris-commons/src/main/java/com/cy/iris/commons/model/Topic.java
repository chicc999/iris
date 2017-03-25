package com.cy.iris.commons.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @Author:cy
 * @Date:Created in  17/3/24
 * @Destription: 主题
 */
public class Topic extends BaseModel {
	public static final int DEFAULT_QUEUE_SIZE = 5;

	/**
	 * 消息类型名称
	 */
	@NotNull
	private String name;
	/**
	 * 消息机房分布
	 */
	private String distribution = null;
	/**
	 * 消息生产者部门
	 */
	private String department = null;
	/**
	 * 消息类型代码
	 */
	@Pattern(regexp = "^[a-zA-Z0-9]+[a-zA-Z0-9_]*[a-zA-Z0-9]+$", message = "Please enter correct code")
	private String code;
	/**
	 * 队列重要性
	 */
	@Min(0)
	@Max(2)
	private int importance = 2;
	/**
	 * 队列数
	 */
	@Min(0)
	@Max(99)
	private int queues = DEFAULT_QUEUE_SIZE;
	/**
	 * 是否归档
	 */
	private boolean archive = false;
	/**
	 * 消息类型描述
	 */
	private String description;
	/**
	 * 类型 0:topic,1:broadcast,2:sequential
	 */
	private TopicType type;

	public Topic() {
		type = TopicType.TOPIC;
	}

	public Topic(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDistribution() { return distribution; }

	public void setDistribution(String distribution) { this.distribution = distribution; }

	public String getDepartment() { return department; }

	public void setDepartment(String department) { this.department = department; }

	public int getImportance() { return importance; }

	public void setImportance(int importance) { this.importance = importance; }

	public int getQueues() {
		return queues;
	}

	public void setQueues(int queues) {
		this.queues = queues;
	}

	public boolean isArchive() {
		return archive;
	}

	public void setArchive(boolean archive) {
		this.archive = archive;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public TopicType getType() {
		return type;
	}

	public void setType(TopicType type) {
		this.type = type;
	}
}
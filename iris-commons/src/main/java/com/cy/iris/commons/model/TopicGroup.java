package pers.cy.iris.commons.model;

/**
 * @Author:cy
 * @Date:Created in  17/3/24
 * @Destription:
 */
public class TopicGroup extends BaseModel {
	// 主题ID
	private long topicId;
	// 分组
	private String group;

	public long getTopicId() {
		return topicId;
	}

	public void setTopicId(long topicId) {
		this.topicId = topicId;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}
}

package com.cy.iris.commons.model;

import java.util.Date;

/**
 * @Author:cy
 * @Date:Created in  17/3/24
 * @Destription: 所有可以被存进数据库的模型基类
 */
public abstract class BaseModel {

	public static final int ENABLED = 1;

	public static final int DISABLED = 0;

	public static final int DELETED = -1;

	/**
	 * 主键
	 */
	protected long id;
	/**
	 * 创建时间
	 */
	protected Date createTime;
	/**
	 * 创建人
	 */
	protected long createBy;
	/**
	 * 创建人代码
	 */
	protected String createUser;

	/**
	 * 修改时间
	 */
	protected Date updateTime;
	/**
	 * 修改人
	 */
	protected long updateBy;
	/**
	 * 修改人代码
	 */
	protected String updateUser;
	/**
	 * 状态
	 */
	protected int status;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(long createBy) {
		this.createBy = createBy;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public long getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(long updateBy) {
		this.updateBy = updateBy;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
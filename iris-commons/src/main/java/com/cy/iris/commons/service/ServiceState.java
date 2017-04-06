package pers.cy.iris.commons.service;

/**
 * 服务器状态
 */
public enum ServiceState {
	/**
	 * 准备启动
	 */
	WILL_START("WILL_START"),
	/**
	 * 启动中
	 */
	STARTING("STARTING"),
	/**
	 * 启动失败
	 */
	START_FAILED("START_FAILED"),
	/**
	 * 启动完成
	 */
	STARTED("STARTED"),
	/**
	 * 准备关闭
	 */
	WILL_STOP("WILL_STOP"),
	/**
	 * 关闭中
	 */
	STOPPING("STOPPING"),
	/**
	 * 关闭失败
	 */
	STOP_FAILED("STOP_FAILED"),
	/**
	 * 关闭完成
	 */
	STOPPED("STOPPED");

	private String description;
	ServiceState(String description){
		this.description = description;
	}

	public String toString(){
		return this.description;
	}
}

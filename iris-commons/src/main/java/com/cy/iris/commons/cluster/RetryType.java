package pers.cy.iris.commons.cluster;

/**
 * @Author:cy
 * @Date:Created in  17/3/30
 * @Destription: 重试服务类型
 */
public enum RetryType {
	/**
	 * 直连数据库
	 */
	DB,
	/**
	 * 访问远程服务
	 */
	REMOTE
}
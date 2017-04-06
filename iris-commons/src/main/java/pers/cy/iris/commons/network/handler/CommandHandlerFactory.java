package pers.cy.iris.commons.network.handler;


/**
 * 命令处理器工厂类
 */
public interface CommandHandlerFactory {

	/**
	 * 获取处理器
	 *
	 * @param type 类型
	 */
	CommandHandler getHandler(int type);

}
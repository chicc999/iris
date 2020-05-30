package pers.cy.iris.coordinator.raft.core;

/**
 * @Author:cy
 * @Date:Created in  2020/5/30
 * @Destription: 状态机接口
 */
public interface StateMachine<T> {
	/**
	 * 将日志应用到状态机接口
	 * @param logEntry
	 */
	void apply(LogEntry<T> logEntry);
}

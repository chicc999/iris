package pers.cy.iris.coordinator.raft.core;

/**
 * @Author:cy
 * @Date:Created in  2020/5/10
 * @Destription:
 */
public class LogEntry<T> {

	private long index;

	private long term;

	private T entry;
}

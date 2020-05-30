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

	public long getIndex() {
		return index;
	}

	public void setIndex(long index) {
		this.index = index;
	}

	public long getTerm() {
		return term;
	}

	public void setTerm(long term) {
		this.term = term;
	}

	public T getEntry() {
		return entry;
	}

	public void setEntry(T entry) {
		this.entry = entry;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof LogEntry)) return false;

		LogEntry<?> logEntry = (LogEntry<?>) o;

		if (getIndex() != logEntry.getIndex()) return false;
		return getTerm() == logEntry.getTerm();
	}

}

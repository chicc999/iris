package pers.cy.iris.broker.store.sequence;

import pers.cy.iris.broker.store.QueueItem;

/**
 * @Author:cy
 * @Date:Created in  17/5/26
 * @Destription:
 */
public class Sequence extends LinkedNode<Sequence> {

	public static final String DEVIDE = "-";
	long first;
	long last;
	int blockSize = QueueItem.CONSUMER_QUEUE_ITEM_SIZE;

	public Sequence(long value) {
		first = last = value;
	}

	public Sequence(long first, long last) {
		if (first > last) {
			throw new RuntimeException("Construct Sequence error,first:" + first + " ,last:" + last);
		}

		this.first = first;
		this.last = last;
	}

	public Sequence() {

	}

	/**
	 * 是否和此序列尾部相连
	 * 如果此序列的末尾+每个存储块大小与目标值相等，则相连
	 * @param value
	 * @return
	 */
	public boolean isAdjacentToLast(long value) {
		return last + blockSize == value;
	}

	/**
	 * 是否和此序列头部相连
	 * @param value
	 * @return
	 */
	public boolean isAdjacentToFirst(long value) {
		return first - blockSize == value;
	}

	/**
	 * 是否被包含在此序列中
	 * @param value
	 * @return
	 */
	public boolean contains(long value) {
		return first <= value && value <= last;
	}

	/**
	 * 此段序列包含的存储单元数量
	 * @return
	 */
	public long range() {
		return first == last ? 1 : (last - first) / blockSize + 1;
	}

	@Override
	public String toString() {
		return first == last ? "" + first : first + DEVIDE + last;
	}

	/**
	 * 获取序列头数值
	 * @return
	 */
	public long getFirst() {
		return first;
	}

	/**
	 * 设置序列头数值
	 * @param first
	 */
	public void setFirst(long first) {
		this.first = first;
	}

	/**
	 * 获取序列尾数值
	 * @return
	 */
	public long getLast() {
		return last;
	}

	/**
	 * 设置序列尾数值
	 * @param last
	 */
	public void setLast(long last) {
		this.last = last;
	}

	public interface Closure<T extends Throwable> {
		public void execute(long value) throws T;
	}

	public <T extends Throwable> void each(Closure<T> closure) throws T {
		for (long i = first; i <= last; i++) {
			closure.execute(i);
		}
	}


}
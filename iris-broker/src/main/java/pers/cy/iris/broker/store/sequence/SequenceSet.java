package pers.cy.iris.broker.store.sequence;

import pers.cy.iris.broker.store.QueueItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @Author:cy
 * @Date:Created in  17/5/26
 * @Destription:
 */
public class SequenceSet extends LinkedNodeList<Sequence> implements Iterable<Long> {

	int blockSize = QueueItem.CONSUMER_QUEUE_ITEM_SIZE;

	public SequenceSet() {

	}

	/**
	 * 将一个序列加入序列集合
	 *
	 * @param value
	 */
	public void add(Sequence value) {
		// TODO we can probably optimize this a bit
		for (long i = value.first; i < value.last + blockSize; i = i + blockSize) {
			add(i);
		}
	}

	/**
	 * 将给定值加入到链表中
	 * 遍历链表，尝试将此值加入每一个节点
	 * 如果此值已经是节点中包含的值，返回false
	 * 如果加入此值后节点符合合并条件，则合并
	 *
	 * @param value
	 * @return
	 */
	public synchronized boolean add(long value) {

		//如果是加进序列的第一个值，设置为头节点并返回
		if (isEmpty()) {
			addFirst(new Sequence(value));
			return true;
		}

		Sequence sequence = getHead();
		while (sequence != null) {
			//校验这个值能否连接到序列尾部
			if (sequence.isAdjacentToLast(value)) {
				// 修改头序列的last
				sequence.last = value;
				// 加上这个值以后，是否可以合并两个序列
				if (sequence.getNext() != null) {
					Sequence next = sequence.getNext();
					if (next.isAdjacentToFirst(value)) {
						// 两个序列被此值连接，后面的序列从链表中断开，头序列增长
						sequence.last = next.last;
						next.unlink();
					}
				}
				return true;
			}

			if (sequence.isAdjacentToFirst(value)) {
				//校验这个值能否连接到序列头部
				sequence.first = value;

				// 加上这个值以后，是否可以与前面的序列连接
				if (sequence.getPrevious() != null) {
					Sequence prev = sequence.getPrevious();
					if (prev.isAdjacentToLast(value)) {
						// 两个序列被此值连接，前面的序列从链表中断开，序列增长
						sequence.first = prev.first;
						prev.unlink();
					}
				}
				return true;
			}

			// 如果属于前序序列，加入头部
			if (value < sequence.first) {
				sequence.linkBefore(new Sequence(value));
				return true;
			}

			// 已经在头部序列中，返回失败
			if (sequence.contains(value)) {
				return false;
			}

			sequence = sequence.getNext();
		}

		// 遍历所有序列都无法加入，加入链表尾部
		addLast(new Sequence(value));
		return true;
	}

	/**
	 * 从序列集合中移除一个序列
	 *
	 * @param sequence
	 * @return
	 */
	public boolean remove(Sequence sequence) {
		if (sequence == null) {
			return false;
		}

		//循环移除序列中的每个值
		for (long first = sequence.first; first <= sequence.last; first = first + sequence.blockSize) {
			remove(first);
		}

		return true;
	}

	/**
	 * 将给定值移除出序列集合
	 * @param value
	 * @return
	 */
	public boolean remove(long value) {
		Sequence sequence = getHead();
		//对序列集合中的每个节点，尝试移除此值
		while (sequence != null) {
			//如果要移除的值就在此序列中
			if (sequence.contains(value)) {
				if (sequence.range() == 1) {
					//单个值直接移除
					sequence.unlink();
					return true;
				} else if (sequence.getFirst() == value) {
					//待移除的值在头部
					sequence.setFirst(value + blockSize);
					return true;
				} else if (sequence.getLast() == value) {
					//待移除的值在尾部
					sequence.setLast(value - blockSize);
					return true;
				} else {
					//在序列中部，将序列拆成2个序列
					sequence.linkBefore(new Sequence(sequence.first, value - blockSize));
					sequence.linkAfter(new Sequence(value + blockSize, sequence.last));
					sequence.unlink();
					return true;
				}

			}

			sequence = sequence.getNext();
		}

		return false;
	}

	/**
	 * 移除序列集合中的第一个存储单元
	 * @return
	 */
	public long removeFirst() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}

		Sequence rc = removeFirstSequence(1);
		return rc.first;
	}

	/**
	 * 移除最后一个序列
	 * @return
	 */
	public Sequence removeLastSequence() {
		if (isEmpty()) {
			return null;
		}

		Sequence rc = tail();
		rc.unlink();
		return rc;
	}

	/**
	 * 从序列头部移除若干存储单元
	 * @param count
	 * @return 被移除的序列
	 */
	public Sequence removeFirstSequence(long count) {
		if (isEmpty()) {
			return null;
		}

		Sequence sequence = getHead();
		//FIXME sequence.range() < count 的情况是否有问题
		while (sequence != null) {
			if (sequence.range() == count) {
				sequence.unlink();
				return sequence;
			}
			if (sequence.range() > count) {
				Sequence rc = new Sequence(sequence.first, sequence.first + (count - 1) * blockSize);
				sequence.first += count * blockSize;
				return rc;
			}
			sequence = sequence.getNext();
		}
		return null;
	}

	/**
	 * 计算两个值之间所有缺少的序列
	 * @param first
	 * @param last
	 * @return
	 */
	public List<Sequence> missing(long first, long last) {
		//FIXME

		ArrayList<Sequence> rc = new ArrayList<Sequence>();
		if (first > last) {
			throw new IllegalArgumentException("First cannot be more than last");
		}
		if (isEmpty()) {
			rc.add(new Sequence(first, last));
			return rc;
		}

		Sequence sequence = getHead();
		while (sequence != null && first <= last) {
			if (sequence.contains(first)) {
				first = sequence.last + blockSize;
			} else {
				if (first < sequence.first) {
					if (last < sequence.first) {
						rc.add(new Sequence(first, last));
						return rc;
					} else {
						rc.add(new Sequence(first, sequence.first - blockSize));
						first = sequence.last + blockSize;
					}
				}
			}
			sequence = sequence.getNext();
		}

		if (first <= last) {
			rc.add(new Sequence(first, last));
		}
		return rc;
	}

	/**
	 * 返回序列集合中的所有序列
	 * @return
	 */
	public List<Sequence> allSequence() {
		ArrayList<Sequence> rc = new ArrayList<Sequence>(size());
		Sequence sequence = getHead();
		while (sequence != null) {
			rc.add(new Sequence(sequence.first, sequence.last));
			sequence = sequence.getNext();
		}
		return rc;
	}

	/**
	 * 返回所有序列的字符串字面值
	 * @return
	 */
	public List<String> getSequences() {
		List<Sequence> sequences = allSequence();
		if (sequences == null || sequences.size() == 0) {
			return null;
		}

		List<String> result = new ArrayList<String>(sequences.size());
		for (Sequence sequence : sequences) {
			result.add(sequence.toString());
		}

		return result;
	}

	/**
	 * 将字符串字面值反序列化为链表
	 * @param sequences
	 */
	public void setSequences(List<String> sequences) {
		if (sequences == null || sequences.size() == 0) {
			return;
		}

		for (String seqStr : sequences) {
			Sequence sequence = toSequence(seqStr);
			if (sequence != null) {
				if (isEmpty()) {
					addFirst(sequence);
					continue;
				}
				Sequence curSeq = getHead();
				for (; curSeq != null; curSeq = curSeq.getNext()) {
					if (sequence.getLast() < curSeq.getFirst()) {
						curSeq.linkBefore(sequence);
						break;
					}
				}
				if (curSeq == null) {
					tail().linkAfter(sequence);
				}
			}
		}
	}

	/**
	 * 字符串转化为序列区间
	 * @param str
	 * @return
	 */
	private Sequence toSequence(String str) {

		String[] data = str.split(Sequence.DEVIDE);
		if (data.length > 3 || data.length == 0) {
			throw new RuntimeException("Invalid sequence:" + str);
		}

		if (data.length == 1) {
			return new Sequence(Long.parseLong(data[0]));
		} else {
			return new Sequence(Long.parseLong(data[0]), Long.parseLong(data[1]));
		}


	}

	private void checkValue(long value) {
		if (value % blockSize != 0) {
			throw new RuntimeException("Invalid value, mod " + blockSize + "!=0");
		}
	}

	/**
	 * 序列集合中是否存在给定值
	 * @param value
	 * @return
	 */
	public boolean contains(long value) {
		if (isEmpty()) {
			return false;
		}

		checkValue(value);

		Sequence sequence = getHead();
		while (sequence != null) {
			if (sequence.contains(value)) {
				return true;
			}
			sequence = sequence.getNext();
		}

		return false;
	}

	/**
	 * 序列集合中是否存在序列包含first到last中的所有值
	 * @param first
	 * @param last
	 * @return
	 */
	public boolean contains(long first, long last) {
		if (isEmpty()) {
			return false;
		}
		Sequence sequence = getHead();
		while (sequence != null) {
			if (sequence.first <= first && first <= sequence.last) {
				return last <= sequence.last;
			}
			sequence = sequence.getNext();
		}
		return false;
	}

	/**
	 * 链表中是否包含此序列
	 * @param sequence
	 * @return
	 */
	public boolean contains(Sequence sequence) {
		return sequence != null && contains(sequence.getFirst(), sequence.getLast());
	}


	/**
	 * 计算序列集合中存储单元的总个数
	 * @return
	 */
	public long rangeSize() {
		long result = 0;
		Sequence sequence = getHead();
		while (sequence != null) {
			result += sequence.range();
			sequence = sequence.getNext();
		}
		return result;
	}

	/**
	 * 获取迭代器
	 * @return
	 */
	public Iterator<Long> iterator() {
		return new SequenceIterator();
	}

	private class SequenceIterator implements Iterator<Long> {

		private Sequence currentEntry;
		private long lastReturned = -1;

		public SequenceIterator() {
			currentEntry = getHead();
			if (currentEntry != null) {
				lastReturned = currentEntry.first - blockSize;
			}
		}

		/**
		 * 是否存在下一个序列
		 * @return
		 */
		public boolean hasNext() {
			return currentEntry != null;
		}


		public Long next() {
			if (currentEntry == null) {
				throw new NoSuchElementException();
			}

			if (lastReturned < currentEntry.first) {
				lastReturned = currentEntry.first;
				if (currentEntry.range() == blockSize) {
					currentEntry = currentEntry.getNext();
				}
			} else {
				lastReturned++;
				if (lastReturned == currentEntry.last) {
					currentEntry = currentEntry.getNext();
				}
			}

			return lastReturned;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	public int blockSize() {
		return blockSize;
	}

	public static void main(String[] args) throws Exception {


		SequenceSet sequenceSet = new SequenceSet();

		long start = System.currentTimeMillis();
		for (int i = 0; i < 100000 * 22; i = i + 22) {
			sequenceSet.add(i);
		}

		long end = System.currentTimeMillis();

		long used = end - start;

		System.out.println("used: " + used);


	}

}
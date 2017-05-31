package pers.cy.iris.broker.store.offset;

import pers.cy.iris.broker.store.QueueItem;
import pers.cy.iris.broker.store.sequence.Sequence;
import pers.cy.iris.broker.store.sequence.SequenceSet;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author:cy
 * @Date:Created in  17/5/25
 * @Destription: 非连续位置
 */
public class UnSequenceOffset extends Offset{
	//拉取最大位置,用于非顺序消费
	private transient AtomicLong pullEndOffset = new AtomicLong(0);
	//ack位置
	private SequenceSet acks = new SequenceSet();

	public UnSequenceOffset() {
	}

	public UnSequenceOffset(String topic, short queueId, String consumer) {
		super(topic, queueId, consumer);
	}

	public SequenceSet getAcks() {
		return acks;
	}

	public void setAcks(SequenceSet acks) {
		this.acks = acks;
	}

	public void cleanAcks(){
		this.acks = new SequenceSet();
	}

	public AtomicLong getPullEndOffset() {
		return pullEndOffset;
	}

	public void setPullEndOffset(AtomicLong pullEndOffset) {
		this.pullEndOffset = pullEndOffset;
	}

	public void updateOffset(Offset offset) {
		//更新offset
		super.updateOffset(offset);
		SequenceSet set = new SequenceSet();
		long ackOffset = getAckOffset().get();

		if (offset instanceof UnSequenceOffset) {
			UnSequenceOffset unSeqOffset = (UnSequenceOffset) offset;
			set = unSeqOffset.getAcks();
		} else if (ackOffset > 0) {
			//如果是连续的offset且ack大于0，则添加到序列并试图合并
			long startOffset = getSubscribeOffset().get() + QueueItem.CONSUMER_QUEUE_ITEM_SIZE;
			if (startOffset <= ackOffset) {
				set.addFirst(new Sequence(startOffset, ackOffset));
			}
		}
		setAcks(set);
	}
}

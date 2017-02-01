package com.cy.iris.commons.util.eventmanager.disruptor.test;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by cy on 17/1/23.
 */
public class DisruptorTest {

	private long count = 1000 * 1000 * 1000;

	private BlockingQueue<Long> queue = new ArrayBlockingQueue<Long>(1024*1024);

	public static void main(String[] args){
		new DisruptorTest().testDis();
		//new DisruptorTest().testQue();
	}

	private void testDis(){
		EventFactory<LongEvent> eventFactory = new LongEventFactory();
		ExecutorService executor = Executors.newFixedThreadPool(1);
		int ringBufferSize = 1024 * 1024; // RingBuffer 大小，必须是 2 的 N 次方；

		Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(eventFactory,
				ringBufferSize, executor, ProducerType.SINGLE,
				new YieldingWaitStrategy());

		EventHandler<LongEvent> eventHandler = new LongEventHandler();
		disruptor.handleEventsWith(eventHandler);

		disruptor.start();

		Executors.newSingleThreadExecutor().submit(new Producer(disruptor));
	}

	private void testQue(){
		Executors.newSingleThreadExecutor().submit(new QueueConsumer());
		Executors.newSingleThreadExecutor().submit(new QueueProducer());

	}


	class Producer implements Runnable{

		private Disruptor<LongEvent> disruptor;

		private long localCount = 0;

		public Producer(Disruptor<LongEvent> disruptor) {
			this.disruptor = disruptor;
		}

		@Override
		public void run() {
			// 发布事件；
			RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
			long time = System.currentTimeMillis();
			while(localCount<count) {
				long sequence = ringBuffer.next();//请求下一个事件序号；

				try {
					LongEvent event = ringBuffer.get(sequence);//获取该序号对应的事件对象；
					long data = localCount++;//获取要通过事件传递的业务数据；
					event.set(data);
				} finally {
					ringBuffer.publish(sequence);//发布事件；
				}
			}
			System.out.println(System.currentTimeMillis() - time);
		}
	}

	class QueueProducer implements Runnable{
		private long queueCount = 0;
		@Override
		public void run() {
			long time = System.currentTimeMillis();
			while(queueCount<count) {
				try {
					queue.put(queueCount++);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println(System.currentTimeMillis() - time);
		}
	}

	class QueueConsumer implements Runnable{

		@Override
		public void run() {
			while(true){
				try {
					queue.take();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}

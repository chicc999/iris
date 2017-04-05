package com.cy.iris.benchmark.stat;

import com.cy.iris.commons.service.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author:cy
 * @Date:Created in  17/4/5
 * @Destription:
 */
public class CollectJob extends Service{
	private static Logger logger = LoggerFactory.getLogger(CollectJob.class);

	private long maxMsgCount;

	private AtomicLong num = new AtomicLong(0);
	private AtomicLong time= new AtomicLong(0);

	AtomicLong lastNum= new AtomicLong(0);
	AtomicLong lastTime= new AtomicLong(0);

	private AtomicInteger errs = new AtomicInteger(0);

	ExecutorService statExecutor;

	ExecutorService executor;


	@Override
	public void beforeStart() throws Exception {

	}

	@Override
	public void doStart() throws Exception {

	}

	@Override
	public void afterStart() throws Exception {

	}

	@Override
	public void beforeStop() {

	}

	@Override
	public void doStop() {

	}

	private SamplerClient getInstance(String name) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
		Class clazz = Class.forName(name);
		return (SamplerClient)clazz.newInstance();
	}

	public void stat(String SamplerClient,int threadNum) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
		this.executor = Executors.newFixedThreadPool(threadNum);
		CyclicBarrier cyclicBarriers = new CyclicBarrier(threadNum);

		for(int i=0;i< threadNum;i++){
			executor.submit(new StatTask(getInstance(SamplerClient),cyclicBarriers));
		}
	}

	class StatTask implements Runnable{
		private  Logger logger = LoggerFactory.getLogger(StatTask.class);
		private SamplerClient client;
		private CyclicBarrier cyclicBarrier;

		public StatTask(SamplerClient client, CyclicBarrier cyclicBarrier) {
			this.client = client;
			this.cyclicBarrier = cyclicBarrier;
		}

		@Override
		public void run() {

			try{
				client.start();
				logger.info("线程"+ Thread.currentThread().getName() + "初始化完毕");
				cyclicBarrier.await();
			}catch (Exception e){
				e.printStackTrace();
			}

			while (!Thread.interrupted() && client.isStarted()) {
				SampleResult result ;
				result = client.work();

				long cur = num.incrementAndGet();
				time.addAndGet(result.getTime());
				if(!result.isSuccess()){
					errs.incrementAndGet();
				}


				if (maxMsgCount > 0 && cur >= maxMsgCount) {
					break;
				}
			}
			client.stop();
		}
	}

}


package pers.cy.iris.benchmark.stat;

import pers.cy.iris.commons.service.Service;
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

	/**
	 * 进行性能测试
	 * @param SamplerClient 实现的SamplerClient的客户端
	 * @param threadNum 启用的线程
	 * @param maxReqCount 最大请求数量,为0则一直发送
	 * @param interval 性能统计间隔
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 */
	public void stat(String SamplerClient,int threadNum,int maxReqCount,long interval) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
		this.maxMsgCount = maxReqCount;
		this.executor = Executors.newFixedThreadPool(threadNum);
		CyclicBarrier cyclicBarriers = new CyclicBarrier(threadNum);

		this.statExecutor = Executors.newSingleThreadExecutor();
		this.statExecutor.submit(new CollectTask(threadNum,interval));

		for(int i=0;i< threadNum;i++){
			executor.submit(new StatTask(getInstance(SamplerClient),cyclicBarriers));
		}
		executor.shutdown();
		statExecutor.shutdown();
	}

	/**
	 * 进行性能测试
	 * @param SamplerClient 实现的SamplerClient的客户端
	 * @param threadNum 启用的线程
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 */
	public void stat(String SamplerClient,int threadNum) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
		stat(SamplerClient,threadNum,0,4000);
	}

	/**
	 * 执行请求、统计每次请求结果
	 */
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

	/**
	 * 汇总任务
	 */
	class CollectTask implements Runnable{
		private  Logger logger = LoggerFactory.getLogger(StatTask.class);
		private int threadNum;
		private long interval;

		public CollectTask(int threadNum, long interval) {
			this.threadNum = threadNum;
			this.interval = interval;
		}

		@Override
		public void run() {
			while(!Thread.interrupted()) {
				try {
					Thread.sleep(interval);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				long curNum = num.get();
				long curTime = time.get();
				long count = curNum - lastNum.get();
				long totalTime = curTime - lastTime.get();

				lastNum.set(curNum);
				lastTime.set(curTime);


				String info = String.format("请求总数:%d , %ds 平均tps:%.0f , 最近%ds tps:%.0f , err:%d", curNum,(int)(lastTime.get()/threadNum/1000/1000/1000), curTime == 0 ? 0 : curNum * 1000.0 * 1000 * 1000 * threadNum / curTime,interval/1000, totalTime == 0 ? 0 : count * 1000.0 * 1000 * 1000 * threadNum / totalTime , errs.get());
				logger.info(info);
				System.out.println(info);
			}

		}
	}


}


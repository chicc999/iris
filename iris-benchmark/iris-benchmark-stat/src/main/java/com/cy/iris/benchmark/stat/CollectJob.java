package com.cy.iris.benchmark.stat;

import com.cy.iris.commons.service.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	AtomicLong num = new AtomicLong(0);
	AtomicLong time= new AtomicLong(0);

	AtomicLong lastNum= new AtomicLong(0);
	AtomicLong lastTime= new AtomicLong(0);

	AtomicInteger errs = new AtomicInteger(0);

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

	public void stat(SamplerClient client,int threadNum){
		this.executor = Executors.newFixedThreadPool(threadNum);
	}

}

class StatTask implements Runnable{
	private static Logger logger = LoggerFactory.getLogger(StatTask.class);
	private SamplerClient client;
	@Override
	public void run() {

		try{
			client.start();
		}catch (Exception e){
			e.printStackTrace();
		}

		while (!Thread.interrupted() && client.isStarted()) {
			SampleResult result;
				try{
					result = client.doWork();
				}catch (Exception e){

				}
		}
		client.stop();
	}
}
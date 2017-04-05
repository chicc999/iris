package com.cy.iris.benchmark.stat;

import com.cy.iris.commons.service.Service;

/**
 * @Author:cy
 * @Date:Created in  17/4/5
 * @Destription:
 */
public abstract class SamplerClient extends Service{

	protected SampleResult work(){
		SampleResult result = new SampleResult();
		try{
			boolean success = doWork();
			result.done(success);
		}catch (Exception e){
			result.done(false);
		}
		return result;
	}


	abstract protected boolean doWork();
}

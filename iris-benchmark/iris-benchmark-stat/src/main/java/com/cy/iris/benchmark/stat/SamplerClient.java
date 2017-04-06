package com.cy.iris.benchmark.stat;

import com.cy.iris.commons.service.Service;

/**
 * @Author:cy
 * @Date:Created in  17/4/5
 * @Destription: 采样客户端
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


	/**
	 * 某次采样结果,如果消息应答正确返回true,应答错误返回false,出现异常抛出异常.
	 * @return
	 */
	abstract protected boolean doWork();
}

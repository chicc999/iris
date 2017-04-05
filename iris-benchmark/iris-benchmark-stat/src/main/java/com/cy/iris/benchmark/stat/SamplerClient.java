package com.cy.iris.benchmark.stat;

import com.cy.iris.commons.service.Service;

/**
 * @Author:cy
 * @Date:Created in  17/4/5
 * @Destription:
 */
public abstract class SamplerClient extends Service{

	abstract protected SampleResult doWork();
}

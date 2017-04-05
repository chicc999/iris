package com.cy.iris.benchmark.stat;


/**
 * @Author:cy
 * @Date:Created in  17/4/5
 * @Destription: 单次请求返回结果
 */
public class SampleResult {
	private long startTime;
	private long endTime;
	private boolean success;

	public SampleResult() {
		this.startTime = System.nanoTime();
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public boolean isSuccess() {
		return success;
	}

	public void done(boolean success){
		this.success = success;
		this.endTime = System.nanoTime();
	}
}

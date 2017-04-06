package pers.cy.iris.commons.util;

/**
 * 比较系统调用与缓存的性能
 */
public class SystemClockTest {
	public static void main(String[] args){
		SystemClockTest test = new SystemClockTest();
		int times = 1000 * 1000 * 1000;
		long start = System.currentTimeMillis();
		test.SystemGetTime(times);
		System.out.println("系统调用耗时:"+(System.currentTimeMillis()-start));

		start = System.currentTimeMillis();
		test.SystemClockGetTime(times);
		System.out.println("缓存调用耗时:"+(System.currentTimeMillis()-start));
	}

	private void SystemGetTime(int times){
		for(int i = 0;i<times;i++){
			System.currentTimeMillis();
		}
	}

	private void SystemClockGetTime(int times){
		for(int i = 0;i < times;i++){
			SystemClock.currentTimeMillis();
		}
	}

}

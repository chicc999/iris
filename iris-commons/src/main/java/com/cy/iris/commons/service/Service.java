package pers.cy.iris.commons.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 子类实现抽象方法,调用start()方法启动,Service管理整个生命周期的具体变化
 * <p>
 * ACTION            sercive:start()                beforeStart()             doStart()            afterStart()
 * STATE    STOPPED -----------------> WILL_START ----------------> STARTING ------------> STARTED
 * <p>
 * ACTION                          sercive:stop()                beforeStop()             doStop()
 * STATE    STARTED/START_FAILED -----------------> WILL_STOP ----------------> STOPPING -------------> STOPPED/STOP_FAILED
 */
public abstract class Service implements LifeCycle {

	Logger logger = LoggerFactory.getLogger(Service.class);

	// 服务状态
	protected final AtomicReference<ServiceState> serviceState =
			new AtomicReference<ServiceState>(ServiceState.STOPPED);

	@Override
	public final void start() throws Exception {
		if(channge2StartState()) {
			beforeStart();
			if (serviceState.compareAndSet(ServiceState.WILL_START, ServiceState.STARTING)) {
				try {
					doStart();
					serviceState.set(ServiceState.STARTED);
					afterStart();
				} catch (Exception e) {
					serviceState.set(ServiceState.START_FAILED);
					logger.error("启动失败", e);
					stop();
				}
			}else{
				throw new RuntimeException("启动失败,当前服务器状态为:"+ serviceState.get() + ",预期的状态为 WILL_START");
			}
		}else{
			throw new RuntimeException("启动失败,当前服务器状态为:"+ serviceState.get() + ",预期的状态为 STOPPED");
		}
	}

	@Override
	public final void stop() {
		//服务处于开启状态或者上次关闭失败,允许再次关闭
		if(channge2StopState()) {

			beforeStop();

			if(serviceState.compareAndSet(ServiceState.WILL_STOP,ServiceState.STOPPING)){
				try {
					doStop();
				}catch (Exception e) {
					serviceState.set(ServiceState.STOP_FAILED);
					throw new RuntimeException(e);
				}
				serviceState.set(ServiceState.STOPPED);
			}
		}

	}

	@Override
	public final boolean isStarted() {

			switch (serviceState.get()) {
				case WILL_STOP:
				case STOPPING:
				case STOP_FAILED:
				case STOPPED:
					return false;
				default:
					return true;
			}

	}

	/**
	 * 是否允许关闭,如果允许是否更改状态成功
	 * @return 是否能变成将要启动状态
	 */
	public  boolean channge2StopState(){
		if(isStarted() || serviceState.get().equals(ServiceState.STOP_FAILED)) {
			return serviceState.compareAndSet(ServiceState.STOP_FAILED,ServiceState.WILL_STOP)
					|| serviceState.compareAndSet(ServiceState.WILL_START,ServiceState.WILL_STOP)
					|| serviceState.compareAndSet(ServiceState.STARTING,ServiceState.WILL_STOP)
					|| serviceState.compareAndSet(ServiceState.STARTED,ServiceState.WILL_STOP)
					|| serviceState.compareAndSet(ServiceState.START_FAILED,ServiceState.WILL_STOP);

		}
		return false;
	}

	/**
	 * 是否允许启动,如果允许是否更改状态成功
	 * @return
	 */
	public  boolean channge2StartState(){
		return serviceState.compareAndSet(ServiceState.STOPPED,ServiceState.WILL_START)
				|| serviceState.compareAndSet(ServiceState.STOP_FAILED,ServiceState.WILL_START);
	}

	/**
	 * 启动前
	 *
	 * @throws Exception
	 */
	public abstract void beforeStart() throws Exception;

	/**
	 * 启动
	 *
	 * @throws Exception
	 */
	public abstract void doStart() throws Exception;

	/**
	 * 启动后
	 *
	 * @throws Exception
	 */
	public abstract void afterStart() throws Exception;

	/**
	 * 停止前
	 */
	public abstract void beforeStop();

	/**
	 * 停止
	 */
	public abstract void doStop();

}

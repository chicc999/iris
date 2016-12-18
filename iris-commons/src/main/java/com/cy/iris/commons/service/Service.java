package com.cy.iris.commons.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 子类实现抽象方法,调用start()方法启动,Service管理整个生命周期的具体变化
 *
 *  ACTION            sercive:start()                beforeStart()             doStart()            afterStart()
 *  STATE    STOPPED -----------------> WILL_START ----------------> STARTING ------------> STARTED
 *
 *  ACTION                          sercive:stop()                beforeStop()             doStop()           afterStop()
 *  STATE    STARTED/START_FAILED -----------------> WILL_STOP ----------------> STOPPING -------------> STOPPED
 *
 */
public abstract class Service implements LifeCycle {

    Logger logger = LoggerFactory.getLogger(Service.class);

    // 是否启动
    protected final AtomicBoolean started = new AtomicBoolean(false);

    // 服务状态
    protected final AtomicReference<ServiceState> serviceState =
            new AtomicReference<ServiceState>();

    @Override
    public final void start() throws Exception {
        serviceState.set(ServiceState.WILL_START);
        beforeStart();
        if (started.compareAndSet(false, true)) {
            try {
                serviceState.set(ServiceState.STARTING);
                doStart();
                serviceState.set(ServiceState.STARTED);
                afterStart();
            } catch (Exception e) {
                serviceState.set(ServiceState.START_FAILED);
                logger.error("启动失败", e);
                stop();
            }
        }
    }

    @Override
    public final void stop() {
        // 设置状态将要关闭
        serviceState.set(ServiceState.WILL_STOP);

        beforeStop();

        if (started.compareAndSet(true, false)) {
            serviceState.set(ServiceState.STOPPING);
            doStop();
            serviceState.set(ServiceState.STOPPED);
            afterStop();
        }


    }

    @Override
    public final boolean isStarted() {
		//若没有启动，直接返回false
		if (started.get()) {
			switch (serviceState.get()) {
				case WILL_STOP:
				case STOPPING:
				case STOPPED:
					return false;
				default:
					return true;
			}
		}
		return false;
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

    /**
     * 停止后
     */
    public abstract void afterStop();

}

package com.cy.iris.commons.util.lock;

import com.cy.iris.commons.service.Service;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.zookeeper.CreateMode;

/**
 * 基于zk实现的分布式读写锁
 */
public class ZookeeperReadWriteLocks extends Service{
	private final InterProcessReadWriteLock lock ;
	private final InterProcessMutex readLock ;
	private final InterProcessMutex writeLock ;
	private final CuratorFramework client;
	private static final String LOCK_ROOT = "/lock";
	private static String LOCK_PATH ;

	public ZookeeperReadWriteLocks(CuratorFramework client, String lockPath) {
		this.LOCK_PATH = LOCK_ROOT + lockPath;
		this.lock = new InterProcessReadWriteLock(client, this.LOCK_PATH);
		this.readLock = this.lock.readLock();
		this.writeLock = this.lock.writeLock();
		this.client = client;
	}

	public final Object get(String dataPath) throws Exception {
		this.readLock.acquire();
		try {
			return getDataByPath(dataPath);
		}finally {
			this.readLock.release();
		}
	}

	private Object getDataByPath(String dataPath) throws Exception {
		return client.getData().forPath(dataPath);
	}

	@Override
	public void beforeStart() throws Exception {

	}

	@Override
	public void doStart() throws Exception {
		if(null == this.client.checkExists().forPath(this.LOCK_PATH)) {
			this.client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(this.LOCK_PATH,"".getBytes("utf-8"));
		}
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
}

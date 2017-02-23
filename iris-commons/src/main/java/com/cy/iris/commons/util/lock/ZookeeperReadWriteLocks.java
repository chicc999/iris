package com.cy.iris.commons.util.lock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;

/**
 * 基于zk实现的分布式读写锁
 */
public class ZookeeperReadWriteLocks {
	private final InterProcessReadWriteLock lock ;
	private final InterProcessMutex readLock ;
	private final InterProcessMutex writeLock ;
	private final CuratorFramework client;

	public ZookeeperReadWriteLocks(CuratorFramework client, String lockPath) {
		this.lock = new InterProcessReadWriteLock(client, lockPath);
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
}

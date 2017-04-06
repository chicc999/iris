package pers.cy.iris.commons.util.lock;

import pers.cy.iris.commons.exception.LockTimeoutException;
import pers.cy.iris.commons.service.Service;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.zookeeper.CreateMode;

import java.util.concurrent.TimeUnit;

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

	/**
	 * 获取指定路径下的数据
	 * 如果获取不到锁会一直阻塞
	 * @param dataPath 待获取数据所在的路径
	 * @return 指定路径下的数据
	 *
	 * @throws Exception 其他异常
	 */
	public final byte[] get(String dataPath) throws Exception {
		this.readLock.acquire();
		try {
			return getDataByPath(dataPath);
		}finally {
			this.readLock.release();
		}
	}

	/**
	 * 获取指定路径下的数据
	 * @param dataPath 待获取数据所在的路径
	 * @param timeout  最多等待时间,单位ms
	 * @return 指定路径下的数据
	 *
	 * @throws LockTimeoutException 获取锁超时
	 * @throws Exception 其他异常
	 */
	public final byte[] get(String dataPath,long timeout) throws Exception {
		if(!this.readLock.acquire(timeout, TimeUnit.MILLISECONDS)){
			throw new LockTimeoutException();
		}
		try {
			return getDataByPath(dataPath);
		}finally {
			this.readLock.release();
		}
	}

	/**
	 * 将指定路径修改为指定值
	 * @param dataPath 数据的路径
	 * @param value 数据的值
	 *
	 * @throws Exception
	 */
	public final void set(String dataPath,byte[] value) throws Exception {
		this.writeLock.acquire();
		try{
			setDataByPath(dataPath,value);
		}finally {
			this.writeLock.release();
		}
	}

	/**
	 *
	 * 将指定路径修改为指定值
	 * @param dataPath 数据的路径
	 * @param value 数据的值
	 * @param timeout 获取锁的最大等待时间,单位ms
	 *
	 * @throws LockTimeoutException 获取锁超时
	 * @throws Exception
	 */
	public final void set(String dataPath,byte[] value,long timeout) throws Exception {
		if(!this.writeLock.acquire(timeout,TimeUnit.MILLISECONDS)){
			throw new LockTimeoutException();
		}
		try{
			setDataByPath(dataPath,value);
		}finally {
			this.writeLock.release();
		}
	}

	private void setDataByPath(String dataPath,byte[] value)throws Exception{
		client.setData().forPath(dataPath,value);
	}

	private byte[] getDataByPath(String dataPath) throws Exception {
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

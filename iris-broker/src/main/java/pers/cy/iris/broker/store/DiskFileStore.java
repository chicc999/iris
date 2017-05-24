package pers.cy.iris.broker.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.cy.iris.commons.exception.IrisException;
import pers.cy.iris.commons.exception.QueueNotExistException;
import pers.cy.iris.commons.exception.ServiceNotAvailableException;
import pers.cy.iris.commons.model.message.StoreMessage;
import pers.cy.iris.commons.service.Service;
import pers.cy.iris.commons.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;

/**
 * @Author:cy
 * @Date:Created in  17/4/17
 * @Destription: 硬盘文件存储系统
 */
public class DiskFileStore extends Service implements Store {

	private static Logger logger = LoggerFactory.getLogger(DiskFileStore.class);

	private DiskFileStoreConfig  storeConfig ;

	private OffsetManager offsetManager;

	private FileManager fileManager;

	// 随机访问锁文件
	protected RandomAccessFile lockRaf;

	// 文件锁
	protected FileLock fileLock;

	public static int restartTimes = 0;

	public DiskFileStore() {
	}

	public DiskFileStore(DiskFileStoreConfig storeConfig) {
		this.storeConfig = storeConfig;
	}

	@Override
	public void beforeStart() throws Exception {

		if(storeConfig == null){
			storeConfig = new DiskFileStoreConfig();
		}

		if(fileManager == null){
			fileManager = new FileManager();
			fileManager.setStoreConfig(storeConfig);
		}

		if(offsetManager == null){
			offsetManager = new OffsetManager();
			offsetManager.setFileManager(fileManager);
		}

	}

	@Override
	public void doStart() throws Exception {
		//尝试重启次数增加
		restartTimes++;
		// 独占文件锁，确保只有一个实例启动
		lockFile();

		fileManager.start();

		offsetManager.start();
	}

	@Override
	public void afterStart() throws Exception {
	}

	@Override
	public void beforeStop() {

	}

	@Override
	public void doStop() throws Exception{
		fileManager.stop();

		//释放锁文件
		fileLock.release();
		lockRaf.close();
	}

	public DiskFileStoreConfig getStoreConfig() {
		return storeConfig;
	}

	public void setStoreConfig(DiskFileStoreConfig storeConfig) {
		this.storeConfig = storeConfig;
	}

	public void setOffsetManager(OffsetManager offsetManager) {
		this.offsetManager = offsetManager;
	}



	/**
	 * 独占文件锁，确保只有一个Store实例启动
	 *
	 * @throws Exception
	 */
	protected void lockFile() throws Exception {
		File lockFile = fileManager.getLockFile();
		FileUtil.createFile(lockFile);
		// 加文件锁
		lockRaf = new RandomAccessFile(lockFile, "rw");
		fileLock = lockRaf.getChannel().tryLock();
		if (fileLock == null) {
			throw new IOException(String.format("lock file error,%s", lockFile.getPath()));
		}
	}

	@Override
	public PutResult putMessage(StoreMessage message) throws ServiceNotAvailableException {
		logger.debug("put message {}",message.getMessageId());
		if (message == null) {
			return null;
		}
		if (!isStarted() ) {
			throw new ServiceNotAvailableException("DiskFileStore state: " + this.serviceState.get());
		}

		PutResult result = new PutResult();

		try{
			short queueId = getQueueId(message);
		}catch (Exception e){

		}

		return null;
	}

	/**
	 * 获取待存储的队列ID.
	 * @param message
	 * @return
	 */
	private short getQueueId(StoreMessage message) throws IrisException{
		short count = getQueueCount(message.getTopic());
		if (count <= 0) {
			// 消费队列不存在
			throw new QueueNotExistException("topic : " + message.getTopic());
		}
		return 0;
	}

	/**
	 * 获取topic在本broker的队列数量
	 * @param topic 主题名称
	 * @return queueId
	 * @throws QueueNotExistException
	 */
	private short getQueueCount(String topic)throws QueueNotExistException{
		return offsetManager.getQueueCount(topic);
	}
}

package pers.cy.iris.broker.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.cy.iris.commons.exception.QueueNotExistException;
import pers.cy.iris.commons.service.Service;
import pers.cy.iris.commons.util.ArgumentUtil;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Author:cy
 * @Date:Created in  17/5/13
 * @Destription: 服务端偏移量管理
 */
public class OffsetManager extends Service{

	private static Logger logger = LoggerFactory.getLogger(OffsetManager.class);

	private FileManager fileManager;

	// 偏移量文件
	private File offsetFile;
	// 队列文件
	private File queueFile;
	// 偏移量文件备份(双写)
	private File offsetFileBack;
	// 队列文件备份(双写)
	private File queueFileBack;

	// 消费者偏移量
	//private ConcurrentMap<String/*consumer*/, TopicOffset> offsets = new ConcurrentHashMap<String, TopicOffset>();
	// 队列
	private ConcurrentMap<String/*topic*/, Short> queues = new ConcurrentHashMap<String, Short>();

	@Override
	public void beforeStart() throws Exception {

		if(ArgumentUtil.isNotNull("fileManager",fileManager) && fileManager.isStarted()) {
			this.offsetFile = fileManager.getOffsetFile();
			this.queueFile = fileManager.getTopicQueueFile();
			this.offsetFileBack =
					new File(this.offsetFile.getParentFile(), this.offsetFile.getName() + DiskFileStoreConfig.BACK_SUFFIX);
			this.queueFileBack = new File(this.queueFile.getParentFile(), this.queueFile.getName() + DiskFileStoreConfig.BACK_SUFFIX);
		}else{
			logger.error("启动offsetManager之前请先初始化并启动fileManager");
		}

	}

	@Override
	public void doStart() throws Exception {

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

	public void setFileManager(FileManager fileManager) {
		this.fileManager = fileManager;
	}

	public short getQueueCount(String topic)throws QueueNotExistException {
		return 0;
	}

	public void setOffsetFile(File offsetFile) {
		this.offsetFile = offsetFile;
	}

	public void setQueueFile(File queueFile) {
		this.queueFile = queueFile;
	}
}

package pers.cy.iris.broker.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.cy.iris.commons.service.Service;
import pers.cy.iris.commons.util.ArgumentUtil;

import java.io.File;

/**
 * @Author:cy
 * @Date:Created in  17/5/13
 * @Destription:
 */
public class FileManager extends Service {

	private static Logger logger = LoggerFactory.getLogger(FileManager.class);

	//数据目录
	private File dataDirectory;
	//日志目录
	private File journalDirectory;
	//队列目录
	private File queueDirectory;
	//复制目录
	private File haDirectory;
	//归档日志目录
	private File archiveDirectory;
	//坏块目录
	private File badFileDirectory;
	//检查点文件
	private File checkPointFile;
	//异常关闭文件
	private File abortFile;
	//消费位置文件
	private File offsetFile;
	//订阅关系文件
	private File subscribeFile;
	//有效队列大小文件
	private File topicQueueFile;
	//锁文件
	private File lockFile;
	//统计文件
	private File statFile;

	// 存储配置
	private DiskFileStoreConfig storeConfig;


	public void setStoreConfig(DiskFileStoreConfig storeConfig) {
		this.storeConfig = storeConfig;
	}

	@Override
	public void beforeStart() throws Exception {
		ArgumentUtil.isNotNull("storeConfig",storeConfig);

		//判断数据存储根目录是否存在，不存在则创建目录
		if(ArgumentUtil.isNotNull("dataDirectory",storeConfig.getDataDirectory())){
			this.dataDirectory = new File(storeConfig.getDataDirectory());
			if (dataDirectory.exists()) {
				if (!dataDirectory.isDirectory()) {
					throw new IllegalArgumentException(String.format("%s is not a directory", dataDirectory.getPath()));
				}
			} else {
				if (!dataDirectory.mkdirs()) {
					if (!dataDirectory.exists()) {
						throw new IllegalArgumentException(
								String.format("create directory %s error.", dataDirectory.getPath()));
					}
				}
			}
		}

		//判断目录是否有读写权限
		if (!dataDirectory.canWrite()) {
			throw new IllegalArgumentException(String.format("%s can not be written", dataDirectory.getPath()));
		}
		if (!dataDirectory.canRead()) {
			throw new IllegalArgumentException(String.format("%s can not be read", dataDirectory.getPath()));
		}

		this.journalDirectory = new File(dataDirectory, "journal/");
		this.queueDirectory = new File(dataDirectory, "queue/");
		this.haDirectory = new File(dataDirectory, "replication/");
		this.archiveDirectory = new File(dataDirectory, "archive/");
		this.badFileDirectory = new File(dataDirectory, "badfile/");

		this.checkPointFile = new File(dataDirectory, "checkpoint");
		this.abortFile = new File(dataDirectory, "abort");
		this.offsetFile = new File(dataDirectory, "offset");
		this.subscribeFile = new File(dataDirectory, "subscribe");
		this.topicQueueFile = new File(dataDirectory, "queues");
		this.lockFile = new File(dataDirectory, "lock");
		this.statFile = new File(dataDirectory, "stat");

		if (!journalDirectory.exists()) {
			journalDirectory.mkdir();
		}
		if (!queueDirectory.exists()) {
			queueDirectory.mkdir();
		}
		if (!haDirectory.exists()) {
			haDirectory.mkdir();
		}
		if (!archiveDirectory.exists()) {
			archiveDirectory.mkdir();
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

	public File getDataDirectory() {
		return dataDirectory;
	}

	public File getJournalDirectory() {
		return journalDirectory;
	}

	public File getQueueDirectory() {
		return queueDirectory;
	}

	public File getHaDirectory() {
		return haDirectory;
	}

	public File getArchiveDirectory() {
		return archiveDirectory;
	}

	public File getBadFileDirectory() {
		return badFileDirectory;
	}

	public File getCheckPointFile() {
		return checkPointFile;
	}

	public File getAbortFile() {
		return abortFile;
	}

	public File getOffsetFile() {
		return offsetFile;
	}

	public File getSubscribeFile() {
		return subscribeFile;
	}

	public File getTopicQueueFile() {
		return topicQueueFile;
	}

	public File getLockFile() {
		return lockFile;
	}

	public File getStatFile() {
		return statFile;
	}
}

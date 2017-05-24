package pers.cy.iris.broker.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.cy.iris.commons.exception.ChecksumException;
import pers.cy.iris.commons.service.Service;
import pers.cy.iris.commons.util.ArgumentUtil;
import pers.cy.iris.commons.util.FileUtil;
import pers.cy.iris.commons.util.SystemClock;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Author:cy
 * @Date:Created in  17/5/24
 * @Destription: 检查点文件，磁盘写双份
 */
public class CheckPoint extends Service{

	private static Logger logger = LoggerFactory.getLogger(CheckPoint.class);

	// 检查点数据大小
	private static final int SIZE = 8 + 8 + 8 + 4;
	// 第二份数据的位置
	private static final int NEXT = 1024;
	// 日志写入偏移量
	protected long journalOffset;
	// 日志恢复起始偏移量
	protected long recoverOffset;
	// 时间戳
	protected long timestamp;
	// 文件
	protected File file;
	protected RandomAccessFile raf;

	// 锁
	protected final Lock writeLock = new ReentrantReadWriteLock().writeLock();

	/**
	 * 构造函数
	 *
	 * @param file 本地存储文件
	 */
	public CheckPoint(File file) {
		this.file = file;
	}


	@Override
	public void beforeStart() throws Exception {

		ArgumentUtil.isNotNull("checkpoint file",file);

		if (!FileUtil.createFile(file)) {
			throw new IOException(String.format("create file error,%s", file.getPath()));
		}
		if (!file.canWrite()) {
			throw new IOException(String.format("file can not be written,%s", file.getPath()));
		}
		if (!file.canRead()) {
			throw new IOException(String.format("file can not be read,%s", file.getPath()));
		}
		if (raf == null) {
			raf = new RandomAccessFile(file, "rw");
		}
	}

	@Override
	public void doStart() throws Exception {
		recover();
		logger.info("checkpoint is started.");
	}

	@Override
	public void afterStart() throws Exception {

	}

	@Override
	public void beforeStop() {

	}

	@Override
	public void doStop() throws Exception {
		doFlush();
		raf.close();
		raf = null;
		logger.info("checkpoint is stopped.");
	}

	/**
	 * 恢复
	 *
	 * @throws Exception
	 */
	protected void recover() throws Exception {
		long length = raf.length();
		if (length >= SIZE) {

			// 第一份数据校验和不同
			if (!validateCheckSum(raf,0)) {
				// 尝试取第二份数据
				if (length >= NEXT + SIZE) {
					if (!validateCheckSum(raf,NEXT)) {
						// 校验和错误
						throw new ChecksumException("checkpoint 两份数据都校验失败");
					}
				} else {
					throw new ChecksumException("checkpoint 首份数据校验失败，第二份数据不存在");
				}

			}
			logger.info(
					String.format("recover checkpoint. recoverOffset:%d,journalOffset:%d,timestamp:%d", recoverOffset,
							journalOffset, timestamp));
		}
	}

	/**
	 * 校验checksum是否一致
	 * @param raf  文件
	 * @param pos  起始位置
	 * @return 校验结果
	 * @throws IOException
	 */
	private boolean validateCheckSum(RandomAccessFile raf,long pos) throws IOException {
		raf.seek(pos);
		recoverOffset = raf.readLong();
		journalOffset = raf.readLong();
		timestamp = raf.readLong();
		int checksum = raf.readInt();
		int value = getCheckSum(journalOffset, recoverOffset, timestamp);
		return checksum == value;
	}

	/**
	 * 写入到磁盘中
	 */
	protected void doFlush() {
		long timestamp = SystemClock.currentTimeMillis();
		long recoverOffset = this.recoverOffset;
		long journalOffset = this.journalOffset;
		int checksum = getCheckSum(journalOffset, recoverOffset, timestamp);
		try {
			// 双写
			raf.seek(0);
			raf.writeLong(recoverOffset);
			raf.writeLong(journalOffset);
			raf.writeLong(timestamp);
			raf.writeInt(checksum);

			raf.seek(NEXT);
			raf.writeLong(recoverOffset);
			raf.writeLong(journalOffset);
			raf.writeLong(timestamp);
			raf.writeInt(checksum);

			raf.getFD().sync();
		} catch (IOException e) {
			logger.error("flush checkpoint error.", e);
		} finally {
			this.timestamp = timestamp;
		}
	}

	/**
	 * 刷盘
	 */
	public void flush() {
		writeLock.lock();
		try {
			if (!isStarted()) {
				return;
			}
			doFlush();
		} finally {
			writeLock.unlock();
		}
	}

	public long getJournalOffset() {
		return journalOffset;
	}

	public void setJournalOffset(long journalOffset) {
		this.journalOffset = journalOffset;
	}

	public long getRecoverOffset() {
		return recoverOffset;
	}

	public void setRecoverOffset(long recoverOffset) {
		this.recoverOffset = recoverOffset;
	}

	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * 获取校验和
	 *
	 * @param journalOffset 日志偏移量
	 * @param recoverOffset 恢复偏移量
	 * @param timestamp     时间戳
	 * @return 校验和
	 */
	protected int getCheckSum(final long journalOffset, final long recoverOffset, final long timestamp) {
		int result = (int) (journalOffset ^ (journalOffset >>> 32));
		result = 31 * result + (int) (recoverOffset ^ (recoverOffset >>> 32));
		result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
		return result;
	}
}

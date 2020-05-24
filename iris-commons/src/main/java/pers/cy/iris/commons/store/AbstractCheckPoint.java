package pers.cy.iris.commons.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.cy.iris.commons.exception.ChecksumException;
import pers.cy.iris.commons.service.Service;
import pers.cy.iris.commons.util.ArgumentUtil;
import pers.cy.iris.commons.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Author:cy
 * @Date:Created in  2020/5/23
 * @Destription:
 */
public abstract class AbstractCheckPoint extends Service {
	private static Logger logger = LoggerFactory.getLogger(AbstractCheckPoint.class);

	// 检查点数据大小
	private int size;
	// 第二份数据的位置
	private static final int NEXT = 1024;
	// 文件
	private File file;
	private RandomAccessFile raf;

	private String path;
	private String fileName;

	// 锁
	protected final Lock writeLock = new ReentrantReadWriteLock().writeLock();

	public AbstractCheckPoint() {
		this.path = System.getProperty("user.home");
		this.fileName = "checkPoint";
	}

	/**
	 * 构造函数
	 *
	 */
	public AbstractCheckPoint(String path,String fileName) {
		this.path = path;
		this.fileName = fileName;
	}

	@Override
	public void beforeStart() throws Exception {

		File path = new File(this.path);
		if (path.exists() && !path.isDirectory()) {
			throw new IllegalStateException("config.persistentPath is not a directory. " + path.getPath());
		}
		if (!path.exists()) {
			if (!path.mkdirs()) {
				if (!path.exists()) {
					throw new IllegalStateException("create directory error. " + path.getPath());
				}
			}
		}

		file = new File(path,fileName);

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
		logger.info(file.getName() + " is recovery.");
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
		logger.info(file.getName() + "is safe closed.");
	}

	/**
	 * 恢复
	 *
	 * @throws Exception
	 */
	protected void recover() throws Exception {
		long length = raf.length();

		if (length == 0) {
			// 首次启动,没有检查点数据
			return;
		}

		// 第一份数据校验和不同
		if (!validateCheckSum(raf, 0)) {
			// 尝试取第二份数据
			if (length >= NEXT + Integer.BYTES + size + Integer.BYTES) {
				if (!validateCheckSum(raf, NEXT + Integer.BYTES + size + Integer.BYTES)) {
					// 校验和错误
					throw new ChecksumException("checkpoint 两份数据都校验失败");
				}
			} else {
				throw new ChecksumException("checkpoint 首份数据校验失败，第二份数据不存在");
			}

		}
	}

	/**
	 * 校验checksum是否一致
	 *
	 * @param raf 文件
	 * @param pos 起始位置
	 * @return 校验结果
	 * @throws IOException
	 */
	private boolean validateCheckSum(RandomAccessFile raf, long pos) throws IOException {
		raf.seek(pos);

		size = raf.readInt();
		byte[] data = new byte[size];
		raf.read(data);
		doRecover(data);
		int checksum = raf.readInt();
		int value = getCheckSum();
		return checksum == value;
	}

	protected abstract int getCheckSum();

	protected abstract void doRecover(byte[] data);

	/**
	 * 写入到磁盘中
	 */
	protected void doFlush() {
		byte[] data = encode();
		int checksum = getCheckSum();
		try {
			// 双写
			raf.seek(0);
			raf.writeInt(data.length);
			raf.write(data);
			raf.writeInt(checksum);

			raf.seek(NEXT + Integer.BYTES + data.length + Integer.BYTES);
			raf.writeInt(data.length);
			raf.write(data);
			raf.writeInt(checksum);

			raf.getFD().sync();
		} catch (IOException e) {
			logger.error("flush checkpoint error.", e);
		}
	}

	protected abstract byte[] encode();

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

	public File getFile() {
		return file;
	}
}

package pers.cy.iris.broker.store.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Author:cy
 * @Date:Created in  18/5/10
 * @Destription: channel 文件读写类，不支持2G以上文件
 */
public class ChannelFile implements FileStream{

	private static Logger logger = LoggerFactory.getLogger(ChannelFile.class);

	// 待操作的文件
	private File file;

	// 文件通道
	private FileChannel channel;

	//文件的当前写入位置,不包括文件头
	private int position;

	//文件可写入的长度，不包括文件头
	private int length;

	//文件头大小，默认为0即不存在文件头
	private int headSize ;

	public ChannelFile(File file) {
		this.file = file;
		this.position = 0;
		initHeader();
		this.length = (int)file.length() - headSize;
		try {
			this.channel = new RandomAccessFile(file,"rw").getChannel();
		} catch (FileNotFoundException e) {
			logger.error("file {} is not found.",file.getName());
		}
	}

	@Override
	public int append(final ByteBuffer buffer) throws IOException {
		if (buffer == null || !buffer.hasRemaining()) {
			return 0;
		}
		int size = buffer.remaining();
		if (remaining() < size) {
			throw new IllegalArgumentException(
					String.format("write file exceed ，fileLength:%d,position:%d,bufferSize:%d", length, position, size));
		}
		// 不能确保一次写入多少数据，需要循环
		int len = 0;

		while (buffer.hasRemaining()) {
			len += channel.write(buffer);
		}

		position += len;
		return len;
	}

	/**
	 * 初始化文件头部
	 */
	protected void initHeader(){
		this.headSize = 0;
	}

	private int remaining(){
		return length - position;
	}

	@Override
	public ByteBuffer read(int position, int size) throws IOException {
		return null;
	}

	@Override
	public int position() {
		return this.position;
	}

	@Override
	public void position(final int position) throws IOException {
		if (position < 0 || position > length) {
			throw new IllegalArgumentException("position:" + position + " must be in [0," + length + "]");
		}
		channel.position(position + headSize);
		this.position = position;
	}

	@Override
	public void flush() {
		try {
			channel.force(true);
		} catch (IOException e) {
			logger.error("flush error.",e);
		}
	}


	@Override
	public void close() {
		try {
			channel.close();
		} catch (IOException e) {
			logger.error("channel close failed.",e);
		}
	}
}

package pers.cy.iris.broker.store.file;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @Author:cy
 * @Date:Created in  18/5/10
 * @Destription:
 */
public interface FileStream extends Closeable{

	int append(final ByteBuffer buffer) throws IOException;

	ByteBuffer read(final int position, final int size) throws IOException ;

	/**
	 * 获取当前写入位置
	 *
	 * @return 当前位置
	 * @throws IOException
	 */
	int position();

	/**
	 * 设置当前写入位置，超过的部分进行删除
	 * @param position
	 * @throws IOException
	 */
	void position(int position) throws IOException;

	void flush();

	/**
	 * 无需抛出异常的close
	 * 关闭资源失败打印日志
	 */
	@Override
	void close();
}

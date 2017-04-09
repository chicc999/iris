package pers.cy.iris.commons.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.*;

/**
 * @Author:cy
 * @Date:Created in  17/4/9
 * @Destription:
 */
public class ZipUtil {

	/**
	 * 压缩
	 *
	 * @param src
	 * @param out
	 * @throws IOException
	 */
	public static void compress(final String src, final OutputStream out) throws IOException {
		if (src == null) {
			return;
		}
		compress(src.getBytes(), out);
	}

	/**
	 * 压缩
	 *
	 * @param src 源
	 * @param out 输出流
	 * @throws IOException
	 */
	public static void compress(final byte[] src, final OutputStream out) throws IOException {
		if (src == null || src.length == 0 || out == null) {
			return;
		}
		ZipOutputStream zout = new ZipOutputStream(out);
		try {
			zout.putNextEntry(new ZipEntry("0"));
			zout.write(src);
			zout.closeEntry();
		} finally {
			if(zout != null){
				zout.close();
			}
		}
	}

	/**
	 * 压缩
	 *
	 * @param src 字符串
	 * @return 二进制
	 * @throws IOException
	 */
	public static byte[] compress(final String src) throws IOException {
		if (src == null) {
			return null;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			compress(src, out);
			return out.toByteArray();
		} finally {
			if(out != null){
				out.close();
			}
		}
	}

	/**
	 * 压缩
	 *
	 * @param src
	 * @return
	 * @throws IOException
	 */
	public static byte[] compress(final byte[] src) throws IOException {
		if (src == null) {
			return null;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			compress(src, out);
			return out.toByteArray();
		} finally {
			if(out != null){
				out.close();
			}
		}
	}

	/**
	 * 解压缩
	 *
	 * @param buf
	 * @return
	 * @throws IOException
	 */
	public static String decompress(final byte[] buf) throws IOException {
		if (buf == null || buf.length == 0) {
			return null;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ZipInputStream zin = new ZipInputStream(new ByteArrayInputStream(buf));
		try {
			zin.getNextEntry();
			byte[] buffer = new byte[1024];
			int offset = -1;
			while ((offset = zin.read(buffer)) != -1) {
				out.write(buffer, 0, offset);
			}
			return out.toString();
		} finally {
			if(zin != null){
				zin.close();
			}
			if(out != null){
				out.close();
			}
		}
	}

	/**
	 * 按照Zlib格式压缩
	 *
	 * @param src
	 * @param offset
	 * @param size
	 * @return
	 * @throws IOException
	 */
	public static byte[] compressByZlib(byte[] src, int offset, int size) throws IOException {
		if (src == null) {
			return null;
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream(size);

		Deflater deflater = new Deflater();
		deflater.reset();
		deflater.setInput(src, offset, size);
		deflater.finish();
		int len;
		byte[] block = new byte[1024];
		try {
			while (!deflater.finished()) {
				len = deflater.deflate(block);
				bos.write(block, 0, len);
			}
			return bos.toByteArray();
		} finally {
			deflater.end();
			if(bos != null){
				bos.close();
			}
		}

	}

	/**
	 * 按Zlib格式解压缩
	 *
	 * @param buf
	 * @param offset
	 * @param size
	 * @return
	 * @throws IOException
	 */
	public static byte[] decompressByZlib(byte[] buf, int offset, int size) throws IOException {
		if (buf == null || buf.length == 0 || size <= 0 || offset >= buf.length) {
			return null;
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream(size * 4);
		Inflater inflater = new Inflater();
		inflater.reset();
		inflater.setInput(buf, offset, size);
		int len;
		byte[] block = new byte[1024];
		try {
			while (!inflater.finished()) {
				len = inflater.inflate(block);
				bos.write(block, 0, len);
			}
			return bos.toByteArray();
		} catch (DataFormatException e) {
			throw new IOException(e);
		} finally {
			inflater.end();
			if(bos != null){
				bos.close();
			}
		}
	}


}

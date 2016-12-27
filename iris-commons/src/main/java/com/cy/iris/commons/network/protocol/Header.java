package com.cy.iris.commons.network.protocol;

import com.cy.iris.commons.util.ArgumentUtil;
import io.netty.buffer.ByteBuf;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by cy on 16/12/27.
 */
public class Header {

	public static final int HEADER_SIZE = 4 + 1 + 1 + 4 + 1 + 8;//wyzhangpeng1:length(4) + version(1) + 标识字段(1字节) + requestId(4) + type(1) + time(8)
	private static final AtomicInteger REQUEST_ID = new AtomicInteger(0);
	// 头类型, 可用的类型包括请求和响应
	private HeaderType headerType;
	// 协议版本号
	private byte version = 1;
	// 命令类型
	private int type;
	// 应答方式
	private volatile boolean isNeedAck = true;
	// 总长度
	private int length;
	// 请求ID
	private String requestId;
	// 请求或响应时间
	private long time;
	// 响应状态码
	private int status;
	// 响应错误信息
	private String error;

	public Header() {

	}

	public void encode(final ByteBuf out) throws Exception {
		ArgumentUtil.isNotNull("Header encode ByteBuf",out);

		// 4个字节总长度，先占位
		out.writeInt(0);
		// 1个字节的数据版本
		out.writeByte(version);

	}

	public void decode(final ByteBuf in) throws Exception {

	}
}

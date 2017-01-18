package com.cy.iris.commons.network.protocol;

import com.cy.iris.commons.util.ArgumentUtil;
import com.cy.iris.commons.util.Serializer;
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
	private volatile Acknowledge acknowledge = Acknowledge.ACK_FLUSH;

	// 请求ID
	private int requestId;
	// 请求或响应时间
	private long time;
	// 响应状态码
	private int status;
	// 响应错误信息
	private String error;

	/**--不进行序列化的字段--*/
	private String typeString;
	// 总长度
	private int length;

	public Header() {
	}

	public Header(HeaderType headerType, int type) {
		this.headerType = headerType;
		this.type = type;
		if(this.headerType == HeaderType.REQUEST) {
			this.requestId = REQUEST_ID.incrementAndGet();
		}
	}

	public int getType() {
		return type;
	}

	public Header Type(int type) {
		this.type = type;
		return this;
	}

	public int getRequestId() {
		return requestId;
	}

	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}

	public HeaderType getHeaderType() {
		return headerType;
	}

	public Header HeaderType(HeaderType headerType) {
		this.headerType = headerType;
		return this;
	}

	public ByteBuf encode(final ByteBuf out) throws Exception {
		ArgumentUtil.isNotNull("Header encode ByteBuf",out);

		// 1个字节的数据版本
		out.writeByte(version);

		// 1个字节标识字段
		int identity = (byte) ((headerType.ordinal() & 0x1) | ((acknowledge.ordinal()) << 1 & 0x6));
		out.writeByte(identity);

		// 4个字节请求ID
		out.writeInt(requestId);
		// 1个字节命令类型
		out.writeByte(type);
		// 8个字节发送时间
		out.writeLong(time);

		// 如果为响应类型，需写入响应状态吗和错误消息
		if (headerType == HeaderType.RESPONSE) {
			// 1个字节的状态码
			out.writeByte(status);
			// 2个字节的异常长度
			// 异常信息
			Serializer.write(error, out);
		}

		return out;

	}

	public Header decode(final ByteBuf in) throws Exception {
		ArgumentUtil.isNotNull("header decoder ByteBuf",in);

		// 1个字节的数据版本
		version = in.readByte();
		// 1个字节标识字段 (其中1-1：请求应答标识; 2-3:应答类型; 4-5:事务类型; 6-8:其它)
		int flag = in.readByte();
		headerType = HeaderType.valueOf(flag & 0x1);
		// 应答类型
		acknowledge = Acknowledge.valueOf((flag >> 1) & 0x3);
		// 4个字节请求ID
		requestId = in.readInt();
		// 1个字节命令类型
		type = in.readByte();
		// 8个字节发送时间
		time = in.readLong();
		// 如果为响应类型，需读响应状态吗和错误消息
		if (headerType == HeaderType.RESPONSE) {
			// 1个字节的状态码
			status = in.readByte();
			// 2个字节的异常长度
			// 异常信息
			error = Serializer.read(in);
		}

		return this;
	}


	@Override
	public String toString() {
		return "Header{" +
				"headerType=" + headerType +
				", version=" + version +
				", type=" + type +
				", acknowledge=" + acknowledge +
				", length=" + length +
				", requestId=" + requestId +
				", time=" + time +
				", status=" + status +
				", error='" + error + '\'' +
				", typeString='" + typeString + '\'' +
				'}';
	}

	public  String getTypeString(){
		return this.typeString;
	}

	public Header typeString(String typeString){
		this.typeString = typeString;
		return this;
	}

}

package com.cy.iris.commons.network.protocol;

import com.cy.iris.commons.util.ArgumentUtil;
import io.netty.buffer.ByteBuf;

import java.util.UUID;
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
	private String typeString;
	// 应答方式
	private volatile boolean isNeedAck = true;
	// 请求ID
	private int requestId = 0;

	// 响应状态码
	private int status;
	// 响应错误信息
	private String error;

	//接收到请求的时间
	private long receiveTime;

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
		// command是请求还是响应
		out.writeInt(headerType.ordinal());
		// 具体command类型
		out.writeInt(type);
		// requestID
		out.writeInt(requestId);

		return out;

	}

	public Header decode(final ByteBuf in) throws Exception {
		ArgumentUtil.isNotNull("header decoder ByteBuf",in);
		//decode时间作为接收时间
		this.receiveTime = System.currentTimeMillis();

		this.version = in.readByte();
		this.headerType = HeaderType.valueOf(in.readInt());
		this.type = in.readInt();
		this.requestId = in.readInt();

		return this;
	}


	@Override
	public String toString() {
		return "Header{" +
				"headerType=" + headerType +
				", version=" + version +
				", type=" + type +
				", typeString='" + typeString + '\'' +
				", isNeedAck=" + isNeedAck +
				", requestId=" + requestId +
				", status=" + status +
				", error='" + error + '\'' +
				", receiveTime=" + receiveTime +
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

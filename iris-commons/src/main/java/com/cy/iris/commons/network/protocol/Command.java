package com.cy.iris.commons.network.protocol;

import java.nio.ByteBuffer;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 命令格式
 * +-------+------------+--------+------+
 * | 总长度 | header长度 | header | body |
 * +-------+------------+--------+------+
 */
public abstract class Command {
	protected String requestId;
	private ConcurrentHashMap<String,String> header = new ConcurrentHashMap<String, String>();

	protected Command() {
		this.requestId = UUID.randomUUID().toString();
	}

	protected Command(String requestId) {
		this.requestId = requestId;
		header.put("",requestId);
	}

	public final String getRequestId() {
		return requestId;
	}

	public final void setHeader(String key, String val){
		this.header.put(key,val);
	}

	abstract ByteBuffer encode();
}

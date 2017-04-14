package pers.cy.iris.commons.model.message;

import pers.cy.iris.commons.util.NetUtil;

import java.util.Arrays;

/**
 * @Author:cy
 * @Date:Created in  17/4/14
 * @Destription: 消息ID
 */
public class MessageId {

	// 地址
	private byte[] address;
	// 偏移量
	private long offset;
	// 消息ID
	private String messageId;

	/**
	 * @param messageId
	 */
	public MessageId(String messageId) {
		if (messageId == null || messageId.isEmpty()) {
			throw new IllegalArgumentException("messageId is invalid");
		}
		int pos = messageId.indexOf('-');
		if (pos < 0) {
			throw new IllegalArgumentException("messageId is invalid");
		}
		String[] parts = new String[2];
		parts[0] = messageId.substring(0, pos);
		parts[1] = messageId.substring(pos + 1);
		if (parts[0] == null || parts[0].isEmpty() || parts[1] == null || parts[1].isEmpty()) {
			throw new IllegalArgumentException("messageId is invalid");
		}
		offset = Long.valueOf(parts[1]);
		if (offset < 0) {
			throw new IllegalArgumentException("messageId is invalid");
		}
		address = NetUtil.toByteByHex(parts[0]);
	}

	/**
	 * @param address
	 * @param offset
	 */
	public MessageId(byte[] address, long offset) {
		if (offset < 0) {
			throw new IllegalArgumentException("offset is invalid");
		}
		if (address == null || address.length < 4) {
			throw new IllegalArgumentException("address is invalid");
		}
		this.address = address;
		this.offset = offset;
	}

	public byte[] getAddress() {
		return this.address;
	}

	public long getOffset() {
		return this.offset;
	}

	public String getMessageId() {
		if (messageId == null && address != null) {
			StringBuilder sb = new StringBuilder();
			NetUtil.appendHex(address, sb);
			sb.append('-').append(offset);
			messageId = sb.toString();
		}
		return this.messageId;
	}

	public String toString() {
		return getMessageId();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		MessageId messageId1 = (MessageId) o;

		if (offset != messageId1.offset) {
			return false;
		}
		if (!Arrays.equals(address, messageId1.address)) {
			return false;
		}
		if (messageId != null ? !messageId.equals(messageId1.messageId) : messageId1.messageId != null) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = address != null ? Arrays.hashCode(address) : 0;
		result = 31 * result + (int) (offset ^ (offset >>> 32));
		result = 31 * result + (messageId != null ? messageId.hashCode() : 0);
		return result;
	}
}

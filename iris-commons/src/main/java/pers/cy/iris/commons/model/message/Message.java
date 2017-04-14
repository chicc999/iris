package pers.cy.iris.commons.model.message;

import io.netty.buffer.ByteBuf;
import pers.cy.iris.commons.util.ArgumentUtil;
import pers.cy.iris.commons.util.CharacterSetUtil;
import pers.cy.iris.commons.util.Serializer;
import pers.cy.iris.commons.util.ZipUtil;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Adler32;
import java.util.zip.Checksum;

/**
 * @Author:cy
 * @Date:Created in  17/4/9
 * @Destription: 消息
 */
public class Message implements Serializable {

	public static final String TAGS = "TAGS";
	public static final int COMPRESS_THRESHOLD = 100;
	public static String EXPIRE = "EXPIRE";
	// 队列ID
	protected short queueId;
	// 是否压缩
	protected boolean compressed;
	// 主题
	protected String topic;
	// 标签
	protected short flag;
	// 应用
	protected String app;
	// 业务ID
	protected String businessId;
	// 优先级
	protected byte priority;
	// 顺序消息
	protected boolean ordered;

	// 消息体校验码
	protected long bodyCRC;
	// 消息体
	protected byte[] body;
	// 属性
	protected Map<String, String> attributes;
	// 发送时间
	protected long sendTime;

	public Message() {
	}

	/**
	 * 构造函数
	 *
	 * @param topic      主题
	 * @param text       文本
	 * @param businessId 业务ID
	 */
	public Message(String topic, String text, String businessId) {
		setTopic(topic);
		setBusinessId(businessId);
		try {
			setBody(text.getBytes("utf-8"));
		} catch (UnsupportedEncodingException ignore) {
		}
	}

	/**
	 * 构造函数
	 *
	 * @param topic      主题
	 * @param content    消息内容
	 * @param businessId 业务ID
	 */
	public Message(String topic, byte[] content, String businessId) {
		setTopic(topic);
		setBusinessId(businessId);
		setBody(content);
	}

	public Message topic(final String topic) {
		setTopic(topic);
		return this;
	}

	public Message flag(final short flag) {
		setFlag(flag);
		return this;
	}

	public Message app(final String app) {
		setApp(app);
		return this;
	}

	public Message businessId(final String businessId) {
		setBusinessId(businessId);
		return this;
	}

	public Message priority(final byte priority) {
		setPriority(priority);
		return this;
	}

	public Message ordered(final boolean ordered) {
		setOrdered(ordered);
		return this;
	}

	public Message body(final byte[] data) {
		setBody(data);
		return this;
	}

	public Message attribute(final String key, final String value) {
		setAttribute(key, value);
		return this;
	}

	public Message attributes(final Map<String, String> attributes) {
		setAttributes(attributes);
		return this;
	}

	public Message queueId(final short queueId){
		setQueueId(queueId);
		return this;
	}

	public String getTopic() {
		return this.topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public short getFlag() {
		return this.flag;
	}

	public void setFlag(short flag) {
		this.flag = flag;
	}

	public String getApp() {
		return this.app;
	}

	public void setApp(String app) {
		this.app = app;
	}

	public String getBusinessId() {
		return this.businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public byte getPriority() {
		return this.priority;
	}

	public void setPriority(byte priority) {
		this.priority = priority;
	}

	public boolean isOrdered() {
		return this.ordered;
	}

	public void setOrdered(boolean ordered) {
		this.ordered = ordered;
	}

	public boolean isCompressed() {
		return this.compressed;
	}

	public void setCompressed(boolean compressed) {
		this.compressed = compressed;
	}


	public long getSendTime() {
		return this.sendTime;
	}

	public void setSendTime(long sendTime) {
		this.sendTime = sendTime;
	}

	public short getQueueId() {
		return this.queueId;
	}

	public void setQueueId(short queueId) {
		this.queueId = queueId;
	}

	public long getBodyCRC() {
		if (bodyCRC == 0) {
			if (body != null && body.length > 0) {
				Checksum checksum = new Adler32();
				checksum.update(body,0,body.length);
				bodyCRC = checksum.getValue();
			}
		}
		return bodyCRC;
	}

	public void setBodyCRC(long bodyCRC) {
		this.bodyCRC = bodyCRC;
	}

	public byte[] getBody(){
		if ( body != null) {
			try {
				if (compressed) {
					return ZipUtil.decompressByZlib(body,0,body.length);
				} else {
					return body;
				}
			} catch (UnsupportedEncodingException e) {
				throw new IllegalStateException("decode body error.",e);
			} catch (IOException e) {
				throw new IllegalStateException("decompress body error.",e);
			}
		}else {
			throw new IllegalStateException("message's body is null");
		}
	}

	public void setBody(byte[] body) {
		if (body.length >= COMPRESS_THRESHOLD) {
			try {
				this.body = ZipUtil.compressByZlib(body, 0, body.length);
				this.compressed = true;
			} catch (IOException ignored) {
			}
		}else {
			this.body = body;
		}
	}

	public Map<String, String> getAttributes() {
		return this.attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	public String getAttribute(String key) {
		if (attributes == null) {
			return null;
		}
		return attributes.get(key);
	}

	public void setAttribute(String key, String value) {
		if (attributes == null) {
			attributes = new HashMap<String, String>();
		}
		attributes.put(key, value);
	}

	public String getTags() {
		return getAttribute(TAGS);
	}

	public void setTags(String tags) {
		setAttribute(TAGS, tags);
	}

	public long getExpire() {
		String value = getAttribute(EXPIRE);
		if (value == null) {
			return 0;
		}
		return Long.valueOf(value);
	}

	public void setExpire(long expire) {
		setAttribute(EXPIRE, String.valueOf(expire));
	}

	public int getSize() {
		if (body != null) {
			return body.length;
		}else {
			return 0;
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Message message = (Message) o;

		if (bodyCRC != message.bodyCRC) {
			return false;
		}
		if (compressed != message.compressed) {
			return false;
		}
		if (flag != message.flag) {
			return false;
		}
		if (ordered != message.ordered) {
			return false;
		}
		if (priority != message.priority) {
			return false;
		}
		if (app != null ? !app.equals(message.app) : message.app != null) {
			return false;
		}
		if (attributes != null ? !attributes.equals(message.attributes) : message.attributes != null) {
			return false;
		}
		if (body != null ? !body.equals(message.body) : message.body != null) {
			return false;
		}
		if (businessId != null ? !businessId.equals(message.businessId) : message.businessId != null) {
			return false;
		}
		if (topic != null ? !topic.equals(message.topic) : message.topic != null) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = (compressed ? 1 : 0);
		result = 31 * result + (topic != null ? topic.hashCode() : 0);
		result = 31 * result + (int) flag;
		result = 31 * result + (app != null ? app.hashCode() : 0);
		result = 31 * result + (businessId != null ? businessId.hashCode() : 0);
		result = 31 * result + (int) priority;
		result = 31 * result + (ordered ? 1 : 0);
		result = 31 * result + (int) (bodyCRC ^ (bodyCRC >>> 32));
		result = 31 * result + (body != null ? body.hashCode() : 0);
		result = 31 * result + (attributes != null ? attributes.hashCode() : 0);
		return result;
	}

	/**
	 * 消息序列化方法
	 * @param out 序列化到此buf中
	 */
	public void encode(final ByteBuf out){

		ArgumentUtil.isNotNull("encode byteBuf",out);

		// 1字节系统字段,由低位起 1-1:消息体压缩标识 2-2:顺序消息 3-8:其它
		short sysCode = (short) (this.isCompressed() ? 1 : 0);
		sysCode |= ((this.isOrdered() ? 1 : 0) << 1) & 0x3;
		out.writeByte(sysCode);

		// 2字节业务标签
		out.writeShort(this.getFlag());
		// 1字节优先级
		out.writeByte(this.getPriority());
		// 4字节消息体CRC
		out.writeLong(this.getBodyCRC());
		// 发送时间
		out.writeLong(this.getSendTime());
		// 4字节消息体大小
		// 消息体
		out.writeInt(body.length);
		out.writeBytes(body);
		// 1字节主题长度
		// 主题
		Serializer.writeByteString(this.getTopic(), out);
		// 1字节应用长度
		// 应用
		Serializer.writeByteString(this.getApp(), out);

		// 1字节业务ID长度
		// 业务ID
		Serializer.writeByteString(this.getBusinessId(), out);
		// 4字节属性长度
		// 属性 （以属性文件格式存储）
		Serializer.writeIntString(toProperties(this.getAttributes()), out);
	}

	/**
	 * 把Map转换成Properties字符串
	 *
	 * @param attributes 散列
	 * @return 字符串
	 */
	protected static String toProperties(final Map<String, String> attributes) {
		if (attributes == null) {
			return "";
		}
		int count = 0;
		StringBuilder builder = new StringBuilder(100);
		for (Map.Entry<String, String> entry : attributes.entrySet()) {
			if (count > 0) {
				builder.append('\n');
			}
			CharacterSetUtil.appendUnicode(builder,entry.getKey(),true);
			builder.append('=');
			CharacterSetUtil.appendUnicode(builder,entry.getKey(),false);
			count++;
		}
		return builder.toString();
	}

}
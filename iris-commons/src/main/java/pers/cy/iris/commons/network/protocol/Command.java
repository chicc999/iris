package pers.cy.iris.commons.network.protocol;

import pers.cy.iris.commons.network.CommandCallback;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFutureListener;

/**
 * 命令格式
 * +--------+------+
 * | header | body |
 * +--------+------+
 */
public abstract class Command {

	// 发送消息
	public static final int PUT_MESSAGE = 1;
	// 取消息
	public static final int GET_MESSAGE = 2;
	// 取消息应答
	public static final int GET_MESSAGE_ACK = 102;
	// 消费应答消息
	public static final int ACK_MESSAGE = 3;
	// 重试消息
	public static final int RETRY_MESSAGE = 4;
	// 存放重试消息
	public static final int PUT_RETRY = 5;
	// 获取重试消息
	public static final int GET_RETRY = 6;
	// 获取重试消息应答
	public static final int GET_RETRY_ACK = 106;
	// 更新重试消息
	public static final int UPDATE_RETRY = 7;
	// 获取重试条数
	public static final int GET_RETRY_COUNT = 8;
	// 获取重试条数应答
	public static final int GET_RETRY_COUNT_ACK = 108;
	// 事务准备
	public static final int PREPARE = 10;
	// 事务提交
	public static final int COMMIT = 11;
	// 事务回滚
	public static final int ROLLBACK = 12;
	// 心跳
	public static final int HEARTBEAT = 30;
	// 获取集群
	public static final int GET_CLUSTER = 31;
	// 获取集群应答
	public static final int GET_CLUSTER_ACK = 131;
	// 获取生产健康状况
	public static final int GET_PRODUCER_HEALTH = 32;
	// 获取消费健康状况
	public static final int GET_CONSUMER_HEALTH = 37;
	// 增加连接
	public static final int ADD_CONNECTION = 33;
	// 删除连接
	public static final int REMOVE_CONNECTION = 133;
	// 增加生产者
	public static final int ADD_PRODUCER = 34;
	// 增加生产者
	public static final int REMOVE_PRODUCER = 134;
	// 删除消费者
	public static final int ADD_CONSUMER = 35;
	// 删除消费者
	public static final int REMOVE_CONSUMER = 135;
	// 客户端性能
	public static final int CLIENT_PROFILE = 36;
	// 客户端性能应答
	public static final int CLIENT_PROFILE_ACK = 136;
	// 复制身份
	public static final int IDENTITY = 50;
	// 获取复制偏移量
	public static final int GET_OFFSET = 51;
	// 获取复制偏移量应答
	public static final int GET_OFFSET_ACK = 151;
	// 获取复制日志
	public static final int GET_JOURNAL = 52;
	// 获取复制日志应答
	public static final int GET_JOURNAL_ACK = 152;
	// 复制日志
	public static final int UPDATE_JOURNAL = 53;
	// 复制日志应答
	public static final int UPDATE_JOURNAL_ACK = 153;
	// Slave在线命令
	public static final int ONLINE = 54;
	// 投票选举
	public static final int REQUEST_VOTE = 55;
	// 选举结果
	public static final int REQUEST_VOTE_ACK = 151;
	// 追加日志
	public static final int APPEND_ENTRIES= 57;
	// 响应追加日志
	public static final int APPEND_ENTRIES_ACK = 157;
	// 同步消费位置
	public static final int GET_CONSUMER_OFFSET = 58;
	// 同步消费位置确认
	public static final int GET_CONSUMER_OFFSET_ACK = 158;
	// 获取checksum
	public static final int GET_CHECKSUM = 60;
	// 获取checksum应答
	public static final int GET_CHECKSUM_ACK = 160;
	// 获取chunk
	public static final int GET_CHUNK = 61;
	// 获取chunk的应答
	public static final int GET_CHUNK_ACK = 161;
	// 获取元数据
	public static final int GET_META_DATA = 62;
	// 获取元数据应答
	public static final int GET_META_DATA_ACK = 162;
	//下发Agent的系统指令
	public static final int SYSTEM_COMMAND = 83;

	// 布尔应答
	public static final int BOOLEAN_ACK = 100;

	// 头部
	protected Header header;

	// 执行成功或失败以后的回调
	protected CommandCallback callback;

	//写网络成功或者失败以后的回调
	protected ChannelFutureListener listenner;

	protected Command() {
		this.header = new Header();
	}

	public Command(Header header) {
		this.header = header;
	}

	public ByteBuf encodeHeader(ByteBuf out) throws Exception {

		//header序列化
		return header.encode(out);

	}

	public Header getHeader() {
		return header;
	}

	public int getRequestId() {
		return header.getRequestId();
	}

	public CommandCallback getCallback() {
		return callback;
	}

	public void setCallback(CommandCallback callback) {
		this.callback = callback;
	}

	public ChannelFutureListener getListenner() {
		return listenner;
	}

	public void setListenner(ChannelFutureListener listenner) {
		this.listenner = listenner;
	}

	protected abstract ByteBuf encodeBody(ByteBuf out) throws Exception;

	protected abstract void decodeBody(ByteBuf in) throws Exception;

	@Override
	public String toString() {
		return this.header.toString();
	}

}

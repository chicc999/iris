package pers.cy.iris.commons.network.netty.session;

import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author:cy
 * @Date:Created in  17/4/12
 * @Destription: 连接ID ([clientID]-[seq])
 */
public class ConnectionId {
	public static AtomicLong CONNECTION_ID = new AtomicLong(0);

	private ClientId clientId;
	private long sequence;
	private String connectionId;

	/**
	 * 构造函数
	 *
	 * @param clientId 客户端ID
	 */
	public ConnectionId(final ClientId clientId) {
		setup(clientId, 0);
	}

	/**
	 * 构造函数
	 *
	 * @param clientId 客户端ID
	 * @param sequence 序号
	 */
	public ConnectionId(final ClientId clientId, final long sequence) {
		setup(clientId, sequence);
	}

	/**
	 * 构造函数
	 *
	 * @param connectionId 字符串表示
	 */
	public ConnectionId(final String connectionId) {
		if (connectionId == null || connectionId.isEmpty()) {
			throw new IllegalArgumentException("connectionId can not be empty");
		}
		String[] parts = new String[]{null, null, null, null, null};
		int index = 0;
		StringTokenizer tokenizer = new StringTokenizer(connectionId, "-");
		while (tokenizer.hasMoreTokens()) {
			parts[index++] = tokenizer.nextToken();
			if (index >= parts.length) {
				break;
			}
		}
		if (index < parts.length) {
			throw new IllegalArgumentException("connectionId is invalid.");
		}
		setup(new ClientId(parts), Long.parseLong(parts[parts.length - 1]));
	}

	/**
	 * 构造函数
	 *
	 * @param parts 字符串分割
	 */
	public ConnectionId(final String[] parts) {
		setup(new ClientId(parts), Long.parseLong(parts[4]));
	}

	/**
	 * 初始化
	 *
	 * @param clientId 客户端ID
	 * @param sequence 序号
	 */
	protected void setup(final ClientId clientId, final long sequence) {
		if (clientId == null) {
			throw new IllegalArgumentException("clientId must not be null");
		}
		long seq = sequence;
		if (seq <= 0) {
			seq = CONNECTION_ID.incrementAndGet();
		}
		this.clientId = clientId;
		this.sequence = seq;

		// 在构造函数中创建，防止延迟加载并发问题
		this.connectionId = clientId.getClientId() + "-" + seq;
	}

	public ClientId getClientId() {
		return this.clientId;
	}

	public long getSequence() {
		return this.sequence;
	}

	public String getConnectionId() {
		return connectionId;
	}

	/**
	 * 增加序号
	 */
	public long incrSequence() {
		return CONNECTION_ID.incrementAndGet();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		ConnectionId that = (ConnectionId) o;

		if (sequence != that.sequence) {
			return false;
		}
		if (clientId != null ? !clientId.equals(that.clientId) : that.clientId != null) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = clientId != null ? clientId.hashCode() : 0;
		result = 31 * result + (int) (sequence ^ (sequence >>> 32));
		return result;
	}

	public String toString() {
		return getConnectionId();
	}

}

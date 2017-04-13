package pers.cy.iris.commons.network.netty.session;

import pers.cy.iris.commons.util.ArgumentUtil;

import java.util.StringTokenizer;

/**
 * @Author:cy
 * @Date:Created in  17/4/6
 * @Destription: 客户端ID ([version]-[ip]-[time]-[pid])
 */
public class ClientId {
	//客户端版本
	private String version;
	// 16进制IP地址
	private String ip;
	// 时间戳
	private long time;
	// 序号
	private long sequence;
	// 客户端ID
	private String clientId;

	/**
	 * 构造函数
	 *
	 * @param clientId 字符串表示
	 */
	public ClientId(final String clientId) {
		if (clientId == null || clientId.isEmpty()) {
			throw new IllegalArgumentException("clientId can not be empty");
		}
		String[] parts = new String[]{null, null, null, null};
		int index = 0;
		StringTokenizer tokenizer = new StringTokenizer(clientId, "-");
		while (tokenizer.hasMoreTokens()) {
			parts[index++] = tokenizer.nextToken();
			if (index >= parts.length) {
				break;
			}
		}
		if (index < parts.length) {
			throw new IllegalArgumentException("clientId is invalid.");
		}
		setup(parts[0], parts[1], Long.parseLong(parts[2]), Long.parseLong(parts[3]));
	}

	/**
	 * 构造函数
	 *
	 * @param parts 字符串分割
	 */
	public ClientId(final String[] parts) {
		if (parts == null || parts.length < 4) {
			throw new IllegalArgumentException("parts is invalid.");
		}
		setup(parts[0], parts[1], Long.parseLong(parts[2]), Long.parseLong(parts[3]));
	}

	/**
	 * 构造函数
	 *
	 * @param version 版本
	 * @param ip      IP
	 * @param time    时间戳
	 */
	public ClientId(final String version, final String ip, final long time) {
		setup(version, ip, time, 0);
	}

	/**
	 * 构造函数
	 *
	 * @param version   版本号
	 * @param ip        IP
	 * @param time      时间戳
	 * @param processId 随机数或进程号
	 */
	public ClientId(final String version, final String ip, final long time, final long processId) {
		setup(version, ip, time, processId);
	}

	/**
	 * 初始化
	 *
	 * @param version   版本号
	 * @param ip        IP
	 * @param time      时间戳
	 * @param processId 随机数或进程号
	 */
	protected void setup(final String version, final String ip, final long time, final long processId) {
		ArgumentUtil.isNotBlank(version);
		ArgumentUtil.isNotBlank(ip);

		this.version = version;
		this.ip = ip;
		this.time = time;
		long pid = processId;
		if (pid <= 0) {
			// 产生正整数
			pid = (long) (Math.random() * 65534 + 1);
		}
		this.sequence = pid;
		// 在构造函数中创建，防止延迟加载并发问题
		StringBuilder builder = new StringBuilder();
		builder.append(version).append('-').append(ip).append('-').append(time).append('-').append(sequence);
		this.clientId = builder.toString();
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getIp() {
		return this.ip;
	}

	public long getTime() {
		return this.time;
	}

	public long getSequence() {
		return this.sequence;
	}

	public String getClientId() {
		return clientId;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		ClientId clientId = (ClientId) o;

		if (sequence != clientId.sequence) {
			return false;
		}
		if (time != clientId.time) {
			return false;
		}
		if (ip != null ? !ip.equals(clientId.ip) : clientId.ip != null) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = ip != null ? ip.hashCode() : 0;
		result = 31 * result + (int) (time ^ (time >>> 32));
		result = 31 * result + (int) (sequence ^ (sequence >>> 32));
		return result;
	}

	@Override
	public String toString() {
		return getClientId();
	}

}
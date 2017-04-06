package pers.cy.iris.commons.cluster;

import java.io.Serializable;
import java.util.StringTokenizer;

/**
 * @Author:cy
 * @Date:Created in  17/3/30
 * @Destription: broker模型
 */
public class Broker implements Serializable, Comparable<Broker> {
	private static final long serialVersionUID = -8443236777342383072L;
	// IP
	private String ip;
	// 端口
	private int port;
	// JMX端口
	private transient int jmxPort;
	// 复制端口
	private transient int replicationPort;
	// 名称
	private transient String name;
	// 分组
	private transient String group;
	// 别名
	private String alias;
	// 类型
	private transient BrokerType type;
	// 数据中心
	private byte dataCenter;
	// 集群角色
	private transient ClusterRole role;
	// 权限
	private Permission permission;
	// 重试类型
	private RetryType retryType;
	// 同步方式
	private SyncMode syncMode;
	// 防止被JSON输出
	private transient boolean readable;
	// 防止被JSON输出
	private transient boolean writable;
	// 防止被JSON输出
	private transient boolean disabled;
	// 防止被JSON输出
	private transient String address;

	public Broker() {
	}

	public Broker(String name) {
		if (name != null && !name.isEmpty()) {
			String[] parts = new String[]{null, null, null, null, null};
			int index = 0;
			StringTokenizer tokenizer = new StringTokenizer(name, "_.:");
			while (tokenizer.hasMoreTokens()) {
				parts[index++] = tokenizer.nextToken();
				if (index >= parts.length) {
					break;
				}
			}
			if (index < 5) {
				throw new IllegalArgumentException("broker name " + name + " is invalid");
			}
			this.ip = parts[0] + "." + parts[1] + "." + parts[2] + "." + parts[3];
			this.port = Integer.valueOf(parts[4]);
		}
	}

	public Broker(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	public Broker(String ip, int port, String alias) {
		this(ip, port);
		this.alias = alias;
	}

	public Broker(String ip, int port, String alias, byte dataCenter) {
		this(ip, port, alias);
		this.dataCenter = dataCenter;
	}

	/**
	 * 获取JMX端口号
	 *
	 * @param port broker的端口
	 * @return JMX端口
	 */
	public static int getJmxPort(final int port) {
		return port % 10000 + 10000;
	}

	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return this.port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getJmxPort() {
		if (jmxPort <= 0) {
			jmxPort = getJmxPort(port);
		}
		return this.jmxPort;
	}

	public void setJmxPort(int jmxPort) {
		this.jmxPort = jmxPort;
	}

	public int getReplicationPort() {
		if (replicationPort <= 0) {
			replicationPort = port % 5000 + 5000;
		}
		return this.replicationPort;
	}

	public void setReplicationPort(int replicationPort) {
		this.replicationPort = replicationPort;
	}

	public String getName() {
		if (name == null && ip != null && !ip.isEmpty() && port > 0) {
			name = ip.replace('.', '_') + "_" + port;
		}
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGroup() {
		if ((group == null || group.isEmpty()) && alias != null && !alias.isEmpty()) {
			int pos = alias.lastIndexOf('_');
			if (pos > 0) {
				group = alias.substring(0, pos);
			}
		}
		return this.group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getAlias() {
		if ((alias == null || alias.isEmpty()) && group != null && !group.isEmpty()) {
			ClusterRole role = getRole();
			switch (role) {
				case MASTER:
					alias = group + "_m";
					break;
				case SLAVE:
					alias = group + "_s";
					break;
			}
		}
		return this.alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public BrokerType getType() {
		if (type == null) {
			if (port > 60000) {
				type = BrokerType.AMQ;
			} else if (port >= 50000) {
				type = BrokerType.JMQ;
			} else if (port >= 40000) {
				type = BrokerType.JMQ_PROXY;
			}
		}
		return this.type;
	}

	public void setType(BrokerType type) {
		this.type = type;
	}

	public SyncMode getSyncMode() {
		return syncMode;
	}

	public void setSyncMode(SyncMode syncMode) {
		this.syncMode = syncMode;
	}

	public byte getDataCenter() {
		return this.dataCenter;
	}

	public void setDataCenter(byte dataCenter) {
		this.dataCenter = dataCenter;
	}

	public ClusterRole getRole() {
		if (role == null) {
			this.role = sourceRole();
		}
		return this.role;
	}

	public void setRole(ClusterRole role) {
		this.role = role;
	}

	public Permission getPermission() {

		if (permission == null) {
			ClusterRole role = getRole();
			switch (role) {
				case MASTER:
					permission = Permission.FULL;
					break;
				case SLAVE:
					permission = Permission.READ;
					break;
				default:
					permission = Permission.NONE;
			}
		}
		return this.permission;
	}

	public void setPermission(Permission permission) {
		this.permission = permission;
	}

	public RetryType getRetryType() {
		return retryType;
	}

	public void setRetryType(RetryType retryType) {
		this.retryType = retryType;
	}

	public String getAddress() {
		return ip + ":" + port;
	}

	/**
	 * 获取初始化角色
	 *
	 * @return 初始化角色
	 */
	public ClusterRole sourceRole() {
		ClusterRole role = ClusterRole.getRoleByAlias(alias);
		if (role == null) {
			role = ClusterRole.getRoleByPort(port);
		}
		if (role == null) {
			role = ClusterRole.NONE;
		}
		return role;
	}

	/**
	 * 是否可读
	 *
	 * @return 可读标示
	 */
	public boolean isReadable() {
		return role != ClusterRole.NONE && getPermission().contain(Permission.READ);
	}

	/**
	 * 是否可写
	 *
	 * @return 可写标示
	 */
	public boolean isWritable() {
		return role == ClusterRole.MASTER && getPermission().contain(Permission.WRITE);
	}

	/**
	 * 是否被禁用了
	 *
	 * @return
	 */
	public boolean isDisabled() {
		return role == ClusterRole.NONE || getPermission() == Permission.NONE;
	}

	/**
	 * 复制一份
	 *
	 * @return 复制数据
	 */
	public Broker clone() {
		Broker target = new Broker();
		target.setIp(ip);
		target.setPort(port);
		target.setJmxPort(jmxPort);
		target.setReplicationPort(replicationPort);
		target.setName(name);
		target.setGroup(group);
		target.setAlias(alias);
		target.setType(type);
		target.setDataCenter(dataCenter);
		target.setRole(role);
		target.setPermission(permission);
		target.setRetryType(retryType);
		return target;
	}


	@Override
	public String toString() {
		// {"ip":"127.0.0.1","port":50000,"alias":"mq1_m","dataCenter":0,"permission":"FULL","retryType":"DB"}
		StringBuilder builder = new StringBuilder();
		builder.append("{\"ip\":\"").append(ip).append("\",\"port\":").append(port).append(",\"alias\":\"")
				.append(getAlias()).append("\",\"dataCenter\":").append(dataCenter).append(",\"permission\":\"")
				.append(getPermission()).append("\"");
		if (syncMode != null) {
			builder.append(",\"syncMode\":\"").append(syncMode).append("\"");
		}
		if (retryType != null) {
			builder.append(",\"retryType\":\"").append(retryType).append("\"");
		}

		builder.append("}");
		return builder.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Broker broker = (Broker) o;

		if (port != broker.port) {
			return false;
		}
		if (ip != null ? !ip.equals(broker.ip) : broker.ip != null) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = ip != null ? ip.hashCode() : 0;
		result = 31 * result + port;
		return result;
	}

	/**
	 * 按分组和角色升序排序
	 * 先按分组排序，若分组相同则按角色排序（wyzhangpeng1）
	 * @param o
	 * @return
	 */
	@Override
	public int compareTo(Broker o) {
		if (this == null && o == null) {
			return 0;
		}
		if (this != null && o == null) {
			return 1;
		}
		if (this == null && o != null) {
			return -1;
		}
		if (this.getGroup() != null && o.getGroup() == null) {
			return 1;
		}
		if (this.getGroup() == null && o.getGroup() != null) {
			return -1;
		}
		int value = this.getGroup().compareTo(o.getGroup());
		if (value != 0) {
			return value;
		}
		if (this.getRole() != null && o.getRole() == null) {
			return 1;
		}
		if (this.getRole() == null && o.getRole() != null) {
			return -1;
		}
		return this.getRole().compareTo(o.getRole());
	}

}
package com.cy.iris.commons.cluster;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author:cy
 * @Date:Created in  17/3/31
 * @Destription: broker组,包含一个master,若干slave
 */
public class BrokerGroup implements Serializable {

	private static final long serialVersionUID = -7266001157722687953L;
	// 分组中的Broker列表
	private List<Broker> brokers = new ArrayList<Broker>();
	// Broker类型
	private BrokerType brokerType;
	// 权限
	private Permission permission;
	// 分组
	private String group;
	// 生产者权重，用于分流
	private transient short weight;

	public BrokerGroup() {
	}

	public BrokerGroup(List<Broker> brokers) {
		if (brokers != null) {
			this.brokers.addAll(brokers);
		}
	}

	public List<Broker> getBrokers() {
		return this.brokers;
	}

	public BrokerType getBrokerType() {
		if (brokerType == null && !brokers.isEmpty()) {
			brokerType = brokers.get(0).getType();
		}
		return this.brokerType;
	}

	public Permission getPermission() {
		if (permission == null) {
			Permission max = Permission.NONE;
			for (Broker broker : brokers) {
				if (broker.getPermission().ordinal() > max.ordinal()) {
					max = broker.getPermission();
				}
			}
			permission = max;
		}
		return this.permission;
	}

	public void setPermission(Permission permission) {
		this.permission = permission;
	}

	public String getGroup() {
		if ((group == null || group.isEmpty()) && !brokers.isEmpty()) {
			group = brokers.get(0).getGroup();
		}
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	/**
	 * 注册指定的broker到该broker组，并标记注册的broker所在组为当前组
	 *
	 * @param broker 要注册的broker
	 */
	public void addBroker(Broker broker) {
		if (broker != null) {
			if ((broker.getGroup() == null || broker.getGroup().isEmpty()) && (group != null && !group.isEmpty())) {
				broker.setGroup(group);
			}
			brokers.add(broker);
		}
	}

	/**
	 * 从当前broker组移除指定的broker,并标记移除的broker所在组为null
	 *
	 * @param broker 要移除的broker
	 */
	public void removeBroker(Broker broker) {
		if (broker != null) {
			brokers.remove(broker);
		}
	}

	public Broker getMaster() {
		for (Broker broker : brokers) {
			if (broker != null && broker.getRole() == ClusterRole.MASTER) {
				return broker;
			}
		}

		return null;
	}


	/**
	 * 获取Broker组中的所有Slave，不包含backup
	 * <p>
	 * 该方法不会返回null, 当Broker组中没有Slave, 返回一个空的List
	 * </p>
	 *
	 * @return 包含在当前Broker组中所有slave的List
	 */
	public List<Broker> getSlaves() {
		List<Broker> slaves = new ArrayList<Broker>();

		for (Broker broker : brokers) {
			if (broker != null && broker.getRole() == ClusterRole.SLAVE) {
				slaves.add(broker);
			}
		}

		return slaves;
	}

	/**
	 * 获取该Broker组中指定名称的Broker
	 *
	 * @param name 指定的Broker名称
	 * @return 如果找到指定名称的broker，则返回；如果未找到，则返回null
	 */
	public Broker getBroker(String name) {
		for (Broker broker : brokers) {
			if (broker == null) {
				continue;
			}

			if (broker != null && name.equals(broker.getName())) {
				return broker;
			}
		}

		return null;
	}

	public short getWeight() {
		return weight;
	}

	public void setWeight(short weight) {
		this.weight = weight;
	}

	/**
	 * 复制一份数据
	 *
	 * @return 复制数据
	 */
	public BrokerGroup clone() {
		BrokerGroup target = new BrokerGroup();
		target.setGroup(group);
		target.setPermission(permission);
		for (Broker broker : brokers) {
			target.addBroker(broker.clone());
		}
		return target;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		BrokerGroup group = (BrokerGroup) o;
		if (!this.getGroup().equals(group.getGroup())) {
			return false;
		}
		if (brokers != null ? !brokers.equals(group.brokers) : group.brokers != null) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		return brokers != null ? brokers.hashCode() : 0;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(group).append(":");
		for (int i = 0; i < brokers.size(); i++) {
			if (i > 0) {
				sb.append(',');
			}
			sb.append(brokers.get(i).getName());
			sb.append(",");
			sb.append(brokers.get(i).getPermission());
		}
		return sb.toString();
	}
}
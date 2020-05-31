package pers.cy.iris.coordinator.raft.core;

/**
 * @Author:cy
 * @Date:Created in  2020/5/31
 * @Destription:
 */
public class ElectionEvent {
	private final ClusterRole role;

	public ElectionEvent(ClusterRole role) {
		this.role = role;
	}

	public ClusterRole getRole() {
		return role;
	}
}

package pers.cy.iris.broker.store.sequence;

import java.util.ArrayList;

/**
 * @Author:cy
 * @Date:Created in  17/5/25
 * @Destription: 头尾相连的循环链表，且链表中的每个元素都持有链表引用
 */
public class LinkedNodeList<T extends LinkedNode<T>> {

	//链表头
	transient T head;
	//元素个数
	transient int size;

	public LinkedNodeList() {
	}


	/**
	 * 队列是否为空
	 * @return
	 */
	public boolean isEmpty() {
		return head == null;
	}

	/**
	 * 向队尾添加节点
	 * @param node
	 */
	public void addLast(T node) {
		node.linkToTail(this);
	}

	/**
	 * 向头部添加节点
	 * @param node
	 */
	public void addFirst(T node) {
		node.linkToHead(this);
	}

	/**
	 * 获取头部节点
	 * @return
	 */
	public T getHead() {
		return head;
	}

	/**
	 * 获取尾部节点
	 * @return
	 */
	public T tail() {
		return head != null ? head.prev : null;
	}

	/**
	 * 清理链表中的所有元素
	 */
	public void clear() {
		while (head != null) {
			head.unlink();
		}
	}

	/**
	 * 将链表添加到本链表后面
	 * @param list
	 */
	public void addLast(LinkedNodeList<T> list) {
		if (list.isEmpty()) {
			return;
		}
		if (head == null) {
			//如果自身为空链表，将目标链表转移过来
			head = list.head;
			reparent(list);
		} else {
			//将目标链表连接在尾节点以后
			tail().linkAfter(list);
		}
	}

	/**
	 * 将链表添加到本链表之前
	 * @param list
	 */
	public void addFirst(LinkedNodeList<T> list) {
		if (list.isEmpty()) {
			return;
		}
		if (head == null) {
			//如果自身为空链表，将目标链表转移过来
			reparent(list);
			head = list.head;
			list.head = null;
		} else {
			//将链表添加在本节点之前
			getHead().linkBefore(list);
		}
	}

	/**
	 * 将链表中所有节点里保存的链表换作自身
	 * @param list
	 * @return
	 */
	public T reparent(LinkedNodeList<T> list) {
		size += list.size;
		T n = list.head;
		do {
			n.list = this;
			n = n.next;
		} while (n != list.head);
		list.head = null;
		list.size = 0;
		return n;
	}

	/**
	 * 循环链表向后移动一个节点
	 *
	 * @return
	 */
	public T rotate() {
		if (head == null)
			return null;
		return head = head.getNextCircular();
	}

	/**
	 * 链表向后移动到指定节点
	 * @param head
	 */
	public void rotateTo(T head) {
		assert head != null : "Cannot rotate to a null head";
		assert head.list == this : "Cannot rotate to a node not linked to this list";
		this.head = head;
	}

	/**
	 * 获取链表节点个数
	 * @return
	 */
	public int size() {
		return size;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		boolean first = true;
		T cur = getHead();
		while (cur != null) {
			if (!first) {
				sb.append(", ");
			}
			sb.append(cur);
			first = false;
			cur = cur.getNext();
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 * 将自身的元素装入ArrayList
	 * @return
	 */
	public ArrayList<T> toArrayList() {
		ArrayList<T> rc = new ArrayList<T>(size);
		T cur = head;
		while (cur != null) {
			rc.add(cur);
			cur = cur.getNext();
		}
		return rc;
	}
}

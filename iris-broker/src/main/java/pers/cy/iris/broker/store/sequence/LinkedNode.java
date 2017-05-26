package pers.cy.iris.broker.store.sequence;

/**
 * @Author:cy
 * @Date:Created in  17/5/25
 * @Destription: 节点
 */
public class LinkedNode<T extends LinkedNode<T>> {

	protected transient LinkedNodeList<T> list;
	protected transient T next;
	protected transient T prev;

	public LinkedNode() {
	}

	@SuppressWarnings("unchecked")
	private T getThis() {
		return (T) this;
	}

	/**
	 * 获取该节点所在链表的头节点
	 * @return
	 */
	public T headNode() {
		return list.head;
	}

	/**
	 * 获取该节点所在链表的尾部节点
	 * @return
	 */
	public T tailNode() {
		return list.head.prev;
	}

	/**
	 * 获取该节点的下一个节点
	 * @return
	 */
	public T getNext() {
		return isTailNode() ? null : next;
	}

	/**
	 * 获取该节点的前一个节点
	 * @return
	 */
	public T getPrevious() {
		return isHeadNode() ? null : prev;
	}

	/**
	 * 如果是循环链表，获取该节点的下一个节点
	 * @return
	 */
	public T getNextCircular() {
		return next;
	}

	/**
	 * 如果是循环链表，获取该节点的前一个节点
	 * @return
	 */
	public T getPreviousCircular() {
		return prev;
	}

	/**
	 * 是否为链表头节点
	 * @return
	 */
	public boolean isHeadNode() {
		return list != null && list.head != null && list.head == this;
	}

	/**
	 * 是否为链表尾节点
	 * @return
	 */
	public boolean isTailNode() {
		return list != null && list.head != null && list.head.prev != null &&
				list.head.prev == this;
	}

	/**
	 * 将节点添加为自身下一个
	 * @param node
	 */
	public void linkAfter(T node) {
		if (node == this) {
			throw new IllegalArgumentException("You cannot link to yourself");
		}
		if (node.list != null) {
			throw new IllegalArgumentException("You only insert nodes that are not in a list");
		}
		if (list == null) {
			throw new IllegalArgumentException("This node is not yet in a list");
		}

		node.list = list;

		// 插入node到自己和下一个节点之间
		node.prev = getThis(); // link this<-node
		node.next = next; // link node->next
		next.prev = node; // link node<-next
		next = node; // this->node
		list.size++;
	}

	/**
	 * 将目标链表连接在尾节点以后
	 * @param rightList
	 */
	public void linkAfter(LinkedNodeList<T> rightList) {
		if (rightList.isEmpty()) return;

		if (rightList == list) {
			throw new IllegalArgumentException("You cannot link to yourself");
		}
		if (list == null) {
			throw new IllegalArgumentException("This node is not yet in a list");
		}

		T rightHead = rightList.head;
		T rightTail = rightList.head.prev;
		list.reparent(rightList);

		// given we linked this<->next and are inserting list in between
		rightHead.prev = getThis(); // link this<-list
		rightTail.next = next; // link list->next
		next.prev = rightTail; // link list<-next
		next = rightHead; // this->list
	}

	/**
	 * 将节点添加为自身上一个
	 * @param node
	 */
	public void linkBefore(T node) {

		if (node == this) {
			throw new IllegalArgumentException("You cannot link to yourself");
		}
		if (node.list != null) {
			throw new IllegalArgumentException("You only insert nodes that are not in a list");
		}
		if (list == null) {
			throw new IllegalArgumentException("This node is not yet in a list");
		}

		node.list = list;

		// 将节点插入到自身和上一个节点之间
		node.next = getThis(); // node->this
		node.prev = prev; // prev<-node
		prev.next = node; // prev->node
		prev = node; // node<-this

		//如果自身是头节点，移交head指针
		if (this == list.head) {
			list.head = node;
		}
		list.size++;
	}

	/**
	 * 将链表添加到自身之前
	 * @param leftList
	 */
	public void linkBefore(LinkedNodeList<T> leftList) {
		if (leftList.isEmpty()) return;

		if (leftList == list) {
			throw new IllegalArgumentException("You cannot link to yourself");
		}
		if (list == null) {
			throw new IllegalArgumentException("This node is not yet in a list");
		}

		T leftHead = leftList.head;
		T leftTail = leftList.head.prev;
		list.reparent(leftList);


		leftTail.next = getThis(); // list->this
		leftHead.prev = prev; // prev<-list
		prev.next = leftHead; // prev->list
		prev = leftTail; // list<-this

		if (isHeadNode()) {
			list.head = leftHead;
		}
	}

	/**
	 * 将自身添加到目标链表尾部
	 * @param target
	 */
	public void linkToTail(LinkedNodeList<T> target) {
		if (list != null) {
			throw new IllegalArgumentException("This node is already linked to a node");
		}

		if (target.head == null) {
			/*
			如果目标链表是空链表，将自身添加为目标链表头，
			并将next和prev指针指向自己
			将目标链表存储在节点自身中
			 */
			next = prev = target.head = getThis();
			list = target;
			list.size++;
		} else {
			target.head.prev.linkAfter(getThis());
		}
	}

	/**
	 * 将自身添加的链表的头部
	 * @param target
	 */
	public void linkToHead(LinkedNodeList<T> target) {
		if (list != null) {
			throw new IllegalArgumentException("This node is already linked to a list");
		}

		if (target.head == null) {
			/*
			如果目标链表是空链表，将自身添加为目标链表头，
			并将next和prev指针指向自己
			将目标链表存储在节点自身中
			 */
			next = prev = target.head = getThis();
			list = target;
			list.size++;
		} else {
			target.head.linkBefore(getThis());
		}
	}

	/**
	 * 将自身从链表中移除
	 * @return
	 */
	public boolean unlink() {

		// 如果已经被从链表中移除
		if (list == null) {
			return false;
		}

		if (getThis() == prev) {
			//节点中只剩自身一个元素
			list.head = null;
		} else {

			next.prev = prev; // prev<-next
			prev.next = next; // prev->next

			if (isHeadNode()) {
				list.head = next;
			}
		}
		list.size--;
		list = null;
		return true;
	}

	/**
	 * 将此节点所在的链表分割为2个链表。
	 * 此节点将作为原链表的尾部节点，另一个链表将返回。
	 *
	 * @return
	 */
	public LinkedNodeList<T> splitAfter() {

		//如果已经是尾节点，返回一个新的空链表
		if (isTailNode()) {
			return new LinkedNodeList<T>();
		}

		LinkedNodeList<T> newList = new LinkedNodeList<T>();
		//下一个节点为新链表的头部节点
		newList.head = next;


		newList.head.prev = list.head.prev; // 新链表头节点prev指向原链表尾节点
		newList.head.prev.next = newList.head; // new list: tail->head
		next = list.head; // old list: tail->head
		list.head.prev = getThis(); // old list: tail<-head

		//更新新链表中所有节点中的链表信息
		T n = newList.head;
		do {
			n.list = newList;
			n = n.next;
			newList.size++;
			list.size--;
		} while (n != newList.head);

		return newList;
	}

	/**
	 * 将此节点所在的链表分割为2个链表。
	 * 此节点将作为新链表的头部节点，另一个链表将返回。
	 *
	 * @return
	 */
	public LinkedNodeList<T> splitBefore() {

		if (isHeadNode()) {
			return new LinkedNodeList<T>();
		}

		LinkedNodeList<T> newList = new LinkedNodeList<T>();
		newList.head = list.head;
		list.head = getThis();

		T newListTail = prev;

		prev = newList.head.prev; // old list: tail<-head
		prev.next = getThis(); // old list: tail->head
		newList.head.prev = newListTail; // new list: tail<-head
		newListTail.next = newList.head; // new list: tail->head

		//更新新链表中所有节点中存有的链表
		T n = newList.head;
		do {
			n.list = newList;
			n = n.next;
			newList.size++;
			list.size--;
		} while (n != newList.head);

		return newList;
	}

	/**
	 * 是否属于某一个链表
	 * @return
	 */
	public boolean isLinked() {
		return list != null;
	}

	/**
	 * 获取节点所属的链表
	 * @return
	 */
	public LinkedNodeList<T> getList() {
		return list;
	}

}

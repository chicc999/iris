package pers.cy.iris.commons.util.eventmanager;


/**
 * 事件拥有者
 * 即把事件和listenner绑定,当存在eventListener时,就直接响应此listenner
 */
public class EventOwner<E> {
	private E event;
	private EventListener<E> listener;

	public EventOwner(E event) {
		this.event = event;
	}

	public EventOwner(E event, EventListener<E> listener) {
		this.event = event;
		this.listener = listener;
	}

	public E getEvent() {
		return event;
	}

	public EventListener<E> getListener() {
		return listener;
	}

}
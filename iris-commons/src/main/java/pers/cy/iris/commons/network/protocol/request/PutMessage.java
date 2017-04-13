package pers.cy.iris.commons.network.protocol.request;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import pers.cy.iris.commons.model.message.Message;
import pers.cy.iris.commons.network.netty.session.ProducerId;
import pers.cy.iris.commons.network.protocol.Command;
import pers.cy.iris.commons.network.protocol.Header;
import pers.cy.iris.commons.network.protocol.HeaderType;
import pers.cy.iris.commons.util.Serializer;


/**
 * @Author:cy
 * @Date:Created in  17/4/6
 * @Destription:
 */
public class PutMessage extends Command{

	// 生产者ID
	private ProducerId producerId;
	// 消息数组
	private Message[] messages;
	// 队列ID
	private short queueId;

	public PutMessage() {
		super(new Header(HeaderType.REQUEST,PUT_MESSAGE).typeString("PUT_MESSAGE"));
	}

	public ProducerId getProducerId() {
		return producerId;
	}

	public void setProducerId(ProducerId producerId) {
		this.producerId = producerId;
	}

	public Message[] getMessages() {
		return messages;
	}

	public void setMessages(Message[] messages) {
		this.messages = messages;
	}

	public short getQueueId() {
		return queueId;
	}

	public void setQueueId(short queueId) {
		this.queueId = queueId;
	}

	@Override
	protected ByteBuf encodeBody() {
		ByteBuf body = PooledByteBufAllocator.DEFAULT.buffer();
		Serializer.writeByteString(producerId.getProducerId(),body);

		body.writeInt(messages.length);
		for(Message message : messages){
			message.encode(body);
		}

		body.writeShort(this.queueId);
		return body;
	}

	@Override
	protected void decodeBody(ByteBuf in) {

	}
}

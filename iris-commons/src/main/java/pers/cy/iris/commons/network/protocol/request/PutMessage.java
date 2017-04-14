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
 * @Destription: 生产消息请求
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

	public PutMessage(Header header) {
		super(header);
		this.header.typeString("PUT_MESSAGE");
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
	protected ByteBuf encodeBody(ByteBuf body) {
		Serializer.writeByteString(producerId.getProducerId(),body);

		body.writeInt(messages.length);
		for(Message message : messages){
			message.encode(body);
		}

		body.writeShort(this.queueId);
		return body;
	}

	@Override
	protected void decodeBody(ByteBuf in) throws Exception{
		producerId = new ProducerId(Serializer.readByteString(in));

		//读取消息条数
		int count = in.readInt();
		// 需要解码为BrokerMessage类型
		messages = new Message[count];
		for(int i=0;i<count;i++){
			Message message = new Message();
			messages[i] = message.decode(in);
		}
		// 队列ID
		queueId = in.readShort();

	}
}

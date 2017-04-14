package pers.cy.iris.commons.netty;

import pers.cy.iris.commons.exception.ConnectException;
import pers.cy.iris.commons.exception.RemotingIOException;
import pers.cy.iris.commons.exception.RequestTimeoutException;
import pers.cy.iris.commons.model.message.Message;
import pers.cy.iris.commons.network.netty.client.NettyClient;
import pers.cy.iris.commons.network.netty.client.NettyClientConfig;
import pers.cy.iris.commons.network.netty.session.ClientId;
import pers.cy.iris.commons.network.netty.session.ConnectionId;
import pers.cy.iris.commons.network.netty.session.ProducerId;
import pers.cy.iris.commons.network.protocol.Command;
import pers.cy.iris.commons.network.protocol.request.GetCluster;
import io.netty.channel.Channel;
import pers.cy.iris.commons.network.protocol.request.PutMessage;

import java.net.InetSocketAddress;

/**
 * Created by cy on 16/12/26.
 */
public class NettyClientTest {

	private NettyClient nettyClient;
	public static void main(String[] args){
		new NettyClientTest().init();
	}

	private void init(){
		nettyClient = new NettyClient(new NettyClientConfig());
		try {
			nettyClient.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Channel channel = nettyClient.createChannelSync(new InetSocketAddress("localhost",50088));
			PutMessage command = new PutMessage();
			command.setProducerId(new ProducerId(new ConnectionId(new ClientId("1.0","localhost",System.currentTimeMillis()))));
			Message message = new Message("test","第一条消息","1");
			message.setApp("app");
			Message[] messages = new Message[1];
			messages[0] = message;
			command.setMessages(messages);
			Command command1 = nettyClient.sync(channel,command,10000);
			if(command1.getHeader().getStatus() != 200){
				System.out.println(command1.getHeader().getError());
			}
		} catch (ConnectException e) {
			e.printStackTrace();
		} catch (RequestTimeoutException e){
			e.printStackTrace();
		}catch (RemotingIOException e) {
			e.printStackTrace();
		}
	}
}

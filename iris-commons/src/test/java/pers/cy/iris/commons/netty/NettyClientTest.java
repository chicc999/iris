package pers.cy.iris.commons.netty;

import pers.cy.iris.commons.exception.ConnectException;
import pers.cy.iris.commons.exception.RemotingIOException;
import pers.cy.iris.commons.exception.RequestTimeoutException;
import pers.cy.iris.commons.network.netty.client.NettyClient;
import pers.cy.iris.commons.network.netty.client.NettyClientConfig;
import pers.cy.iris.commons.network.protocol.request.GetCluster;
import io.netty.channel.Channel;

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
			GetCluster command = new GetCluster();
			command.app("aaa").dataCenter((byte)1).clientId("y134huihuew");
			nettyClient.sync(channel,command,10000);
		} catch (ConnectException e) {
			e.printStackTrace();
		} catch (RequestTimeoutException e){
			e.printStackTrace();
		}catch (RemotingIOException e) {
			e.printStackTrace();
		}
	}
}
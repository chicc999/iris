package com.cy.iris.commons.netty;

import com.cy.iris.commons.exception.ConnectException;
import com.cy.iris.commons.exception.RemotingIOException;
import com.cy.iris.commons.network.netty.client.NettyClient;
import com.cy.iris.commons.network.netty.client.NettyClientConfig;
import com.cy.iris.commons.network.protocol.Header;
import com.cy.iris.commons.network.protocol.request.HeartBeat;
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
			nettyClient.sync(channel,new HeartBeat());
		} catch (ConnectException e) {
			e.printStackTrace();
		} catch (RemotingIOException e) {
			e.printStackTrace();
		}
	}
}

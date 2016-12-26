package com.cy.iris.commons.netty;

import com.cy.iris.commons.network.netty.client.NettyClient;
import com.cy.iris.commons.network.netty.client.NettyClientConfig;

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
		nettyClient.createChannel(new InetSocketAddress("www.baidu.com",80));
	}
}

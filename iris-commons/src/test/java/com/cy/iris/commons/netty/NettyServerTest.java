package com.cy.iris.commons.netty;

import com.cy.iris.commons.network.netty.server.NettyServer;
import com.cy.iris.commons.network.netty.server.NettyServerConfig;

/**
 * Created by cy on 16/12/26.
 */
public class NettyServerTest {

	private NettyServer nettyServer;

	public static void main(String[] args){
		new NettyServerTest().init();
	}

	private void init(){
		NettyServerConfig serverConfig= new NettyServerConfig();
		serverConfig.setPort(50088);

		nettyServer = new NettyServer(serverConfig);
		try {
			nettyServer.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

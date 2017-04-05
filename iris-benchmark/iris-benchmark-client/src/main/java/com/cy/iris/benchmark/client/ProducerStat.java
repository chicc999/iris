package com.cy.iris.benchmark.client;

import com.cy.iris.benchmark.stat.SampleResult;
import com.cy.iris.benchmark.stat.SamplerClient;
import com.cy.iris.commons.exception.RemotingIOException;
import com.cy.iris.commons.exception.RequestTimeoutException;
import com.cy.iris.commons.network.netty.client.NettyClient;
import com.cy.iris.commons.network.netty.client.NettyClientConfig;
import com.cy.iris.commons.network.protocol.Command;
import com.cy.iris.commons.network.protocol.request.GetCluster;
import io.netty.channel.Channel;

import java.net.InetSocketAddress;

/**
 * @Author:cy
 * @Date:Created in  17/4/5
 * @Destription:
 */
public class ProducerStat extends SamplerClient{

	private NettyClient nettyClient;
	private Channel channel;
	GetCluster command ;

	@Override
	public void beforeStart() throws Exception {
		if(nettyClient == null){
			nettyClient = new NettyClient(new NettyClientConfig());
		}
	}

	@Override
	public void doStart() throws Exception {
		nettyClient.start();
		channel = nettyClient.createChannelSync(new InetSocketAddress("localhost",50088));
		command = new GetCluster();
		command.app("aaa").dataCenter((byte)1).clientId("y134huihuew");
	}

	@Override
	public void afterStart() throws Exception {
	}

	@Override
	public void beforeStop() {

	}

	@Override
	public void doStop() {
		nettyClient.stop();
	}

	@Override
	protected SampleResult doWork() {
		SampleResult result = new SampleResult();
		try {
			Command response =	nettyClient.sync(channel,command,10000);
		} catch (Exception e) {

		}
		return null;
	}
}

package com.cy.iris.benchmark.client;

import com.cy.iris.benchmark.stat.CollectJob;
import com.cy.iris.benchmark.stat.SamplerClient;
import com.cy.iris.commons.network.netty.client.NettyClient;
import com.cy.iris.commons.network.netty.client.NettyClientConfig;
import com.cy.iris.commons.network.protocol.Command;
import com.cy.iris.commons.network.protocol.request.GetCluster;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * @Author:cy
 * @Date:Created in  17/4/5
 * @Destription:
 */
public class ProducerStat extends SamplerClient{

	private static Logger logger = LoggerFactory.getLogger(ProducerStat.class);

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
	protected boolean doWork() {
		// TODO 不对报文内容做判断,意味着如果服务端返回响应但执行失败,按照执行成功计算
		try {
			nettyClient.sync(channel,command,10000);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static void main(String[] args){
		CollectJob job = new CollectJob();
		try {
			job.stat(ProducerStat.class.getName(),1);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}

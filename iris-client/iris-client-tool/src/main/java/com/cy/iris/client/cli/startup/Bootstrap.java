package com.cy.iris.client.cli.startup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @Author:cy
 * @Date:Created in 17:54 17/3/13
 * @Destription: 启动类
 */
public class Bootstrap {

	private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);

	public static void main(String[] args) throws IOException {
		Bootstrap bootstrap = new Bootstrap();

		bootstrap.init();

	}

	private void init() throws IOException {
		logger.info("开始启动");
		System.out.println("请输入指令:");
		BufferedReader br =
				new BufferedReader(new InputStreamReader(System.in));

		String line;
		while ((line = br.readLine()) != null) {
			executeLine(line);
		}
	}

	private void executeLine(String line) {
		if (!line.equals("")) {
			System.out.println(line);
		}
	}
}

package com.cy.iris.coordinator;

import org.apache.curator.test.TestingServer;

/**
 * Created by cy on 17/2/21.
 */
public class Test {
	public static void main(String[] args) throws Exception {
		new TestingServer(2181);
	}
}

package com.cy.iris.commons.util.eventmanager.disruptor.test;

import com.lmax.disruptor.EventHandler;

import java.util.concurrent.TimeUnit;

public class LongEventHandler implements EventHandler<LongEvent>
{
	public void onEvent(LongEvent event, long sequence, boolean endOfBatch)
	{
		//System.out.println("Event: " + event);

	}
}
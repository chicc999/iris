package com.cy.iris.commons.util.eventmanager.disruptor.test;

import com.lmax.disruptor.EventFactory;

public class LongEventFactory implements EventFactory<LongEvent>
{
	public LongEvent newInstance()
	{
		return new LongEvent();
	}
}
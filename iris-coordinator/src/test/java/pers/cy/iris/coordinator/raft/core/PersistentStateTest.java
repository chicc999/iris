package pers.cy.iris.coordinator.raft.core;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Random;
import java.util.UUID;

/**
 * @Author:cy
 * @Date:Created in  2020/5/23
 * @Destription:
 */

public class PersistentStateTest {

	@Test
	public void testFlushAndRecover() throws Exception {
		PersistentState persistentState = new PersistentState("/export/data");
		persistentState.start();
		long term = new Random().nextLong();
		String id = UUID.randomUUID().toString();
		persistentState.setCurrentTerm(term);
		persistentState.setVotedFor(id);

		persistentState.stop();

		persistentState.start();
		Assert.assertEquals(persistentState.getCurrentTerm(),term);
		Assert.assertEquals(persistentState.getVotedFor(),id);

		File file = persistentState.getFile();
		file.deleteOnExit();
	}
}

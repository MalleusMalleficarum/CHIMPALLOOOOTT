package edu.kit.ipd.creativecrowd.persistentmodel;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ExperimentWatchdogTest {

	ExperimentWatchdog ewd;
	@Before
	public void setUp() {
		ewd = new ExperimentWatchdog("Brot");
	}
	@After
	public void tearDown() {
		ewd = null;
	}
	@Test
	public void testInvalidRun() {
		ExperimentWatchdog test = new ExperimentWatchdog(null);
		test.run();
	}

	@Test
	public void testExperimentWatchdog() {
		new ExperimentWatchdog("Test");
		assertNotEquals(null, ExperimentWatchdog.getWatchdog("Test"));
	}

	@Test
	public void testIncreaseSubmitted() {
		assertEquals(ewd.increaseSubmitted() + 1, ewd.increaseSubmitted());
	}

	@Test
	public void testIncreasePermanent() {
		assertEquals(ewd.increasePermanent() + 1, ewd.increasePermanent());
	}

	@Test
	public void testIncreaseDuplicate() {
		assertEquals(ewd.increaseDuplicate() + 1, ewd.increaseDuplicate());
	}

	@Test
	public void testGetWatchdog() {
		assertNotEquals(null, ExperimentWatchdog.getWatchdog("Brot"));
	}

}

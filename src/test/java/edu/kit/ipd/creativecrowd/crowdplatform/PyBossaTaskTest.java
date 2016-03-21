package edu.kit.ipd.creativecrowd.crowdplatform;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.kit.ipd.creativecrowd.crowdplatform.PyBossaTask.PyBossaInfo;

public class PyBossaTaskTest {

	PyBossaTask pbt;
	@Before
	public void setUp() {
		try {
			HITSpec hits = new HITSpec("title", "description", 1, 1, "https://its a trap", 31, 31, null);
			pbt = new PyBossaTask(hits, 42);
		} catch (IllegalInputException e) {
			System.out.println(e.getMessage());
		}
	}
	@Test
	public void testPyBossaTaskHITSpecInt() {
		assertNotEquals(null, pbt.getInfo());
		assertEquals(1, pbt.getN_answers());
		assertEquals(42, pbt.getProject_id());
	}

	@Test
	public void testGetN_answers() {
		assertEquals(1, pbt.getN_answers());
	}

	@Test
	public void testSetN_answers() {
		pbt.setN_answers(42);
		assertEquals(42, pbt.getN_answers());
	}

	@Test
	public void testGetInfo() {
		PyBossaInfo test = pbt.getInfo();
		assertNotEquals(null, test);
		assertEquals("title", test.getTitle());
		assertEquals("description", test.getDescription());
		assertEquals(1, test.getRewardCents());
		assertEquals("https://its a trapPyBossa", test.getQuestion());
	}

	@Test
	public void testGetProject_id() {
		assertEquals(42, pbt.getProject_id());
	}

	@After
	public void tearDown() {
		pbt = null;
	}
}

package edu.kit.ipd.creativecrowd.crowdplatform;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class AssignmentIdTest {

	AssignmentId ai;
	@Before
	public void setUp() {
		ai = new AssignmentId("I bin oa Ai-Dieh");
	}
	@Test
	public void testSetAndGetId() {
		assertEquals("I bin oa Ai-Dieh", ai.getId());
		ai.setId("I bin koa Ai-Dieh");
		assertEquals("I bin koa Ai-Dieh", ai.getId());
	}

}

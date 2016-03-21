package edu.kit.ipd.creativecrowd.crowdplatform;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class WorkerIdTest {
	
	WorkerId wid;
	
	@Before
	public void setUp() {
		wid = new WorkerId("Ja sowass!");
	}
	
	@Test
	public void testGetId() {
		assertEquals("Ja sowass!", wid.getId());
	}

}

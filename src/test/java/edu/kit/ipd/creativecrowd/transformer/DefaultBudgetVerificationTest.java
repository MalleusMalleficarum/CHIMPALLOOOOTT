package edu.kit.ipd.creativecrowd.transformer;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import java.util.LinkedList;

public class DefaultBudgetVerificationTest {
	
	DefaultBudgetVerification dbv;
	LinkedList<Boolean> ids;
	
	@Before
	public void setUp() {
		dbv = new DefaultBudgetVerification();
		ids = new LinkedList<Boolean>();
		ids.add(true);
		ids.add(false);
		ids.add(true);
	}
	@Test
	public void testVerify() {
		assertEquals(false, dbv.verify(ids));
	}

}

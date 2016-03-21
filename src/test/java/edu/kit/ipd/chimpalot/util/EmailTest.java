package edu.kit.ipd.chimpalot.util;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class EmailTest {

	static Email em;
	@BeforeClass
	public static void setUp() {
		em = new Email();
	}
	@AfterClass
	public static void tearDown() {
		em = null;
	}
	@Test
	public void testSendMail() {
		assertFalse(em.sendMail(null, null, null));
		assertTrue(em.sendMail("chimpalot.reward@gmail.com", "run of a testcase", "A testcase for Email.class was run, this is the result."));
	}
	

	@Test
	public void testIsValidEmail() {
		assertTrue(Email.isValidEmail("chimpalot.reward@gmail.com"));
		assertFalse(Email.isValidEmail("ich bin keine email"));
	}

}

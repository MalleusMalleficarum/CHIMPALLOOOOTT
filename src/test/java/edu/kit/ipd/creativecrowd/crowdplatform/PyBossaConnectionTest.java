package edu.kit.ipd.creativecrowd.crowdplatform;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.kit.ipd.chimpalot.util.GlobalApplicationConfig;

public class PyBossaConnectionTest {

	PyBossaConnection pbc;
	HITSpec hits;
	AssignmentId aid;
	WorkerId wid;
	
	@Before
	public void setUp() throws IllegalInputException{
		GlobalApplicationConfig.configure(true);
		pbc = new PyBossaConnection();
		hits = new HITSpec("title", "description", 1, 1, "https://its a trap", 31, 31, null);
		aid = new AssignmentId(null);
		wid = new WorkerId(null);
	}
	
	@Test(expected = ConnectionFailedException.class)
	public void testPublishInvalidHIT() throws ConnectionFailedException {
		pbc.publishHIT(null);
	}

	@Test(expected = IllegalInputException.class)
	public void testEndInvalidHIT() throws IllegalInputException{
		pbc.endHIT(null);
	}

	@Test(expected = IllegalInputException.class)
	public void testApproveInvalidHIT() throws IllegalInputException{
		pbc.approveHIT(aid, wid, null);
	}

	@Test(expected = IllegalInputException.class)
	public void testSendBonus() throws IllegalInputException{
		pbc.sendBonus(aid, wid, 0, null);
	}

	@Test(expected = IllegalInputException.class)
	public void testRejectInvalidAssignment() throws IllegalInputException{
		pbc.rejectAssignment(aid, null);
	}

	@Test(expected = IllegalInputException.class)
	public void testGetAvailableAssignmentsForInvalid() throws IllegalInputException{
		pbc.getAvailableAssignments(null);
	}

	@Test(expected = IllegalInputException.class)
	public void testExtendAssignmentNumberInvalid() throws IllegalInputException{
		pbc.extendAssignmentNumber(0, null);
	}

	@Test
	public void testGetPlatformIdentity() {
		assertEquals(PlatformIdentity.PyBossa, pbc.getPlatformIdentity());
	}

	@Test
	public void testGetAbbrevation() {
		assertEquals("PB", pbc.getAbbrevation());
	}

}

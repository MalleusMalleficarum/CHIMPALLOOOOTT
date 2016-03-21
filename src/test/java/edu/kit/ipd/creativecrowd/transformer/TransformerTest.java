package edu.kit.ipd.creativecrowd.transformer;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.kit.ipd.chimpalot.util.GlobalApplicationConfig;
import edu.kit.ipd.creativecrowd.crowdplatform.AbstractConnection;
import edu.kit.ipd.creativecrowd.crowdplatform.ConnectionFailedException;
import edu.kit.ipd.creativecrowd.crowdplatform.HITSpec;
import edu.kit.ipd.creativecrowd.crowdplatform.IllegalInputException;
import edu.kit.ipd.creativecrowd.crowdplatform.MTurkConnection;
import edu.kit.ipd.creativecrowd.crowdplatform.PlatformIdentity;
import edu.kit.ipd.creativecrowd.crowdplatform.PyBossaConnection;

public class TransformerTest {

	InternalLogicMediator ilm;
	Transformer trans;
	@BeforeClass
	public static void setUpGlConf() {
		GlobalApplicationConfig.configure(true);
	}
	
	@Before
	public void setUp() {
		ilm = new InternalLogicMediator();
		trans = new Transformer(ilm);
	}

	@After
	public void tearDown() {
		ilm = null;
		trans = null;
	}
	@Test
	public void testAddAndGetConnection() {
		AbstractConnection ac1 = null;
		List<AbstractConnection> before = trans.getConnections();
		before.add(ac1);
		trans.addConnection(ac1);
		assertEquals(before, trans.getConnections());
	}

	@Test(expected = ConnectionFailedException.class)
	public void testInvalidAdditionalPublish() throws ConnectionFailedException{
		trans.additionalPublish(null);
	}

	@Test
	public void testContainsConnection() throws ConnectionFailedException{
		trans.addConnection(new PyBossaConnection());
		trans.addConnection(new MTurkConnection());
		assertEquals(true, trans.containsConnection(PlatformIdentity.MTurk));
		assertEquals(true, trans.containsConnection(PlatformIdentity.PyBossa));
		assertEquals(false, trans.containsConnection(PlatformIdentity.Unspecified));
	}

	@Test(expected = IllegalInputException.class)
	public void testRemoveInvalidConnection() throws IllegalInputException{
		trans.addConnection(new PyBossaConnection());
		trans.removeConnection(null);
	}

	@Test(expected = IllegalInputException.class)
	public void testInvalidUpdate() throws IllegalInputException, ConnectionFailedException{
		trans.addConnection(new PyBossaConnection());
		trans.update(PlatformIdentity.Unspecified, false, true);
	}

	@Test
	public void testPublishHIT() throws ConnectionFailedException, IllegalInputException{
		assertEquals("", trans.publishHIT(null));
		trans.addConnection(new MTurkConnection());
		String ret = trans.publishHIT(new HITSpec("title", "description", 1, 1, "https://its a trap", 31, 31, null));
		System.out.println(ret);
		assertTrue(ret.startsWith("?MTurk|"));
	}

	@Test(expected = IllegalInputException.class)
	public void testEndInvalidHIT() throws IllegalInputException{
		trans.endHIT(null);
	}

	@Test(expected = IllegalInputException.class)
	public void testExtendInvalidAssignmentNumber() throws IllegalInputException{
		trans.extendAssignmentNumber(0, null);
	}

	@Test(expected = IllegalInputException.class)
	public void testApproveInvalidHIT() throws IllegalInputException {
		trans.approveHIT(null, null, null);
	}

	@Test(expected = IllegalInputException.class)
	public void testSendInvalidBonus() throws IllegalInputException {
		trans.sendBonus(null, null, -4, null);
	}

	@Test(expected = IllegalInputException.class)
	public void testRejectInvalidAssignment() throws IllegalInputException {
		trans.rejectAssignment(null, "");
	}

}

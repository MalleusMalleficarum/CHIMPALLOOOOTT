package edu.kit.ipd.creativecrowd.transformer;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.BeforeClass;
import org.junit.Test;

import edu.kit.ipd.creativecrowd.crowdplatform.PlatformIdentity;

public class IDGeneratorTest {

	static IDGenerator idg;
	static LinkedList<String> ids;
	static String mainId;
	
	@BeforeClass 
	public static void setUp() {
		idg = new IDGenerator();
		ids = new LinkedList<String>();
		ids.add(PlatformIdentity.MTurk.toString() + "|12?");
		ids.add(PlatformIdentity.PyBossa.toString() + "|192837");
		mainId = (PlatformIdentity.MTurk.toString() + "|12?" + PlatformIdentity.PyBossa.toString() + "|192837");
	}
	
	@Test
	public void testGetID() {
		assertEquals(null, idg.getID(null, PlatformIdentity.MTurk));
		assertEquals("12", idg.getID(mainId, PlatformIdentity.MTurk));
		assertEquals("192837", idg.getID(mainId, PlatformIdentity.PyBossa));
	}

	@Test
	public void testGetMainID() {
		String testid = idg.getMainID(ids);
		assertEquals(mainId, testid);
	}

}

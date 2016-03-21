package edu.kit.ipd.creativecrowd.crowdplatform;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import edu.kit.ipd.creativecrowd.crowdplatform.PyBossaTask.PyBossaInfo;

public class PyBossaInfoTest extends PyBossaInfo {

	static PyBossaInfo pbi;
	@BeforeClass
	public static void setUp() {
		pbi = new PyBossaInfo("title", "description", "question", "keywords", 3);
	}

	@Test
	public void testGetTitle() {
		assertEquals("title", pbi.getTitle());
	}

	@Test
	public void testGetDescription() {
		assertEquals("description", pbi.getDescription());
	}

	@Test
	public void testGetKeywords() {
		assertEquals("keywords", pbi.getKeywords());
	}

	@Test
	public void testGetQuestion() {
		assertEquals("question", pbi.getQuestion());
	}

	@Test
	public void testGetUrl_b() {
		assertEquals("https://pbs.twimg.com/profile_images/2466029543/mdaqaefyp99zmbx7l8qr.jpeg", pbi.getUrl_b());
	}

	@Test
	public void testGetRewardCents() {
		assertEquals(3, pbi.getRewardCents());
	}

	@Test
	public void testSetRewardCents() {
		pbi.setRewardCents(42);
		assertEquals(42, pbi.getRewardCents());
	}

}

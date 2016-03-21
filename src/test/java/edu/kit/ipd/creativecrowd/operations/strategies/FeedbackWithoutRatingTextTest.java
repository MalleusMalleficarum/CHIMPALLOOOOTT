package edu.kit.ipd.creativecrowd.operations.strategies;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.kit.ipd.creativecrowd.mutablemodel.MutableAssignment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.operations.MockExperiment;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;

public class FeedbackWithoutRatingTextTest {
	private static String APPROVE = "Dear Worker,\nwe´re happy to approve your work. Thanks for participating in our studies!";
	private static String BONUSGRANTED = "Dear Worker,\nwe´re happy to grant you a bonus. Please participate in future tasks again!";
	MockExperiment mock;
	MutableExperiment exp;
	MutableAssignment ass;
	FeedbackWithoutRatingText feed;
	Map<String,String> params;

	@Before
	public void setUp() throws Exception {
		mock = new MockExperiment();
		exp = mock.getExperiment();
		ass = exp.addAssignment();
		feed = new FeedbackWithoutRatingText();
		params = new HashMap<String,String>();
		params.put("ftg_class", "edu.kit.ipd.operations.strategies.FeedbackWithoutRatingText");
		params.put("ftg.rejection_message", "reject");
		feed.setParams(params);
	}

	@After
	public void tearDown() throws Exception {
		mock.deleteExperiment();
	}

	@Test
	public void testApprove() {
		try {
			ass.setPaymentOutcome(true, 10);
		} catch (DatabaseException e1) {
			fail(e1.getMessage());
		}
		try {
			assertEquals(APPROVE , feed.generateFeedback(ass));;
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
		
	}
	@Test 
	public void testReject() {
		try {/*-?|Anika|Anika|c11|?*/
			ass.setPaymentOutcome(false, 0);
		} catch (DatabaseException e1) {
			fail(e1.getMessage());
		}
		try {
			assertEquals( "reject", feed.generateFeedback(ass));
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
		
		
	}
	@Test
	public void testBonus() {
		try {
			ass.setPaymentOutcome(true, 10);
		} catch (DatabaseException e1) {
			fail(e1.getMessage());
		}
		try {
			assertEquals(BONUSGRANTED , feed.generateBonusMessage(ass));;
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
		
	}


}

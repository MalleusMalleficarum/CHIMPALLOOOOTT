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

public class FeedbackWithRatingTextTest {
MockExperiment mock;
MutableExperiment exp;
MutableAssignment ass;
FeedbackWithRatingText feed;
Map<String,String> params;

@Before
public void setUp() throws Exception {
	mock = new MockExperiment();
	exp = mock.getExperiment();
	ass = exp.addAssignment();
	feed = new FeedbackWithRatingText();
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
	public void test() {
		try {
			assertNotNull("There is no message generated",feed.generateFeedback(ass));
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
		
	}
	@Test
	public void testBonus() {
		try {
			assertNotNull("There is no message generated",feed.generateBonusMessage(ass));
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
		
	}

}

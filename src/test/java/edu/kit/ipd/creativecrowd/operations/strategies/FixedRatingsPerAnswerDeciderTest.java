package edu.kit.ipd.creativecrowd.operations.strategies;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.kit.ipd.creativecrowd.mutablemodel.MutableAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAssignment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRating;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRatingOption;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRatingTask;
import edu.kit.ipd.creativecrowd.operations.MockExperiment;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.RatingTask;

public class FixedRatingsPerAnswerDeciderTest {
	MockExperiment mock;
	MutableExperiment exp;
	FixedRatingsPerAnswerDecider frpad;
	MutableAnswer answer;
	
	@Before
	public void setUp() throws Exception {
		mock = new MockExperiment();
		exp = mock.getExperiment();
		MutableAssignment a = exp.addAssignment();
		a.getTaskConstellation().addCreativeTask(exp.getCreativeTask());
		answer = a.getTaskConstellation().answerCreativeTaskAt(0);
		a.setSubmitted();
		MutableAssignment as = exp.addAssignment();
		MutableRatingTask rt = exp.addRatingTask();
		as.getTaskConstellation().addRatingTask(rt);
		rt.addAnswerToBeRated(answer);
		MutableRating r = as.getTaskConstellation().addRatingToRatingTaskAt(0);
		for(MutableRatingOption ro: exp.getRatingOptions()) {
			r.setRatingOption(ro);
			r.setFinalQualityIndex(1);
			answer.addRating(r);
			break;
		}
		HashMap<String, String> params = new HashMap<String,String>();
		params.put("rsd.n", "1");
		frpad = new FixedRatingsPerAnswerDecider();
		frpad.setParams(params);
	}

	@After
	public void tearDown() throws Exception {
		mock.deleteExperiment();
	}
	/**
	 * übergibt dem FixedRating eine Antwort mit einem Rating. Da in der Konfigurationsdatei auch ein Rating
	 * gefordert wird, wird als Rückgabe true erwartet.
	 * @throws DatabaseException 
	 */
	@Test
	public void test() throws DatabaseException {
		assertTrue(frpad.hasEnoughRatings(answer, exp));
	}

}

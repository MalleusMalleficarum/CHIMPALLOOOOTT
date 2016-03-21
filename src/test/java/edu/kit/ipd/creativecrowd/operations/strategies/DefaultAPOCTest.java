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
import edu.kit.ipd.creativecrowd.readablemodel.PaymentOutcome;

public class DefaultAPOCTest {
	MockExperiment mock;
	MutableExperiment experiment;
	DefaultAPOC def;
	MutableAnswer answer;
	MutableAssignment a;
	PaymentOutcome p;

	@Before
	public void setUp() throws Exception {
		mock = new MockExperiment();
		experiment = mock.getExperiment();
		def = new DefaultAPOC();
		def.setParams(new HashMap<String, String>());
		a = experiment.addAssignment();
		a.getTaskConstellation().addCreativeTask(experiment.getCreativeTask());
		answer = a.getTaskConstellation().answerCreativeTaskAt(0);
		answer = a.getTaskConstellation().answerCreativeTaskAt(0);
		a.setSubmitted();
		answer.setFinalQualityIndex(1);
		MutableRatingTask rt = experiment.addRatingTask();
		a.getTaskConstellation().addRatingTask(rt);
		rt.addAnswerToBeRated(answer);
		for(MutableRatingOption ro: experiment.getRatingOptions()) {
			MutableRating r = a.getTaskConstellation().addRatingToRatingTaskAt(1);
			r.setRatingOption(ro);
			r.setFinalQualityIndex(1);
		}

	}

	@After
	public void tearDown() throws Exception {
		mock.deleteExperiment();
	}
	/**
	 * Hier soll ein PaymentOutcome für ein Assignment welches approved werden soll, generiert werden.
	 * @throws DatabaseException
	 */
	@Test
	public void test() throws DatabaseException {
		def.run(experiment);
		assertTrue(a.getPaymentOutcome().isApproved());
	}
	@Test
	public void test2() throws DatabaseException {
		def.run(experiment);
		assertEquals(3, a.getPaymentOutcome().getBonusPaidCents());
	}


}

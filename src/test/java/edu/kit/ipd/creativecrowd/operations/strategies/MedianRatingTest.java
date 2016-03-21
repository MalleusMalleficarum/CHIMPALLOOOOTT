package edu.kit.ipd.creativecrowd.operations.strategies;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.kit.ipd.creativecrowd.crowdplatform.WorkerId;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAssignment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRating;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRatingTask;
import edu.kit.ipd.creativecrowd.operations.MockExperiment;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.RatingOption;

public class MedianRatingTest {
	MockExperiment mock;
	MutableExperiment experiment;
	MedianRating rate;
	MutableAnswer answer;

	@Before
	public void setUp() throws Exception {
		mock = new MockExperiment();
		experiment = mock.getExperiment();
		rate = new MedianRating();
		MutableAssignment a = experiment.addAssignment();
		a.getTaskConstellation().addCreativeTask(experiment.getCreativeTask());
		a.setWorker(new WorkerId("mturkid"));
		answer = a.getTaskConstellation().answerCreativeTaskAt(0);
		answer = a.getTaskConstellation().answerCreativeTaskAt(0);
		a.setSubmitted();
		MutableAssignment as = experiment.addAssignment();
		MutableRatingTask rt = experiment.addRatingTask();
		as.getTaskConstellation().addRatingTask(rt);
		rt.addAnswerToBeRated(answer);
		for(RatingOption ro: experiment.getRatingOptions()) {
			as.setWorker(new WorkerId("someworker"));
			MutableRating r = as.getTaskConstellation().addRatingToRatingTaskAt(0);
			r.setRatingOption(ro);
			r.setFinalQualityIndex(1);
			answer.addRating(r);
		}

	}

	@After
	public void tearDown() throws Exception {
		mock.deleteExperiment();
	}
	/**
	 * Dieser Testfall lässt für eine Antwort mit 3 Ratings den RatingQualityIndex berechnen und überprüft das Ergebnis.
	 * @throws DatabaseException
	 */
	@Test
	public void test() throws DatabaseException {
		rate.run(experiment);
//		for(MutableAnswer a: experiment.getCreativeTask().getAnswers()) {
//			assertEquals(2, a.getFinalQualityIndex(), 0.01);
//			}
		assertEquals(2, answer.getFinalQualityIndex(), 0.01); //TODO This test is probably not correct.
	}

}

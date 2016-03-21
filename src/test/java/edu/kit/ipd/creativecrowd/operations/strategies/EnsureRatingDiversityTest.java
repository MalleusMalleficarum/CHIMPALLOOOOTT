package edu.kit.ipd.creativecrowd.operations.strategies;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.kit.ipd.creativecrowd.crowdplatform.WorkerId;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAssignment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRating;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRatingTask;
import edu.kit.ipd.creativecrowd.operations.MockExperiment;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.RatingOption;

public class EnsureRatingDiversityTest {
	MockExperiment mock;
	MutableExperiment experiment;
	EnsureRatingDiversity erd;
	MutableRating r;
	RatingOption mro;

	@Before
	public void setUp() throws Exception {
		mock = new MockExperiment();
		experiment = mock.getExperiment();
		erd = new EnsureRatingDiversity();
		erd.setParams(new HashMap<String,String>());
		MutableAssignment as = experiment.addAssignment();
		MutableRatingTask rt = experiment.addRatingTask();
		as.getTaskConstellation().addRatingTask(rt);
		for(RatingOption ro: experiment.getRatingOptions()) {
			mro = ro;
			break;
		}
		as.setWorker(new WorkerId("mturkid"));
		MutableRating ra = as.getTaskConstellation().addRatingToRatingTaskAt(0);
		ra.setRatingOption(mro);
		r = as.getTaskConstellation().addRatingToRatingTaskAt(0);
		r.setRatingOption(mro);
		MutableRating ri = as.getTaskConstellation().addRatingToRatingTaskAt(0);
		ri.setRatingOption(mro);
		
		}
	

	@After
	public void tearDown() throws Exception {
		mock.deleteExperiment();
	}

	/**
	 * Hier wird ein Experiment mit einem Assignment betrachtet, bei dem der
	 * Worker in allen Ratings dasselbe angekreuzt hat. Es wird erwartet, dass
	 * die Qualitätskennzahl der Ratings 0 ist.
	 * @throws DatabaseException 
	 */
	@Test
	public void testBad() throws DatabaseException {
		erd.run(experiment);
		assertEquals(0,r.getFinalQualityIndex(),0.01);

	}

}

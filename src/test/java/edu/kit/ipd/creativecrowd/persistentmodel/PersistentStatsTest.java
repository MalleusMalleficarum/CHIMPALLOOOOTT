package edu.kit.ipd.creativecrowd.persistentmodel;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import edu.kit.ipd.chimpalot.jsonclasses.ConfigModelJson;
import edu.kit.ipd.chimpalot.util.Logger;
import edu.kit.ipd.creativecrowd.crowdplatform.WorkerId;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAssignment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableCreativeTask;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRatingTask;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableStats;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableTaskConstellation;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;

public class PersistentStatsTest extends PersistentModelBefore {

	private MutableStats testStats;
	
	/**
	 * Diese Methode holt die Stats aus dem Experiment heraus.
	 */
	@Before
	public void setUpStats() {
		try {
			testStats = testExp.getStats();
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	/**
	 * Diese Methode testet die Berechnung und das Holen der Abbrecher.
	 */
	@Test
	public void getCancelledCountTest() {
		try {
			assertTrue(testStats.getCancelledCount() == 0);
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	/**
	 * Diese Methode testet die Berechnung und das Holen der Anzahl der Abgaben.
	 */
	@Test
	public void getSubmissionCountTest() {
		try {

			testExp.addAssignment().setSubmitted();
			testExp.addAssignment().setSubmitted();
			testExp.addAssignment().setSubmitted();
			testExp.addAssignment().setSubmitted();
			testExp.addAssignment().setSubmitted();
			assertTrue(testStats.getSubmissionCount() == 5);
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}
	
	/**
	 * Diese Methode testet die Berechnung und das Holen der Anzahl der akzeptierten Hits.
	 */
	@Test
	public void getAcceptedHitCountTest() {
		try {
			testExp.addAssignment();
			testExp.addAssignment();
			testExp.addAssignment();
			testExp.addAssignment();
			testExp.addAssignment();
			testExp.addAssignment();
			assertTrue(testStats.getAcceptedHitCount() == 6);
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	/**
	 * Diese Methode testet die Berechnung und das Holen der Anzahl der Bewertungen.
	 */
	//TODO this
	@Test
	public void getRatingCountTest() {
		try {
			MutableAssignment m1 = testExp.addAssignment();
			m1.setWorker(new WorkerId("hallo"));
			MutableTaskConstellation tc1 = m1.getTaskConstellation();
			MutableAssignment m2 = testExp.addAssignment();
			m2.setWorker(new WorkerId("wellt"));
			MutableTaskConstellation tc2 = m2.getTaskConstellation();
			MutableCreativeTask ct = testExp.addCreativeTask();
			MutableRatingTask rt1 = testExp.addRatingTask();
			MutableRatingTask rt2 = testExp.addRatingTask();
			MutableRatingTask rt3 = testExp.addRatingTask();
			tc1.addCreativeTask(ct);
			tc2.addCreativeTask(ct);
			tc1.addRatingTask(rt1);
			tc1.addRatingTask(rt2);
			tc2.addRatingTask(rt3);
			tc1.addRatingToRatingTaskAt(1);
			tc1.addRatingToRatingTaskAt(1);
			tc1.addRatingToRatingTaskAt(2);
			tc2.addRatingToRatingTaskAt(1);
			tc2.addRatingToRatingTaskAt(1);
			tc2.addRatingToRatingTaskAt(1);
			assertTrue(testStats.getRatingCount()== 6);
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	/**
	 * Diese Methode testet  das Setzen und das Holen der Anzahl der Klicks auf die Hitvorschau.
	 */
	@Test
	public void getAddPreviewClickTest() {
		try {
			testStats.addPreviewClick();
			testStats.addPreviewClick();
			testStats.addPreviewClick();
			assertTrue(testStats.getPreviewClicksCount() == 3);
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}

	}
	
	/**
	 * Erzeugung zweier Experimente und Vergleich deren Timestamps.
	 * Der Test wirft bei Ungleichheit oder bei einer Exception einen Fehler.
	 */
	//THIS
	@Test
	public void getTimestampTest() {
		try {
			ConfigModelJson x = new ConfigModelJson();
			MutableExperiment testExp1 = testRep.createExperiment("1", x);
			MutableExperiment testExp2 = testRep.createExperiment("2", x);
			String t = testExp1.getStats().getTimestampBegin();
			Logger.debug(testExp2.getStats().getTimestampBegin());

			assertTrue(testExp1.getStats().getTimestampBegin().equals(t));
			
			testRep.deleteExperiment("1");
			testRep.deleteExperiment("2");
			
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}
}

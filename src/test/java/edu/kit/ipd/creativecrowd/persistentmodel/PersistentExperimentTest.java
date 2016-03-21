package edu.kit.ipd.creativecrowd.persistentmodel;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import edu.kit.ipd.creativecrowd.mturk.AssignmentId;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAssignment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableCreativeTask;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRatingOption;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRatingTask;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableStats;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.Assignment;

public class PersistentExperimentTest extends PersistentModelBefore {

	/**
	 *versucht, ein Assignment hinzuzufügen und es dann zu getten
	 */
	@Test
	public void addGetAssignmentTest() {
		try {
			MutableAssignment ass = testExp.addAssignment();
			ass.setAssignmentID(new AssignmentId("id"));
			assertTrue(ass!=null);
			MutableAssignment ass2 =  testExp.getAssignment(ass.getID());
			assertTrue(ass2!=null);
			Assignment ass3 = testExp.getAssignmentWithMturkId(ass.getMTurkAssignmentID());
			assertTrue(ass3!=null);

			assertTrue(ass.equals(ass2));
			assertTrue(ass3.getID().equals(ass.getID()));
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}
	/**
	 * versucht, die Stats des testExperiments zu getten
	 */
	@Test
	public void getStatsTest() {
		try {
			MutableStats stats = testExp.getStats();
			assertTrue(stats != null);
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	/**
	 * versucht, dem testexperiment einen CreativeTask hinzuzufügen, gettet ihn dann und überprüft die zwei so erhaltenen CreativeTasks auf gleichheit
	 */
	@Test
	public void addGetCreativeTaskTest() {
		try {
			MutableCreativeTask ct = testExp.addCreativeTask();
			MutableCreativeTask ct2 = testExp.getCreativeTask();
			assertTrue(ct.equals(ct2));
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}


	}

	/**
	 * versucht, dem testexperiment einen RatingTask hinzuzufügen, gettet ihn dann und überprüft die zwei so erhaltenen RatingTask auf gleichheit
	 */
	@Test
	public void addGetRatingTaskTest() {
		try {
			MutableRatingTask ct = testExp.addRatingTask();
			MutableRatingTask ct2 = testExp.getRatingTasks().iterator().next();
			assertTrue(ct.equals(ct2));
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}

	}
	/**
	 * versucht, dem testexperiment eine RatingOption hinzuzufügen, gettet sie dann und überprüft die zwei so erhaltenen RatingOption auf Gleichheit
	 */
	@Test
	public void addGetRatingOptionTest() {
		try {
			MutableRatingOption ct = testExp.addRatingOption();
			MutableRatingOption ct2 = testExp.getRatingOptions().iterator().next();
			assertTrue(ct.equals(ct2));
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}

	}

	/**
	 * markiert das Testexperiment als beendet und überprüft dann, ob es als beendet markiert wurde
	 */
	@Test
	public void markAsFinishedTest() {
		try {
			testExp.markAsFinished();
			testExp.markExperimentAsSoftFinished();
			assertTrue(testExp.isFinished());
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}

	}

	/**
	 * gibt dem Experiment einen BEschreibungstext und gettet ihn wieder. Überprüft auf gleichheit
	 */
	@Test
	public void setGetDescriptionTest() {
		try {
			testExp.setDescription("hello testtest");
			String result = testExp.getDescription();
			assertTrue(result.equals("hello testtest"));
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}

	}

	/**
	 * Überprüft ein gesettes Budget mit dem danach erhaltenen gegetteten
	 */
	@Test
	public void setGetBudgetTest() {
		try {
			testExp.setBudget(100);
			testExp.setBasicPaymentAnswer(2);
			testExp.setBasicPaymentHIT(3);
			testExp.setBasicPaymentRating(4);
			testExp.setBonusPayment(5);
			assertTrue(testExp.getBudgetCents() == 100
					&&	testExp.getBasicPaymentAnswerCents() == 2
					&& testExp.getBasicPaymentHITCents() == 3
					&& testExp.getBasicPaymentRatingCents() == 4
					&& testExp.getBonusPaymentCents() == 5);
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}

	}
	/**
	 * Überprüft ein gesetten HITTitle mit dem danach erhaltenen gegetteten
	 */
	@Test
	public void setGetHITTitleTest() {
		try {
			testExp.setHITTitle("hit123");
			assertTrue(testExp.getHITTitle().equals("hit123"));

		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}

	}
	/**
	 * Überprüft eine gesette HITDEscription mit der danach erhaltenen gegetteten
	 */
	@Test
	public void setGetHITDescriptionTest() {
		try {
			testExp.setHITDescription("descr123");
			assertTrue(testExp.getHITDescription().equals("descr123"));
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}

	}
	/**
	 * Überprüft gesette tags mit den danach erhaltenen gegetteten
	 */
	@Test
	public void setGetTagsTest() {
		try {
			List<String> sets = new LinkedList<String>();
			sets.add("tagBla");
			sets.add("tag2Bla");
			sets.add("tag3Bla");
			testExp.setTags(sets);

			Iterable<String> res = testExp.getTags();
			Iterator<String> it = res.iterator();
			assertTrue(it.next().equals("tagBla"));
			assertTrue(it.next().equals("tag2Bla"));
			assertTrue(it.next().equals("tag3Bla"));
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}

	}
	/**
	 * Überprüft gesette qualifications mit den danach erhaltenen gegetteten
	 */
	@Test
	public void setGetQualificationsTest() {
		try {
			List<String> sets = new LinkedList<String>();
			sets.add("qualiBla");
			sets.add("quali2Bla");
			sets.add("quali3Bla");
			testExp.setQualifications(sets);

			Iterable<String> res = testExp.getQualifications();
			Iterator<String> it = res.iterator();

			assertTrue(it.next().equals("qualiBla"));
			assertTrue(it.next().equals("quali2Bla"));
			assertTrue(it.next().equals("quali3Bla"));
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}

	}
	/**
	 * Überprüft gesette HITID mit der danach erhaltenen gegetteten
	 */
	@Test
	public void setGetHitIDTest() {
		try {
			testExp.setHitID("TestHit");
			assertTrue(testExp.getHitID().equals("TestHit"));
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}

	}
	/**
	 * Überprüft gesette strategyparams mit den danach erhaltenen gegetteten
	 */
	@Test
	public void setGetStrategyParamsTest() {
		try {
			Map<String,String> map = new HashMap<String,String>();
			map.put("tac","1");
			map.put("ksjzdfg", "jzgugz");
			map.put("sdgf", "asdf");

			testExp.setStrategyParams(map);
			assertTrue(testExp.getStrategyParams().equals(map));
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}

	}
	/**
	 * Überprüft gesette numberofratingsperassignment mit der danach erhaltenen gegetteten
	 */
	@Test
	public void setGetMaxNumberOfRatingsPerAssignment() {
		try {
			testExp.setMaxNumberOfAnswersPerAssignment(55);
			testExp.setMaxNumberOfRatingsPerAssignment(66);

			assertTrue(testExp.getMaxNumberOfAnswersPerAssignment() == 55 && testExp.getMaxNumberOfRatingsPerAssignment() == 66);
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	@Test
	public void setGetRatingTaskViewClass() {
		try {
			testExp.setRatingTaskViewClass("Test!");
			assertTrue(testExp.getRatingTaskViewClass().contentEquals("Test!"));
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}

}

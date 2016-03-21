package edu.kit.ipd.creativecrowd.persistentmodel;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.kit.ipd.chimpalot.jsonclasses.ConfigModelJson;
import edu.kit.ipd.chimpalot.util.GlobalApplicationConfig;
import edu.kit.ipd.chimpalot.util.Logger;
import edu.kit.ipd.creativecrowd.crowdplatform.WorkerId;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAssignment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableCalibrationAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableCalibrationQuestion;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableControlAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableControlQuestion;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableCreativeTask;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRating;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRatingTask;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableTaskConstellation;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.Rating;
import edu.kit.ipd.creativecrowd.readablemodel.Task;

public class PersistentTaskConstellationTest extends PersistentAssignmentBefore {
	MutableTaskConstellation t;
	static PersistentExperimentRepo rep;
	MutableTaskConstellation task;
	MutableAssignment a;
	MutableExperiment e;
	static PersistentCalibrationQuestionRepo q;
	@BeforeClass
	public static void setUPBEforeClass() {
		try {
			File file = new File("CreativeCrowd.db");
			file.delete();
			GlobalApplicationConfig.configure(true);
			rep = new PersistentExperimentRepo();
			 q = new PersistentCalibrationQuestionRepo();
		} catch (DatabaseException e) {
			
			e.printStackTrace();
			fail(e.getMessage());
		}
		}
	
	@Before
	public void setUp() {
		try {
			e = rep.createExperiment("hallo welt", new ConfigModelJson());
			a = e.addAssignment();
			a.setWorker(new WorkerId(new PersistentWorkerRepo().findWorker("MIYNAME", "collmail@web.de").getID()));
			task = a.getTaskConstellation();
		}  catch (DatabaseException e) {
			
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	@After
	public void tearDown() {
		try {
			rep.deleteExperiment("hallo welt");
		} catch (DatabaseException e) {
			
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	@SuppressWarnings("rawtypes")
	private int length(Iterable it) {
		int i = 0;
		for(@SuppressWarnings("unused") Object o : it) {
			i++;
		}
		return i;
	}
	@Test
	public void test() throws DatabaseException {
		assertEquals(length(task.getAnswers()),0);
		assertEquals(task.getCurrentTaskIndex(), 0);
		assertEquals(task.getAssignment().getID(), a.getID());
		assertEquals(length(task.getRatings()), 0);
		assertEquals(task.submitButtonExists(), false);
		assertEquals(task.getTaskCount(), 0);
		assertEquals(task.againButtonExists(), false);
		assertEquals(task.nextButtonExists(), false);
		assertEquals(length(task.getTasks()), 0);
	}
	@Test
	public void calibrationQuestion() {
		try {
			MutableCalibrationQuestion quest = q.createCalibrationQuestion("UnzUnzUnz");
			task.addCalibrationQuestion(quest);

			Logger.debug(quest.getID());

			assertEquals(task.getMutableCalibrationQuestion(0).getID(), quest.getID());
			q.deleteCalibrationQuestion(quest.getID());
		}  catch (DatabaseException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
	}
	@Test
	public void controlQuestion() {
		try {
			MutableControlQuestion quest = e.addControlQuestion();
			task.addControlQuestion(quest);
			assertEquals(task.getMutableControlQuestion(0).getID(), quest.getID());
			e.removeControlQuestion(quest.getID());
		}  catch (DatabaseException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	@Test
	public void next() {

	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Diese Methode testet, ob der Next Knopf angezeigt wird oder nicht (setzen und holen aus der Datenbank).
	 */
	@Test
	public void setGetNextButtonTest() {
		try {
			testTC.setNextButton(true);
			assertTrue(testTC.nextButtonExists());
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	/**
	 * Diese Methode testet, ob der Again Knopf angezeigt wird oder nicht (setzen und holen aus der Datenbank).
	 */
	@Test
	public void setGetAgainButtonTest() {
		try {
			testTC.setAgainButton(true);
			assertTrue(testTC.againButtonExists());
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	/**
	 * Diese Methode testet, ob der Submit Knopf angezeigt wird oder nicht (setzen und holen aus der Datenbank).
	 */
	@Test
	public void setGetSubmitButtonTest() {
		try {
			testTC.setSubmitButton(true);
			assertTrue(testTC.submitButtonExists());
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	/**
	 * Diese Methode setzt die aktuelle Aufgabe auf eine Kreativaufgabe und holt die aktuelle Aufgabe dann von der Datenbank.
	 */
	@Test
	public void setGetCurrentCreativeTaskTest() {
		try {
			MutableCreativeTask creativeTask = testExp.getCreativeTask();
			testTC.setCurrentTask(0);
			assertTrue(creativeTask.equals(testTC.getCurrentTask()));
			assertTrue(testTC.getMutableCreativeTask(0).equals(creativeTask));
			assertTrue(testTC.getCurrentTaskIndex() == 0);
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	/**
	 * Diese Methode setzt die aktuelle Aufgabe auf eine Bewertungsaufgabe und holt die aktuelle Aufgabe dann von der Datenbank.
	 */
	@Test
	public void setGetCurrentRatingTaskTest() {
		try {
			MutableRatingTask ratingTask = testExp.addRatingTask();
			testTC.addRatingTask(ratingTask);
			testTC.setCurrentTask(1);
			assertTrue(testTC.getCurrentTask().equals(ratingTask));
			assertTrue(testTC.getMutableRatingTask(1).equals(ratingTask));
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	/**
	 * Diese Methode prüft ob das Holen der Anzahl der Aufgaben korrekt abläuft.
	 */
	@Test
	public void getTaskCountTest() {
		try {
			testTC.addCreativeTask((MutableCreativeTask)testAss.getExperiment().getCreativeTask());
			testTC.addCreativeTask((MutableCreativeTask)testAss.getExperiment().getCreativeTask());
			testTC.addRatingTask(testExp.addRatingTask());
			testTC.addRatingTask(testExp.addRatingTask());
			assertTrue(testTC.getTaskCount() == 5);
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	/**
	 * Diese Methode prüft ob das Holen aller Aufgaben korrekt abläuft.
	 */
	@Test
	public void getTasksTest() {
		try {
			List<Task> tasks = new ArrayList<Task>();
			tasks.add(testExp.getCreativeTask());
			tasks.add(testExp.addRatingTask());
			tasks.add(testExp.addRatingTask());
			tasks.add(testExp.addRatingTask());
			Iterator<Task> it = tasks.iterator();
			for(Task t : testTC.getTasks()) {
				assertTrue(t.equals(it.next()));
			}
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	/**
	 * Diese Methode prüft das Setzen der Antwort auf einer Kreaqtivaufgabe.
	 */
	@Test
	public void answerCreativeTaskAtTest() {
		try {
			assertTrue(testTC.answerCreativeTaskAt(0).getCreativeTask().equals(testExp.getCreativeTask()));
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	/**
	 * Diese Methode prüft das Setzen der Bewertung auf einer Bewertungsaufgabe zur Aufgabenkonstellation.
	 */
	@Test
	public void addRatingToRatingTaskAtTest() {
		try {
			MutableRatingTask rt = testExp.addRatingTask();
			testTC.addRatingTask(rt);
			MutableRating rat = testTC.addRatingToRatingTaskAt(1);
			assertTrue(rt.getRatings().iterator().next().equals(rat));
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	/**
	 * Diese Methode prüft das Holen aller Bewertungen der Aufgabenkonstellation.
	 */
	@Test
	public void getRatingsTest() {
		try {
			MutableRatingTask ratingTask = testExp.addRatingTask();
			testTC.addRatingTask(ratingTask);
			MutableRating rating = testTC.addRatingToRatingTaskAt(1);
			assertTrue(testTC.getRatings().iterator().next().equals(rating));
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	/**
	 * Diese Methode prüft das Holen aller Antworten der Aufgabenkonstellation.
	 */
	@Test
	public void getAnswersTest() {
		try {
			MutableAnswer answer = testTC.answerCreativeTaskAt(0);
			assertTrue(testTC.getAnswers().iterator().next().equals(answer));
		} catch (DatabaseException e) {
			e.printStackTrace();
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	/**
	 * Diese Methode prüft das Holen des Assignments von der Aufgabenkonstellation aus.
	 */
	@Test
	public void getAssignmentTest() {
		try {
			assertTrue(testAss.equals(testTC.getAssignment()));
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}
	@Test
	public void answerCalibQuestAtString() {
		try {
			MutableCalibrationQuestion quest = q.createCalibrationQuestion("My IDxxxx");
			
			task.addCalibrationQuestion(quest);
			Logger.debug(quest.getID());
			assertEquals(quest.addAnswer().getCalibrationQuestion().getID(), quest.getID());
			assertEquals(quest.addAnswer().getCalibrationQuestion().getID(), quest.getID());
			MutableCalibrationAnswer a= task.answerCalibrationQuestionAt(quest.getID());
			Logger.debug(a.getCalibrationQuestion().getID());
			assertEquals(a.getCalibrationQuestion().getID(), quest.getID());
			
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	@Test
	public void answerCalibQuestAtInt() {
		try {
			MutableCalibrationQuestion quest = q.createCalibrationQuestion("My ID is differentasdasd");
			
			task.addCalibrationQuestion(quest);
			Logger.debug(quest.getID());
			assertEquals(quest.addAnswer().getCalibrationQuestion().getID(), quest.getID());
			assertEquals(quest.addAnswer().getCalibrationQuestion().getID(), quest.getID());
			MutableCalibrationAnswer a= task.answerCalibrationQuestionAt(0);
			Logger.debug("POPO====== " + a.getCalibrationQuestion().getID());
			assertEquals(a.getCalibrationQuestion().getID(), quest.getID());
			
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	@Test
	public void answerControlQuestAtInt() {
		try {
			MutableControlQuestion quest = e.addControlQuestion();
			
			task.addControlQuestion(quest);
			Logger.debug(quest.getID());
			//assertEquals(quest.addAnswer().getControlQuestion().getID(), quest.getID());
			assertEquals(quest.addAnswer().getControlQuestion().getID(), quest.getID());
			MutableControlAnswer a= task.answerControlQuestionAt(0);
			Logger.debug("POPO====== " + a.getControlQuestion().getID());
			//assertEquals(a.getControlQuestion().getID(), quest.getID());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	@Test
	public void addRatingRating() {
		try {
			MutableRatingTask t = e.addRatingTask();
			task.addRatingTask(t);
			assertEquals(length(task.getRatings()), 0);
			MutableRating rat = task.addRatingToRatingTaskAt(0);
			assertEquals(length(task.getRatings()), 1);
			for(Rating r : task.getRatings()) {
				assertEquals(r.getID(), rat.getID());
			}
		} catch (DatabaseException e) {
			
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	@Test
	public void addRatingRatingRating() {
		try {
			MutableRatingTask t = e.addRatingTask();
			task.addRatingTask(t);
			assertEquals(length(task.getRatings()), 0);
			MutableRating rat = task.addRatingToRatingTaskAt(t.getID());
			assertEquals(length(task.getRatings()), 1);
			for(Rating r : task.getRatings()) {
				assertEquals(r.getID(), rat.getID());
			}
		} catch (DatabaseException e) {
			
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test (expected=DatabaseException.class)
	public void moreThanTasks() throws DatabaseException {
			task.getCurrentTask();
	}
	
	@Test
	public void creativeTaskAt() throws DatabaseException {
		MutableCreativeTask tomboya = testExp.addCreativeTask();
		task.addCreativeTask(tomboya);
		assertEquals(task.answerCreativeTaskAt(tomboya.getID()).getCreativeTask().getID(), tomboya.getID());
	}
}

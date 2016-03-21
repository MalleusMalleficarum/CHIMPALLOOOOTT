package edu.kit.ipd.creativecrowd.persistentmodel;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.junit.Test;

import edu.kit.ipd.creativecrowd.mutablemodel.MutableAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableCreativeTask;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRating;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRatingTask;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.Task;

public class PersistentTaskConstellationTest extends PersistentAssignmentBefore {

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
}

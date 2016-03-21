package edu.kit.ipd.creativecrowd.persistentmodel;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import edu.kit.ipd.creativecrowd.mutablemodel.MutableAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRating;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRatingTask;

public class PersistentRatingTaskTest extends PersistentAssignmentBefore {

	private MutableRatingTask testRatingTask;

	/**
	 * Ergänzt eine Bewertungsaufgabe zum Experiment und fügt die Bewertungsaufgabe zur Aufgabenkonstellation hinzu.
	 */
	@Before
	public void setUpRatingTask() {
		try {
			testRatingTask = testExp.addRatingTask();
			testTC.addRatingTask(testRatingTask);
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	/**
	 * Diese Methode ergänzt eine Antwort zum RatingTask und holt diese dann von der Datenbank.
	 */
	@Test
	public void answersToBeRatedTest() {
		try {
			MutableAnswer answer = testTC.answerCreativeTaskAt(0);
			testRatingTask.addAnswerToBeRated(answer);
			assertTrue(testRatingTask.getAnswerToBeRated(answer.getID()).equals(answer));
			assertTrue(testRatingTask.getAnswersToBeRated().iterator().next().equals(answer));
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	/**
	 * Diese Methode ergänzt eine Bewertung zur Bewertungsaufgabe und holt diese wieder aus der Datenbank heraus.
	 */
	@Test
	public void getRatingTests() {
		try {
			MutableRating rating = testTC.addRatingToRatingTaskAt(1);
			assertTrue(testRatingTask.getRatings().iterator().next().equals(rating));
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}
}

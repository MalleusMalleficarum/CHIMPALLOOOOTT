package edu.kit.ipd.creativecrowd.persistentmodel;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import edu.kit.ipd.creativecrowd.mutablemodel.MutableAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRating;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRatingOption;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRatingTask;

public class PersistentRatingTest extends PersistentAssignmentBefore {

	private MutableRating testRating;
	private MutableRatingTask testRatingTask;

	/**
	 * Ergänzt eine Bewertungsaufgabe zum Experiment und fügt die Bewertungsaufgabe zur Aufgabenkonstellation hinzu.
	 * Zu dieser Bewertungsaufgabe und der zugehörigen Aufgabenkonstellation wird eine Bewertung hinzugefügt.
	 */
	@Before
	public void setUpRating() {
		try {
			testRatingTask = testExp.addRatingTask();
			testTC.addRatingTask(testRatingTask);
			testRating = testTC.addRatingToRatingTaskAt(1);
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	/**
	 * Diese Methode testet den Text einer Bewertung (setzen und holen aus der Datenbank).
	 */
	@Test
	public void textTest() {
		try {
			testRating.setText("text!");
			assertTrue(testRating.getText().contentEquals("text!"));
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	/**
	 * Diese Methode wählt eine Bewertungsoption einer Bewertung aus und holt sie aus der Datenbank.
	 */
	@Test
	public void ratingOptionTest() {
		try {
			MutableRatingOption option = testExp.addRatingOption();
			testRating.setRatingOption(option);
			assertTrue(testRating.getSelectedRatingOption().equals(option));
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	/**
	 * Diese Methode testet die endgültige Bewertungshöhe der Bewertung (setzen und holen aus der Datenbank).
	 */
	@Test
	public void finalQualityIndexTest() {
		try {
			testRating.setFinalQualityIndex(0.7356F);
			assertTrue(testRating.getFinalQualityIndex() == 0.7356F);
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	/**
	 * Diese Methode testet mit einer hinzugefügten Antwort, ob die richtige Antwort in der Bewertung gesetzt wird.
	 */
	@Test
	public void getAnswerTest() {
		try {
			MutableAnswer testAnswer = testTC.answerCreativeTaskAt(0);
			testAnswer.addRating(testRating);
			assertTrue(testAnswer.equals(testRating.getAnswer()));
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}
}

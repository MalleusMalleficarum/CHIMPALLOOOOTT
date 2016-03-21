package edu.kit.ipd.creativecrowd.persistentmodel;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import edu.kit.ipd.creativecrowd.mutablemodel.MutableRatingOption;

public class PersistentRatingOptionTest extends PersistentAssignmentTest {

	private MutableRatingOption testRatingOption;

	/**
	 * Ergänzt eine Bewertungsoption auf dem Experiment.
	 */
	@Before
	public void setUpRatingOption() {
		try {
			testRatingOption = testExp.addRatingOption();
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	/**
	 * Diese Methode testet den Wert der ausgehenden Bezahlung (setzen und holen aus der Datenbank).
	 */
	@Test
	public void valueTest() {
		try {
			testRatingOption.setValue(0.734F);
			assertTrue(testRatingOption.getValue() == 0.734F);
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	/**
	 * Diese Methode testet den Text der ausgehenden Bezahlung (setzen und holen aus der Datenbank).
	 */
	@Test
	public void textTest() {
		try {
			testRatingOption.setText("text!");
			assertTrue(testRatingOption.getText().contentEquals("text!"));
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}
}

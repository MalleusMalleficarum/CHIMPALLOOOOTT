package edu.kit.ipd.creativecrowd.persistentmodel;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import edu.kit.ipd.chimpalot.util.Logger;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRating;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRatingTask;

public class PersistentAnswerTest extends PersistentAssignmentBefore {

	private MutableAnswer testAnswer;

	/**
	 * Ergänzt eine Antwort auf die Kreativaufgabe an der Stelle 0 in der gegebenen Aufgabenkonstellation.
	 */
	@Before
	public void setUpAnswer() {
		try {
			Logger.debug("+++++: " +testTC.getID());
			
			testAnswer = testTC.answerCreativeTaskAt(0);
		} catch (DatabaseException e) {
			e.printStackTrace();
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	/**
	 * Diese Methode testet den Antworttext eines Workers (setzen und holen aus der Datenbank).
	 */
	@Test
	public void textTest() {
		try {
			testAnswer.setText("text!");
			assertTrue(testAnswer.getText().contentEquals("text!"));
		} catch (DatabaseException e) {
			e.printStackTrace();
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	/**
	 * Diese Methode testet die endgültige Antwortbewertungen aller Worker (setzen und holen aus der Datenbank).
	 */
	@Test
	public void finalQualityIndexTest(){
		try {
			testAnswer.setFinalQualityIndex(0.734F);
			assertTrue(testAnswer.getFinalQualityIndex() == 0.734F);
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	/**
	 * Diese Methode testet, ob die Antwort bewertet ist (setzen und holen aus der Datenbank).
	 */
	@Test
	public void isRatedTest() {
		try {
			testAnswer.markAsRated();
			assertTrue(testAnswer.isSufficientlyRated());
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}


	/**
	 * Erzeugt eine weitere Antwort und vergleicht deren Timestamps.
	 * Ein Fehler bei Ungleichheit oder bei einer Exception generiert.
	 **/
	@Test
	public void getTimestampTest() {
		try {
			MutableAnswer ans = testTC.answerCreativeTaskAt(0);

			testAnswer.getTimestampBegin().equals(ans.getTimestampBegin());

		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}
	
	/**
	 * Prüft ob zwei Antworten verschiedene Antworten auch als solches erkannt werden, gleiches bei Gleichheit 
	 **/
	@Test
	public void equalsTest() {
		try {
			MutableAnswer ans = testTC.answerCreativeTaskAt(0);
			assertTrue(testAnswer.equals(testAnswer));
			assertTrue(!testAnswer.equals(ans));

		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	/**
	 * erstellt ein Rating (erstellt dafür einen RatingTask) und fügt das Rating der Test-Antwort hinzu.
	 * prüft anschließend, ob sich das Rating unter den zurückgegebenen zu der Test-Antwort gehörenden Ratings befindet.
	 * 
	 * **/
	@Test
	public void getAddRatingTest() throws DatabaseException {
		try {
			assertTrue(!testAnswer.getRatings().iterator().hasNext());
			MutableRatingTask rt = testExp.addRatingTask();
			testTC.addRatingTask(rt);
			MutableRating rat = testTC.addRatingToRatingTaskAt(1);
			testAnswer.addRating(rat);
			boolean equ = false;
			for(MutableRating it : testAnswer.getRatings()) {
				equ = it.equals(rat);
				if(equ) break;
			}
			assertTrue(equ);

		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	/**
	 *Setzt die Answer auf submitted und prüft, ob sie submitted ist.
	 **/
	@Test
	public void isSetSubmittedTest()   {
		try{
			assertTrue(!testAnswer.isSubmitted());

		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	/**
	 * Prüft, ob die Methode getCreativeTask den testCreativeTask zurückgibt
	 */
	@Test
	public void getCreativeTask()   {
		try {
			assertTrue(testAnswer.getCreativeTask().equals(testExp.getCreativeTask()));
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}
}

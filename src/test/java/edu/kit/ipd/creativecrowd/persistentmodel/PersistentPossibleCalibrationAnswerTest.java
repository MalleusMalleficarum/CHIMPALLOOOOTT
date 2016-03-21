package edu.kit.ipd.creativecrowd.persistentmodel;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.kit.ipd.chimpalot.util.GlobalApplicationConfig;
import edu.kit.ipd.chimpalot.util.Logger;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableCalibrationQuestion;
import edu.kit.ipd.creativecrowd.mutablemodel.MutablePossibleCalibrationAnswer;

public class PersistentPossibleCalibrationAnswerTest {
	
	MutablePossibleCalibrationAnswer ans;
	static MutableCalibrationQuestion quest;
	static PersistentCalibrationQuestionRepo rep;
	@BeforeClass
	public static void setUpBeforeClass()  {
		GlobalApplicationConfig.configure(true);
		try {
			rep = new PersistentCalibrationQuestionRepo();
			quest = rep.createCalibrationQuestion("Der erste Mensch der herausfand dass Milch trinkbar ist war sehr durstig");
			quest.setQuestion("Menschen können unterwasser überleben, aber nicht sehr lange");
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}

	@AfterClass
	public static void tearDownAfterClass()  {
		try {
			rep.deleteCalibrationQuestion("Der erste Mensch der herausfand dass Milch trinkbar ist war sehr durstig");
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	/**
	 * Initialises the answer, every test has this answer
	 */
	@Before
	public void setUp() {
		try {
			ans = quest.addPossibleAnswer();
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
		
	}
	/**
	 * destroys the answer by destroying the question
	 */
	@After
	public void tearDown() {
		try {
			rep.deleteCalibrationQuestion("Der erste Mensch der herausfand dass Milch trinkbar ist war sehr durstig");
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	/**
	 * Nonset value should be false;
	 */
	@Test
	public void isTrueWithoutSet() {
		try {
			assertFalse(ans.getIsTrue());
			
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void ValueTrueAfterSet() {
		try {
			ans.setIsTrue(true);
			assertTrue(ans.getIsTrue());
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void textWithoutInit() {
		try {
			assertEquals(ans.getText(), null);
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void textWithInit() {
		try {
			ans.setText("Pi ist das verhältnis des Kreisumfangs zum Durchmesser im euklidischen Raum");
			assertEquals(ans.getText(), "Pi ist das verhältnis des Kreisumfangs zum Durchmesser im euklidischen Raum");
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void onlyOneQuestion() {
		try {
			MutableCalibrationQuestion q = ans.getCalibrationQuestion();
			for(MutablePossibleCalibrationAnswer a : q.getPossibleAnswers()) {
				assertEquals(a.getID(), ans.getID());
				assertEquals(a.getText(), null);
			}
				
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void isTheQuestionTheQuestion() {
		try {
			Logger.debug("#############################");
			MutableCalibrationQuestion dailyQuest = rep.createCalibrationQuestion("Maus mit Hasenohren");
			
			dailyQuest.setQuestion("Ich bin toll");
			Logger.debug("Q: "+ dailyQuest.getID());
			Logger.debug("Q: "+ dailyQuest.getQuestion());
			MutablePossibleCalibrationAnswer answer = dailyQuest.addPossibleAnswer();
			
			
			Logger.debug(answer.getCalibrationQuestion().getQuestion());
			Logger.debug("#############################");
			rep.deleteCalibrationQuestion("Maus mit Hasenohren");
			
			
		} catch (DatabaseException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
	}

}

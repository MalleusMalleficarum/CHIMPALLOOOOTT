package edu.kit.ipd.creativecrowd.persistentmodel;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import edu.kit.ipd.creativecrowd.mutablemodel.MutableAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableCreativeTask;

public class PersistentCreativeTaskTest extends PersistentAssignmentBefore {

	private MutableCreativeTask testCreativeTask;

	
	
	

	/**
	 * gettet den creativeTask von der testTC
	 */
	@Before
	public void setUpCreativeTask() {
		try {
			PersistentCalibrationQuestionRepo rep = new PersistentCalibrationQuestionRepo();
			rep.deleteCalibrationQuestion(rep.createCalibrationQuestion("id").getID());
			new PersistentConfigModelRepo();
			testCreativeTask = (MutableCreativeTask) testTC.getCurrentTask();
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	/**
	 * settet eine PictureURL und überprüft anschließend die gegettete auf Gleichheit 
	 */
	@Test
	public void pictureURLTest() {
		try {
			testCreativeTask.setPicture("http://www.gggggl.de/", "http://www.ggggggl.de/");
			String url = testCreativeTask.getPictureURL();
			assertTrue(url.equals("http://www.gggggl.de/"));
			String licenseUrl = testCreativeTask.getPictureLicenseURL();
			assertTrue(licenseUrl.equals("http://www.ggggggl.de/"));
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}
	/**
	 * settet eine Beschreibung und überprüft anschließend die gegettete auf Gleichheit 
	 */
	@Test
	public void descriptionTest() {
		try {
			testCreativeTask.setDescription("description");
			assertTrue(testCreativeTask.getDescription().contentEquals("description"));
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}
	/**
	 * fügt mehrere Antworten zum CreativeTask hinzu und prüft die Anzahl der danach gegetteten auf Korrektheit
	 */
	@Test
	public void getAnswersTest() {
		try {
			MutableAnswer testAnswer = testTC.answerCreativeTaskAt(0);
			assertTrue(testAnswer.getCreativeTask().equals(testCreativeTask));
			testTC.answerCreativeTaskAt(0);
			testTC.answerCreativeTaskAt(0);
			testTC.answerCreativeTaskAt(0);
			testTC.answerCreativeTaskAt(0);
			testTC.answerCreativeTaskAt(0);
			int i = 0;
			for(@SuppressWarnings("unused") MutableAnswer ans : testAnswer.getCreativeTask().getAnswers()) {
				i++;
			}
			assertTrue(i == 6);
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}
	
	@Test
	public void getSetAccordingRatingTaskDescription() {
		try {
			testCreativeTask.setAccordingRatingTaskDescription("test!");
			assertTrue(testCreativeTask.getAccordingRatingTaskDescription().contentEquals("test!"));
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}
}

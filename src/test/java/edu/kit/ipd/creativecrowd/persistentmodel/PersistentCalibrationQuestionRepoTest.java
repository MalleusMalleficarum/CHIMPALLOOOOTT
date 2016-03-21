package edu.kit.ipd.creativecrowd.persistentmodel;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.kit.ipd.chimpalot.util.GlobalApplicationConfig;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableCalibrationQuestion;
/**
 * 
 * @author Robin
 *
 */
public class PersistentCalibrationQuestionRepoTest {
	static PersistentCalibrationQuestionRepo rep;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		File file = new File("CreativeCrowd.db");
		file.delete();
		GlobalApplicationConfig.configure(true);
		rep = new PersistentCalibrationQuestionRepo();
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		List<MutableCalibrationQuestion> que = rep.loadAllCalibrationQuestions();
		for(MutableCalibrationQuestion q : que) {
			rep.deleteCalibrationQuestion(q.getID());
		}
	}

	@Test (expected=IDAlreadyUsedException.class)
	public void doubleID() throws IDAlreadyUsedException, DatabaseException {
		
			rep.createCalibrationQuestion("Hallo");
			rep.createCalibrationQuestion("Hallo");		
	}
	@Test 
	public void deleteSomethingNotThere() {
		try {
			rep.deleteCalibrationQuestion("HALLO WELT");
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	private int length(Iterable<?> o) {
		int i = 0;
		for(@SuppressWarnings("unused") Object os : o) {
			i++;
		}
		return i;
	}
	@Test
	public void theQuestionIsTheSame() {
		try {
			MutableCalibrationQuestion quest = rep.createCalibrationQuestion("Ich darf wirklich selbst ids vergeben :O");
			MutableCalibrationQuestion loaded = rep.loadCalibrationQuestion("Ich darf wirklich selbst ids vergeben :O");
			assertEquals(quest.getQuestion(), loaded.getQuestion());
			assertEquals(quest.getID(), loaded.getID());
			assertEquals(length(quest.getCalibrationAnswers()), length(loaded.getCalibrationAnswers()));
			assertEquals(length(quest.getPossibleAnswers()), length(loaded.getPossibleAnswers()));
		
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void iCanLoadMoreThanOne() {
		
		try {
			assertNotNull(rep.createCalibrationQuestion("Wurzel aus Seil gleich Schnur"));
			assertNotNull(rep.createCalibrationQuestion("Ratten können nicht spucken"));
			assertEquals(rep.loadAllCalibrationQuestions().size(), 2);
		}  catch (DatabaseException e) {
			fail(e.getMessage());
	}
	}
	@Test (expected = DatabaseException.class)
	public void loadSomethingNotThere() throws IDNotFoundException, DatabaseException {
		
		rep.loadCalibrationQuestion("Der Weltraum existiert nicht");
		
	}
}

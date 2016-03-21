package edu.kit.ipd.creativecrowd.persistentmodel;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.kit.ipd.chimpalot.util.GlobalApplicationConfig;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableCalibrationAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableCalibrationQuestion;
import edu.kit.ipd.creativecrowd.mutablemodel.MutablePossibleCalibrationAnswer;
/**
 * 
 * @author Robin
 *
 */
public class PersistentCalibrationQuestionTest {
	static PersistentCalibrationQuestionRepo rep;
	static PersistentExperimentRepo exp;
	MutableCalibrationQuestion quest;
	
	@BeforeClass
	public static void setUpBeforeClass() {
		File file = new File("CreativeCrowd.db");
		file.delete();
		GlobalApplicationConfig.configure(true);
		try {
			rep = new PersistentCalibrationQuestionRepo();
			exp = new PersistentExperimentRepo();
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}

	@AfterClass
	public static void tearDownAfterClass() {
	}

	@Before
	public void setUp() {
		try {
			quest = rep.createCalibrationQuestion("Kopiere einen Spiegel und du erhälst einen Fotokopierer");
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}

	@After
	public void tearDown() {
		try {
			for(MutableCalibrationQuestion q : rep.loadAllCalibrationQuestions()) {
				rep.deleteCalibrationQuestion(q.getID());
			}
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void readingStuffThatIsNotWritten() {
		try {
			assertNotNull(quest.getCalibrationAnswers());
			//TODO assertNotNull(quest.getExperiments()); //Causes severe problems, something is wrong in the statement ambiguous column name
			assertNotNull(quest.getPossibleAnswers());
			
			
		} catch (DatabaseException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	@Test
	public void placeStuff() {
		try {
			quest.addExperiment("hallo Welt");
			//TODO kann auch hier nix auslesen.
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	
	
	@SuppressWarnings("rawtypes")
	private int length(Iterable o) {
		int i = 0;
		for(@SuppressWarnings("unused") Object os : o) {
			i++;
		}
		return i;
	}
	
	
	@Test
	public void noIdeaWhatThisTestDoes() {
		try {
			quest.addPossibleAnswer();
			for(MutablePossibleCalibrationAnswer a : quest.getPossibleAnswers()) {
				
				
				a.setText("nicht von dieser Welt");
				assertEquals(a.getText(), "nicht von dieser Welt");
				
				
			}
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void questionShouldBeStored() {
		try {

			quest.setQuestion("myQuest");
			
			assertEquals(quest.getQuestion(), "myQuest");
			
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void removeNotPresentAnswer() {
		try {
			quest.removePossibleAnswer("Soryy, invalid");
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void removeAnswer() {
		try {
			MutablePossibleCalibrationAnswer ans = quest.addPossibleAnswer();
			quest.removePossibleAnswer(ans.getID());
			assertEquals(length(quest.getPossibleAnswers()), 0);
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void addAnswer() {
		try {
			quest.setQuestion("myGod");

			MutableCalibrationAnswer ans = quest.addAnswer();

			MutableCalibrationQuestion q = ans.getCalibrationQuestion();
			assertEquals(q.getQuestion(), "myGod");


			
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void addPossibleAnswer() {
		try {
			MutablePossibleCalibrationAnswer ans = quest.addPossibleAnswer();
			ans.setText("Die milliardste Stelle von pi ist 9");
			MutableCalibrationQuestion q = ans.getCalibrationQuestion();
			for(MutablePossibleCalibrationAnswer a : q.getPossibleAnswers()) {
				assertEquals(a.getText(), "Die milliardste Stelle von pi ist 9");
			}
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void addRemovePossibleAnswer() {
		try {
			MutablePossibleCalibrationAnswer ans = quest.addPossibleAnswer();
			quest.removePossibleAnswer(ans.getID());
			assertEquals(length(quest.getPossibleAnswers()), 0);
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	/**
	 * This needs fixing, i have no idea what i attempted with that
	 */
	@Test
	public void hereMightShitHappen() {
		try {
			MutablePossibleCalibrationAnswer ans = quest.addPossibleAnswer();
			assertEquals(length(quest.getCalibrationAnswers()), 1);
			ans.setCalibrationQuestion("bullshit");
			
			quest.removePossibleAnswer(ans.getID());
			assertEquals(length(quest.getCalibrationAnswers()), 0);
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
//	@Test
//	public void addExperimentAndRemove() {
//		try {
//			MutableExperiment e = exp.createExperiment("HALLO");
//			Logger.debug(e.getID());
//			quest.addExperiment(e.getID());
//			for(Experiment ex : quest.getExperiments()) {
//				assertEquals(ex.getID() , e.getID());
//			}
//			Logger.debug(e.getConfig().getID());
//			
//		} catch (DatabaseException e) {
//			e.printStackTrace();
//			fail(e.getMessage());
//		}
//	}
	

}

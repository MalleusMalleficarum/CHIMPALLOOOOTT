package edu.kit.ipd.creativecrowd.persistentmodel;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.kit.ipd.chimpalot.jsonclasses.ConfigModelJson;
import edu.kit.ipd.chimpalot.util.GlobalApplicationConfig;
import edu.kit.ipd.creativecrowd.crowdplatform.AssignmentId;
import edu.kit.ipd.creativecrowd.crowdplatform.PlatformIdentity;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAssignment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableCalibrationQuestion;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableControlQuestion;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableCreativeTask;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRatingTask;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableStats;
import edu.kit.ipd.creativecrowd.readablemodel.Assignment;
import edu.kit.ipd.creativecrowd.readablemodel.ConfigModel;
import edu.kit.ipd.creativecrowd.readablemodel.ExperimentType;
/**
 * Naja eigentlich hab ich hier alles vorher vorhandene gelöscht
 * @author unknown author + Robin 
 *
 */
public class PersistentExperimentTest {
	static PersistentExperimentRepo testRep;
	static PersistentConfigModelRepo rep;
	ConfigModel conf;
	MutableExperiment exp;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		GlobalApplicationConfig.configure(true);
		try {
			File file = new File("CreativeCrowd.db");
			file.delete();
			testRep = new PersistentExperimentRepo();
			rep = new PersistentConfigModelRepo();

		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() {
		try {
			ConfigModelJson j = new ConfigModelJson();
			j.setID("JETZT HAB ICH EINE ID");
			conf = rep.createConfigModel(j, j.getID(), null);
			exp = testRep.createExperiment("Bad Apple", conf);
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}

	@After
	public void tearDown() throws Exception {
		try {
			List<MutableExperiment> list = testRep.loadAllExperiments();
			for(MutableExperiment e : list) {
				testRep.deleteExperiment(e.getID());
				rep.deleteConfigModel(conf.getID());
			}
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@SuppressWarnings("rawtypes")
	private int length(Iterable iter) {
		int i = 0;
		for(@SuppressWarnings("unused") Object o : iter) {
			i++;
		}
		return i;
	}

	@Test (expected=DatabaseException.class)
	public void nullAssignment() throws DatabaseException {

		exp.getAssignment(null);
		
	}
	@Test (expected=DatabaseException.class)
	public void invalidAssignment() throws DatabaseException {

		exp.getAssignment("Der Karpfen ist der giftigste Fisch der Welt");
		
	}
	@Test
	public void blankAssigments() {
		try {
			assertEquals(length(exp.getAssignments()), 0);
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test (expected = DatabaseException.class)
	public void nullMturkID() throws DatabaseException {
		exp.getAssignmentWithMturkId(null);
	}
	@Test
	public void invalidMturkID() {
		try {
			Assignment m = exp.getAssignmentWithMturkId(new AssignmentId("Ich bin ungültig"));
			assertEquals(m, null);
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void allParametersDefault() {
		
		try {
			assertEquals(exp.getBasicPaymentAnswerCents(PlatformIdentity.PyBossa), 0);
			assertEquals(exp.getBasicPaymentAnswerCents(PlatformIdentity.MTurk), 0);
			assertEquals(exp.getBasicPaymentHITCents(PlatformIdentity.PyBossa), 0);
			assertEquals(exp.getBasicPaymentHITCents(PlatformIdentity.MTurk), 0);
			assertEquals(exp.getBasicPaymentRatingCents(PlatformIdentity.PyBossa),0);
			assertEquals(exp.getBasicPaymentRatingCents(PlatformIdentity.MTurk), 0);
			assertEquals(exp.getBudgetCents(), 0);
			assertEquals(exp.getType(), ExperimentType.Undefined);
			assertEquals(exp.getHitID(), null);
			assertEquals(exp.getRatingTaskViewClass(), null);
			assertEquals(exp.getDescription(), null);
			assertEquals(exp.getHITDescription(), null);
			assertEquals(exp.getHITTitle(), null);
			assertEquals(exp.getMaxNumberOfAnswersPerAssignment(), 0);
			assertEquals(exp.getMaxNumberOfRatingsPerAssignment(), 0);
			assertEquals(exp.isFinished(), false);
			
			
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test (expected = DatabaseException.class)
	public void creativeTaskNotSet() throws DatabaseException {
		exp.getCreativeTask();
	}
	@Test 
	public void getStatsEmpty(){
		try {
			MutableStats s = exp.getStats();
			assertNotNull(exp.getStrategyParams());
			assertEquals(length(exp.getStrategyParams().entrySet()), 0);
			assertNotNull(s);
			assertEquals(s.getAcceptedHitCount(), 0);
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void allComplexParametersEmpty() {
		try {
			assertEquals(length(exp.getBlockedWorkers()), 0);
			assertEquals(length(exp.getCalibrationQuestions()), 0);
			assertEquals(length(exp.getControlQuestions()), 0);
			assertEquals(length(exp.getQualifications(PlatformIdentity.PyBossa)), 0);
			assertEquals(length(exp.getQualifications(PlatformIdentity.MTurk)), 0);
			assertEquals(length(exp.getRatingOptions()), 0);
			assertEquals(length(exp.getRatingTasks()), 0);
			assertEquals(length(exp.getTags()), 0);
		} catch (DatabaseException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
	}
	@Test
	public void setAndGetAssignment() {
		
		try {
			MutableAssignment mutex = exp.addAssignment();
			mutex.setSubmitted();
			MutableAssignment compare1= exp.getAssignment(mutex.getID());
			assertTrue(compare1.isSubmitted());
			assertTrue(mutex.isSubmitted());
			assertEquals(mutex.getID(), compare1.getID());
			for(MutableAssignment compare2 : exp.getAssignments()) {
				assertTrue(compare2.isSubmitted());
				assertEquals(compare2.getID(), mutex.getID());
			}
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void setAndGetCalibrationQuestion() {
		try {
			MutableCalibrationQuestion q = exp.addCalibrationQuestion();
			q.setQuestion("Unze");
			int n = 0;
			for(MutableCalibrationQuestion quest : exp.getCalibrationQuestions()) {
				assertEquals(quest.getQuestion(), q.getQuestion());
				assertEquals(q.getID(), quest.getID());
				n++;
			}
			assertEquals(1, n);
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void setAndGetControlQuestion() {
		try {
			MutableControlQuestion q = exp.addControlQuestion();
			q.setQuestion("Ich bin Unze");
			assertEquals(q.getQuestion(), "Ich bin Unze");
			MutableControlQuestion q2 = exp.addControlQuestion();
			q2.setQuestion("Ich bin keine Unze");
			int n = 0;
			for(MutableControlQuestion quest : exp.getControlQuestions()) {
				n++;
				if (n == 1) {
					assertEquals(quest.getQuestion(), "Ich bin Unze");
					assertEquals(q.getID(), quest.getID());
				}
				if (n == 2) {
					assertEquals("Ich bin keine Unze", quest.getQuestion());
					assertEquals(q2.getID(), quest.getID());
				}
			}
			assertEquals(2, n);
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void setAndGetCreativeTask() {
		try {
			MutableCreativeTask task = exp.addCreativeTask();
			task.setDescription("ICHCH");
			assertEquals(task.getDescription(), "ICHCH");
			MutableCreativeTask res = exp.getCreativeTask();
			assertEquals(res.getID(), task.getID());
			assertEquals(res.getDescription(), task.getDescription());
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void setAndGetRatingTask() {
		try {
			MutableRatingTask task = exp.addRatingTask();
			assertEquals(task.getDescription(), null);
			for(MutableRatingTask t : exp.getRatingTasks()) {
				assertEquals(t.getID(), task.getID());
			}
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void setAndGetFinished() {
		try {
			exp.markAsFinished();
			assertTrue(exp.isFinished());
			exp.markExperimentAsSoftFinished();
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
		
	}
	@Test
	public void setAndGetHITID() {
		try {
			exp.setHitID("HAlllo");
			assertEquals(exp.getHitID(), "HAlllo");
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
		
	}
	@Test (expected = DatabaseException.class)
	public void rmvCalibNull() throws DatabaseException {
		exp.removeCalibrationQuestion(null);
	}
	@Test
	public void rmvCalibShit() throws DatabaseException {
		exp.removeCalibrationQuestion("NICHT VON DIESER WELT");
	}
	@Test (expected = DatabaseException.class)
	public void rmvCtrlNull() throws DatabaseException {
		exp.removeControlQuestion(null);
	}
	@Test 
	public void rmvCtrlShit() throws DatabaseException {
		exp.removeControlQuestion("NICHT VON DIESER WELT");
	}
	@Test
	public void rmvCalibValid() {
		try {
			MutableCalibrationQuestion q = exp.addCalibrationQuestion();
			exp.removeCalibrationQuestion(q.getID());
			assertEquals(length(exp.getCalibrationQuestions()), 0);
		} catch (DatabaseException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	@Test
	public void rmvCtrlValid() {
		try {
			MutableControlQuestion q = exp.addControlQuestion();
			exp.removeControlQuestion(q.getID());
			assertEquals(length(exp.getControlQuestions()), 0);
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
}

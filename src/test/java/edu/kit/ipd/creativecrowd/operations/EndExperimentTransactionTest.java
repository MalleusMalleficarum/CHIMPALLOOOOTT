package edu.kit.ipd.creativecrowd.operations;

import static org.junit.Assert.*;

import java.util.HashMap;

import mockit.Mock;
import mockit.MockUp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.kit.ipd.creativecrowd.crowdplatform.AssignmentId;
import edu.kit.ipd.creativecrowd.crowdplatform.ConnectionFailedException;
import edu.kit.ipd.creativecrowd.crowdplatform.IllegalInputException;
import edu.kit.ipd.creativecrowd.crowdplatform.WorkerId;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAssignment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRatingTask;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.transformer.Transformer;

public class EndExperimentTransactionTest {
	MutableExperiment exp;
	EndExperimentTransaction end;
	MockExperiment mock;
	
	@Before
	public void setUp() throws Exception {
		mock = new MockExperiment();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("apoc_class",
				"edu.kit.ipd.creativecrowd.operations.strategies.DefaultAPOC");
		params.put("aqic_class",
				"edu.kit.ipd.creativecrowd.operations.strategies.AverageRating");
		params.put("ftg_class",
				"edu.kit.ipd.creativecrowd.operations.strategies.FeedbackWithoutRatingText");
		params.put("rqic_class",
				"edu.kit.ipd.creativecrowd.operations.strategies.EnsureRatingDiversity");
		params.put("rsd_class",
				"edu.kit.ipd.creativecrowd.operations.strategies.FixedRatingsPerAnswerDecider");
		params.put("tacc_class",
				"edu.kit.ipd.creativecrowd.operations.strategies.NAssignments");
		params.put(
				"tcm_class",
				"edu.kit.ipd.creativecrowd.operations.strategies.FreeformTaskConstellationMutator");
		
		exp = mock.getExperiment();	
		MockExperiment.setStrategyparams(exp, params);
		end = new EndExperimentTransaction();
	}

	@After
	public void tearDown() throws Exception {
		mock.deleteExperiment();
		mock = null;
		exp = null;
		end = null;
	}
	/**
	 * Führt die Methode die run Methode der  EndExperimentTransaction aus und überprüft ob
	 * dabei keine Exception geworfen wird und überprüft ob das Experiment
	 * am Ende als beendet markiert wurde
	 * @throws DatabaseException 
	 */
	@Test
	public void test() throws DatabaseException {
		MutableAssignment a = exp.addAssignment();
		a.setAssignmentID(new AssignmentId("MT123"));
		a.setWorker(new WorkerId("iamnotarobot"));
		a.getTaskConstellation().addCreativeTask(exp.getCreativeTask());
		MutableAnswer an = a.getTaskConstellation().answerCreativeTaskAt(0);
		MutableAssignment as = exp.addAssignment();
		as.setAssignmentID(new AssignmentId("MT124"));
		MutableRatingTask rt = exp.addRatingTask();
		rt.addAnswerToBeRated(an);
		as.getTaskConstellation().addRatingTask(rt);
		as.getTaskConstellation().addRatingTask(rt);
		as.getTaskConstellation().addRatingTask(rt);
		
		try {
			new MockUp<Transformer>(){
				@Mock
				void endHIT(String Hitid) throws DatabaseException{
					
				}
				@Mock
				void approveHIT(AssignmentId AssignmentId, WorkerId wrkid, String feedback) throws IllegalInputException {
					System.out.println("HIT APPROVED");
				}
				@Mock
				void rejectAssignment(AssignmentId AssignmentId, String feedback) throws IllegalInputException {
					
				}
			};
			end.run(exp);
		} catch (ConnectionFailedException | DatabaseException e) {
			fail(e.getMessage());
		}
		catch (IllegalInputException e) {
		try {
			assertTrue(exp.isFinished());
		} catch (DatabaseException f) {
			e.printStackTrace();
		}
		}
	}
	
	@Test
	public void testWithoutAWorker() throws ConnectionFailedException, DatabaseException, IllegalInputException {
		new MockUp<Transformer>(){
			@Mock
			void endHIT(String Hitid) throws DatabaseException{
				
			}
			@Mock
			void approveHIT(AssignmentId AssignmentId, WorkerId wrkid, String feedback) throws IllegalInputException {
				
			}
			@Mock
			void rejectAssignment(AssignmentId AssignmentId, String feedback) throws IllegalInputException {
				
			}
		};
		end.run(exp);
		assertTrue(exp.isFinished());
	}
	

}

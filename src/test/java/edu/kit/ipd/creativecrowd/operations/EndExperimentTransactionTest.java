package edu.kit.ipd.creativecrowd.operations;

import static org.junit.Assert.*;
import mockit.Mock;
import mockit.MockUp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.kit.ipd.creativecrowd.mturk.AssignmentId;
import edu.kit.ipd.creativecrowd.mturk.ConnectionFailedException;
import edu.kit.ipd.creativecrowd.mturk.IllegalInputException;
import edu.kit.ipd.creativecrowd.mturk.MTurkConnection;
import edu.kit.ipd.creativecrowd.mutablemodel.ExperimentRepo;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAssignment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableCreativeTask;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRatingTask;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.ExperimentSpec;

public class EndExperimentTransactionTest {
	MutableExperiment exp;
	EndExperimentTransaction end;
	MockExperiment mock;
	
	@Before
	public void setUp() throws Exception {
		mock = new MockExperiment();
		mock.setStrategyparams();
		exp = mock.getExperiment();	
		end = new EndExperimentTransaction();
	}

	@After
	public void tearDown() throws Exception {
		mock.deleteExperiment();
		mock = null;
		exp =null;
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
		a.getTaskConstellation().addCreativeTask(exp.getCreativeTask());
		MutableAnswer an = a.getTaskConstellation().answerCreativeTaskAt(0);
		MutableAssignment as = exp.addAssignment();
		MutableRatingTask rt = exp.addRatingTask();
		rt.addAnswerToBeRated(an);
		as.getTaskConstellation().addRatingTask(rt);
		as.getTaskConstellation().addRatingTask(rt);
		as.getTaskConstellation().addRatingTask(rt);
		
		try {
			new MockUp<MTurkConnection>(){
				@Mock
				void endHIT(String Hitid) throws DatabaseException{
					
				}
				@Mock
				void approveHIT(AssignmentId AssignmentId, String feedback) throws IllegalInputException {
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
		new MockUp<MTurkConnection>(){
			@Mock
			void endHIT(String Hitid) throws DatabaseException{
				
			}
			@Mock
			void approveHIT(AssignmentId AssignmentId, String feedback) throws IllegalInputException {
				
			}
			@Mock
			void rejectAssignment(AssignmentId AssignmentId, String feedback) throws IllegalInputException {
				
			}
		};
		end.run(exp);
		assertTrue(exp.isFinished());
	}
	

}

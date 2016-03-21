package edu.kit.ipd.creativecrowd.operations;

import static org.junit.Assert.*;

import java.util.HashMap;

import mockit.Mock;
import mockit.MockUp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.kit.ipd.creativecrowd.crowdplatform.AssignmentId;
import edu.kit.ipd.creativecrowd.crowdplatform.WorkerId;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAssignment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableWorker;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.persistentmodel.PersistentWorkerRepo;
import edu.kit.ipd.creativecrowd.transformer.Transformer;

public class SubmitAssignmentTransactionTest {
	MutableAssignment ass;
	MutableExperiment exp;
	SubmitAssignmentTransaction sub;
	MockExperiment mock;
	MockUp<Transformer> mocki;
	MockUp<EndExperimentTransaction> mocka;

	@Before
	public void setUp() throws Exception {
		mock = new MockExperiment();
		exp = mock.getExperiment();
		ass = exp.addAssignment();
		ass.setAssignmentID(new AssignmentId("MT123"));
		ass.setWorker(new WorkerId("iamrobot"));
		PersistentWorkerRepo repo = new PersistentWorkerRepo();
		MutableWorker wrk;
		try {
		   wrk = repo.loadWorker("iamrobot");
		} catch (Exception e) {
			wrk = repo.createWorker("iamrobot");
		}
		wrk.setBlocked(false);
		sub = new SubmitAssignmentTransaction();
		HashMap<String,String> params = new HashMap<String, String>();
		params.put("tacc_class", "edu.kit.ipd.creativecrowd.operations.strategies.NAssignments");
		params.put("apoc_class", "edu.kit.ipd.creativecrowd.operations.strategies.DefaultAPOC");
		params.put("rsd_class", "edu.kit.ipd.creativecrowd.operations.strategies.ConsentRatingDecider");
		MockExperiment.setStrategyparams(exp, params);
	}

	/**
	 * Der Testfall führt die submitAssignmentTransaction aus und stellt sicher,
	 * dass das Assignment nach Ablauf der Methode als submitted markiert ist.
	 */
	@Test
	public void test() throws Exception {
		mocki = new MockUp<Transformer>(){
			@Mock
			void extendAssignmentNumber(int nrOfAssignments, String hitId){
				
			}	
		};
		mocka = new MockUp<EndExperimentTransaction>(){
			@Mock
			void run(MutableExperiment experiment) {
				
			}
		};
		sub.run(ass, exp);
		try {
			assertTrue(ass.isSubmitted());
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
	}
	@After
	public void tearDown() throws Exception {
		mock.deleteExperiment();
		if(mocki != null) {
			try {
		mocki.tearDown();
			} catch(NullPointerException e) {
				
			}
		}
		if(mocka != null) {
			try {
				mocka.tearDown();
			} catch (NullPointerException e) {
				
			}
		}
	}

}

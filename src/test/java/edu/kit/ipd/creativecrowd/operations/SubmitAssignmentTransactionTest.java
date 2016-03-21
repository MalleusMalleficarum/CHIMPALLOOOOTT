package edu.kit.ipd.creativecrowd.operations;

import static org.junit.Assert.*;

import java.util.HashMap;

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
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAssignment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.ExperimentSpec;

public class SubmitAssignmentTransactionTest {
	MutableAssignment ass;
	MutableExperiment exp;
	SubmitAssignmentTransaction sub;
	MockExperiment mock;
	MockUp mocki;
	MockUp mocka;

	@Before
	public void setUp() throws Exception {
		mock = new MockExperiment();
		exp = mock.getExperiment();
		ass = exp.addAssignment();
		sub = new SubmitAssignmentTransaction();
		HashMap<String,String> params = new HashMap();
		params.put("tacc_class", "edu.kit.ipd.creativecrowd.operations.strategies.NAssignments");
		params.put("apoc_class", "edu.kit.ipd.creativecrowd.operations.strategies.DefaultAPOC");
		params.put("rsd_class", "edu.kit.ipd.creativecrowd.operations.strategies.ConsentRatingDecider");
		exp.setStrategyParams(params);
	}

	/**
	 * Der Testfall führt die submitAssignmentTransaction aus und stellt sicher,
	 * dass das Assignment nach Ablauf der Methode als submitted markiert ist.
	 */
	@Test
	public void test() {
		mocki = new MockUp<MTurkConnection>(){
			@Mock
			void extendAssignmentNumber(int nrOfAssignments, String hitId){
				
			}	
		};
		mocka = new MockUp<EndExperimentTransaction>(){
			@Mock
			void run(MutableExperiment experiment) {
				
			}
		};
		try {
			sub.run(ass, exp);
		} catch (DatabaseException | ConnectionFailedException
				| IllegalInputException e) {
			fail(e.getMessage());
		}
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

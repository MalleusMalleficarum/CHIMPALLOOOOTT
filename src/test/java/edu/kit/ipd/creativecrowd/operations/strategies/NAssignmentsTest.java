package edu.kit.ipd.creativecrowd.operations.strategies;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.operations.MockExperiment;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;

public class NAssignmentsTest {
	MutableExperiment exp;
	NAssignments n;
	MockExperiment mock;

	@Before
	public void setUp() throws Exception {
		mock = new MockExperiment();
		MutableExperiment exp = mock.getExperiment();
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("tacc.n", "10");
		n = new NAssignments();

		MockExperiment.setStrategyparams(exp, params);
		n.setParams(params);
	}

	/**
	 * Dieser Testfall überprüft ob die Methode run() des NAssignments
	 * TotalAssignmentCountController das richtige zurückgibt, wenn ein Parameter in 
	 * der config angegeben wurde
	 * 
	 * @throws DatabaseException
	 */
	@Test
	public void testWithParams() throws DatabaseException {
		assertEquals(n.run(exp, 0, 0), 10);
	}

	@After
	public void tearDown() throws DatabaseException {
		mock.deleteExperiment();
	}

}

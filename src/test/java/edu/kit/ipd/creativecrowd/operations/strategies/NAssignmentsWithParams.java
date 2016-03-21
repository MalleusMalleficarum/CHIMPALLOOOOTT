package edu.kit.ipd.creativecrowd.operations.strategies;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.operations.MockExperiment;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;

public class NAssignmentsWithParams {
	MutableExperiment exp;
	NAssignments n;
	MockExperiment mock;

	@Before
	public void setUpTestWithoutParams() throws DatabaseException {
		mock = new MockExperiment();
		MutableExperiment exp = mock.getExperiment();
		n = new NAssignments();
		n.setParams(new HashMap<String, String>());
	}

	/**
	 * Dieser Testfall überprüft, ob die Methode run() des NAssignments
	 * TotalAssignmentCountControllers das richtige zurückliefert, wenn kein
	 * Parameter in der Konfigurationsdatei angegeben wurde
	 * 
	 * @throws DatabaseException
	 */
	@Test
	public void testWithoutParams() throws DatabaseException {
		assertEquals(n.run(exp, 0, 0), 5);
	}

	@After
	public void tearDown2() throws DatabaseException {
		mock.deleteExperiment();

	}

}

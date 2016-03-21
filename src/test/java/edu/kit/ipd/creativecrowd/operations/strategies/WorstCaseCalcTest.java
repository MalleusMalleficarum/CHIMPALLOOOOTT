package edu.kit.ipd.creativecrowd.operations.strategies;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.operations.MockExperiment;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;

public class WorstCaseCalcTest {
	MutableExperiment exp;
	WorstCaseCalc wc;
	MockExperiment mock;

	@Before
	public void setUp() throws Exception {
		mock = new MockExperiment();
		exp = mock.getExperiment();
		wc = new WorstCaseCalc();
		
	}

	@After
	public void tearDown() throws Exception {
		mock.deleteExperiment();
	}
	/**
	 * Dieser Testfall überprüft, ob die Methode run() des WorstCalc TotalAssignmentCountController
	 * das richtige Ergebnis liefert.
	 * @throws DatabaseException
	 */
	@Test
	public void test() throws DatabaseException {
		assertEquals(wc.run(exp, 10, 100), 40);
		
	}

}

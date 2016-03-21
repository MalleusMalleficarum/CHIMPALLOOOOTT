package edu.kit.ipd.creativecrowd.controller;
import static org.junit.Assert.*;

import java.util.Map;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;

public class ExperimentSpecFromConfigUnitTest {
	@Test
	public void expTest(){
		Map<String, Double> ro = new HashMap<String, Double>();
		ExperimentSpecFromConfig exp = new ExperimentSpecFromConfig(10, 20, 65, 12, 2, "Do a backflip","rate the answeers", "http://www.bla.com", "http://www.lorem.com", "Test",
				"Lorem ipsum", null, ro, null, null, null, null, "Hit Ti", 4, 3);
		assertEquals(exp.getBudgetCents(), 10);
		assertEquals(exp.getDescription(), "Lorem ipsum");
		assertEquals(exp.getBasicPaymentHITCents(), 20);
		try{
			assertEquals(exp.getMaxNumberOfAnswersPerAssignment(), 4);
		}catch(DatabaseException e){
			//This cannot happen
			fail("DatabaseException");
		}

	}
}

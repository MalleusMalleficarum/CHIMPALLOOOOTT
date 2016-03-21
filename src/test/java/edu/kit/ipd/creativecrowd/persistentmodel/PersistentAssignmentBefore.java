package edu.kit.ipd.creativecrowd.persistentmodel;

import static org.junit.Assert.fail;

import org.junit.Before;

import edu.kit.ipd.creativecrowd.mutablemodel.MutableAssignment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableTaskConstellation;

public class PersistentAssignmentBefore extends PersistentModelBefore {

	protected MutableAssignment testAss;
	protected MutableTaskConstellation testTC;

	/**
	 * Erstellt vor jedem Testdurchlauf ein neues Test-Assignment 
	 */
	@Before
	public void setUpAssignment() {

		try {
			testAss = testExp.addAssignment();
			testTC  = testAss.getTaskConstellation();
			testExp.addCreativeTask();
			testTC.addCreativeTask(testExp.getCreativeTask());
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}
}

package edu.kit.ipd.creativecrowd.persistentmodel;

import static org.junit.Assert.fail;

import java.io.File;

import org.junit.After;
import org.junit.Before;

import edu.kit.ipd.creativecrowd.crowdplatform.WorkerId;
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
			PersistentWorkerRepo re = new PersistentWorkerRepo();
			
			testAss.setWorker(new WorkerId(re.createWorker(re.connection.generateID("worker")).getID()));
			testTC  = testAss.getTaskConstellation();
			testExp.addCreativeTask();
			testTC.addCreativeTask(testExp.getCreativeTask());
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}
	@After
	public void tearDown() {
		//TODO actually implement.
			File file = new File("CreativeCrowd.db");
			file.delete();
	}
}

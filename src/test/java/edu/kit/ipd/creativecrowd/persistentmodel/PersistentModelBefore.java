package edu.kit.ipd.creativecrowd.persistentmodel;

import static org.junit.Assert.fail;

import java.io.File;

import org.junit.After;
import org.junit.Before;

import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.util.GlobalApplicationConfig;

public class PersistentModelBefore {
	static PersistentExperimentRepo testRep;
	static MutableExperiment testExp;
	/**
	 * erstellt vor jeder Testcaseausführung ein neues EXperiment
	 */
	@Before
	public void setUpDatabaseConnectionTest() {
		File file = new File("CreativeCrowd.db");
		file.delete();
		try {
			GlobalApplicationConfig.configureFromServletContext(null);
			testRep = new PersistentExperimentRepo();
			testExp = testRep.createExperiment("testExp");
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}
	/**
	 * löscht nach jeder Testcaseausführung das Experiment
	 */
	@After
	public void cleanUpExperiment() {
		try {
			testRep.deleteExperiment(testExp.getID());
			testRep = null;
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}
}

package edu.kit.ipd.creativecrowd.persistentmodel;

import static org.junit.Assert.fail;

import java.io.File;

import org.junit.After;
import org.junit.Before;

import edu.kit.ipd.chimpalot.jsonclasses.ConfigModelJson;
import edu.kit.ipd.chimpalot.util.GlobalApplicationConfig;
import edu.kit.ipd.chimpalot.util.Logger;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;

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
		Logger.debug("Kill: Database");
		try {
			GlobalApplicationConfig.configure(true);
			testRep = new PersistentExperimentRepo();
			ConfigModelJson j = new ConfigModelJson();
			testExp = testRep.createExperiment("testExp", j);
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

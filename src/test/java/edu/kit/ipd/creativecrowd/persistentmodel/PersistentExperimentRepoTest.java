package edu.kit.ipd.creativecrowd.persistentmodel;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;

public class PersistentExperimentRepoTest extends PersistentModelBefore {

	/**
	 * Lädt das Experiment mit dem Namen testExp.getId() und prüft ob das zurückgegebene Experiment dasselbe ist 
	 */
	@Test
	public void loadExperimentTest() {
		try {
			testRep.loadExperiment(testExp.getID()).equals(testExp);
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}

	}

	/**
	 * Löscht das Testexperiment und versucht es anschließend zu laden.
	 */
	@Test
	public void deleteExperiment() {
		try {
			String name = testExp.getID();
			testRep.deleteExperiment(testExp.getID());
			try {
				testRep.loadExperiment(name);
				fail("testExp still exists after deleting it");
			} catch (DatabaseException e) {
				testRep.createExperiment(name);
			}
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}

}

package edu.kit.ipd.creativecrowd.operations.strategies;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.kit.ipd.creativecrowd.mutablemodel.MutableAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAssignment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.operations.MockExperiment;
import edu.kit.ipd.creativecrowd.operations.NoValidTasksException;
import edu.kit.ipd.creativecrowd.operations.strategies.FreeformTaskConstellationMutator;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.persistentmodel.PersistentExperimentRepo;
import edu.kit.ipd.creativecrowd.readablemodel.Button;
import edu.kit.ipd.creativecrowd.readablemodel.CreativeTask;

public class FreeformTaskConstellationMutatorTest {
	MutableExperiment experiment;
	PersistentExperimentRepo repo;
	MutableAssignment as;
	MockExperiment mock;
	
	/**
	 * Überprüft das der erste erstellte Task ein CreativeTask ist. 
	 * Dies bezieht sich auf den Standardfall wenn noch CreativeTasks verfügbar sind. 
	 * 
	 */
	@Test
	public void testFirstTaskGeneration() throws DatabaseException {
		MutableAssignment a = experiment.addAssignment();
		FreeformTaskConstellationMutator t = new FreeformTaskConstellationMutator();
		try {
			t.run(a, experiment, Button.Start);
		} catch (NoValidTasksException | DatabaseException e) {
			fail(e.getMessage());
		}

		try {
			assertTrue("Expected to find a creative task at position 1", 
					a.getTaskConstellation().getCurrentTask() instanceof CreativeTask);
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void testAgain() throws NoValidTasksException, DatabaseException {
		MutableAssignment as = experiment.addAssignment();
		FreeformTaskConstellationMutator t = new FreeformTaskConstellationMutator();
		as.getTaskConstellation().addCreativeTask(experiment.getCreativeTask());
		as.getTaskConstellation().setCurrentTask(0);
		t.run(as, experiment, Button.Again);
		assertTrue(as.getTaskConstellation().getCurrentTask() instanceof CreativeTask);
	}
	@Test
	public void testSecondGeneration() throws NoValidTasksException, DatabaseException {
		MutableAssignment as = experiment.addAssignment();
		as.getTaskConstellation().addCreativeTask(experiment.getCreativeTask());
		MutableAnswer answer = as.getTaskConstellation().answerCreativeTaskAt(0);
		as.setSubmitted();
		MutableAssignment a = experiment.addAssignment();
		a.getTaskConstellation().addCreativeTask(experiment.getCreativeTask());
		a.getTaskConstellation().setCurrentTask(0);
		FreeformTaskConstellationMutator t = new FreeformTaskConstellationMutator();
		t.setParams(new HashMap<String, String>());
		t.run(a, experiment, Button.Next);
	}
	
	
	//TODO mehr Tests für andere Operationen als initiales Erstellen, sobald wir die anderen Vorgänge implementieren
	
	@Before
	public void setUp() {
		try {
			mock = new MockExperiment();
			experiment = mock.getExperiment();
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
		
	}
	@After
	public void TearDown() throws DatabaseException {
		mock.deleteExperiment();
	}
}

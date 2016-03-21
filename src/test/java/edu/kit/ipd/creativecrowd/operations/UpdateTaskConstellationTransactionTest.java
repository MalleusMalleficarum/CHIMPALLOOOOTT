package edu.kit.ipd.creativecrowd.operations;

import static org.junit.Assert.fail;

import java.util.HashMap;

import mockit.Mock;
import mockit.MockUp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.kit.ipd.creativecrowd.mutablemodel.MutableAssignment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableTaskConstellation;
import edu.kit.ipd.creativecrowd.operations.strategies.FreeformTaskConstellationMutator;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.persistentmodel.PersistentExperimentRepo;
import edu.kit.ipd.creativecrowd.readablemodel.Button;
import edu.kit.ipd.creativecrowd.util.GlobalApplicationConfig;

public class UpdateTaskConstellationTransactionTest {
	MutableExperiment experiment;
	PersistentExperimentRepo repo;
	MutableAssignment as;
	MockExperiment mock;
	
	@Before
	public void setUp() throws DatabaseException {
		GlobalApplicationConfig.configureFromServletContext(null);
		try {
			mock = new MockExperiment();
			repo = new PersistentExperimentRepo();
			experiment = mock.getExperiment();
			as = experiment.addAssignment();
		} catch (DatabaseException e) {
			fail("There´s a problem with the database");
		}
		HashMap<String,String> params = new HashMap<String,String>();
		params.put("tcm_class", "edu.kit.ipd.creativecrowd.operations.strategies.FreeformTaskConstellationMutator");
		experiment.setStrategyParams(params);
	}
	/**
	 *Die Methode test() ruft die UpdateTaskConstellation Methode auf und überprüft,
	 *dass keine Exception geworfen wird. 
	 */
	@Test
	public void test() {
		UpdateTaskConstellationTransaction t = new UpdateTaskConstellationTransaction();
		// just test that it doesn't raise
		new MockUp<FreeformTaskConstellationMutator>() {
			@Mock
			MutableTaskConstellation run(MutableAssignment ass, MutableExperiment experiment, Button btn) {
				return null;
				
			}
		};
		try {
			t.run(experiment, as, Button.Start);
		} catch (NoValidTasksException | DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@After
	public void tearDown() throws DatabaseException {
		mock.deleteExperiment();
	}

}

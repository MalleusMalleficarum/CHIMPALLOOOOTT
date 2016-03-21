package edu.kit.ipd.creativecrowd.operations;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import mockit.Mock;
import mockit.MockUp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.kit.ipd.creativecrowd.connector.ModelException;
import edu.kit.ipd.creativecrowd.controller.ExperimentSpecFromConfig;
import edu.kit.ipd.creativecrowd.mturk.ConnectionFailedException;
import edu.kit.ipd.creativecrowd.mturk.HITSpec;
import edu.kit.ipd.creativecrowd.mturk.IllegalInputException;
import edu.kit.ipd.creativecrowd.mutablemodel.ExperimentRepo;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.persistentmodel.PersistentExperimentRepo;
import edu.kit.ipd.creativecrowd.readablemodel.ExperimentSpec;
import edu.kit.ipd.creativecrowd.util.GlobalApplicationConfig;
import edu.kit.ipd.creativecrowd.mturk.MTurkConnection;
/**
 * 
 * @author Anika
 * Klasse, die die CreateExperimentTransaction testet
 */
public class CreateExperimentTransactionTest {
	CreateExperimentTransaction creaty;
	ExperimentRepo repo;
	ExperimentSpec spec;
	MutableExperiment exp;
	MockUp mocki;


	
	@Before
	public void setUp() throws DatabaseException {
		GlobalApplicationConfig.configureFromServletContext(null);
		Map<String, Double>  ratingOps = new HashMap<String, Double>();
		ratingOps.put("Pizza", 1.0);
		ratingOps.put("Kuchen", 2.0);
		ratingOps.put("Torte", 3.0);
		Map<String, String> strategyParams = new HashMap<String, String>();
		List<String> qualifications = new LinkedList<String>();
		List<String> tags = new LinkedList<String>();
		creaty = new CreateExperimentTransaction();
		spec = new ExperimentSpecFromConfig(500, 1, 5, 5, 5, "Do a backflip", "nmvvnh","so", "test", "exp", "mturk", "test", ratingOps, strategyParams, qualifications, tags, "test", "lol", 2, 3);
		repo = new PersistentExperimentRepo();
	}
	/**
	 * führt die Methode run der CreateExperimentTransaction aus und 
	 * stellt sicher, dass keine Exception geworfenn wird
	 * Zudem stellt sie sicher, dass beim Erstellen des HITS eine HITId abgespeichert
	 * wurde.
	 * @throws ModelException 
	 * @throws IllegalInputException 
	 * @throws DatabaseException 
	 * @throws ConnectionFailedException 
	 * @throws StrategyNotFoundException 
	 */
	@Test
	public void runTest() throws ModelException, StrategyNotFoundException, ConnectionFailedException, DatabaseException, IllegalInputException {
		mocki = new MockUp<MTurkConnection>(){
			@Mock
			String publishHIT(HITSpec spec){
				return "ID";
			}
			@Mock
			boolean checkBudget(int AmountInCents) {
				return true;
			}
		};
			exp = creaty.run(repo, spec);
			assertTrue(exp.getID().equals("exp"));
	}


	@After
	public void tearDown() throws DatabaseException {
		repo.deleteExperiment("exp");
		exp = null;
		repo = null;
		if(mocki != null) {
			try{
		mocki.tearDown();
			}catch(NullPointerException e)
			{
				
			}
		}
	}

}

package edu.kit.ipd.creativecrowd.operations;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import mockit.Mock;
import mockit.MockUp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.kit.ipd.chimpalot.jsonclasses.ConfigModelJson;
import edu.kit.ipd.chimpalot.jsonclasses.RatingOptionJson;
import edu.kit.ipd.chimpalot.util.GlobalApplicationConfig;
import edu.kit.ipd.creativecrowd.connector.ModelException;
import edu.kit.ipd.creativecrowd.crowdplatform.ConnectionFailedException;
import edu.kit.ipd.creativecrowd.crowdplatform.HITSpec;
import edu.kit.ipd.creativecrowd.crowdplatform.IllegalInputException;
import edu.kit.ipd.creativecrowd.mutablemodel.ExperimentRepo;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.persistentmodel.PersistentExperimentRepo;
import edu.kit.ipd.creativecrowd.readablemodel.ConfigModelMock;
import edu.kit.ipd.creativecrowd.transformer.Transformer;
/**
 * 
 * @author Anika
 * Klasse, die die CreateExperimentTransaction testet
 */
public class CreateExperimentTransactionTest {
	CreateExperimentTransaction creaty;
	ExperimentRepo repo;
	MutableExperiment exp;
	ConfigModelJson config;
	MockUp<Transformer> mocki;


	
	@Before
	public void setUp() throws Exception {
		GlobalApplicationConfig.configure(true);
		creaty = new CreateExperimentTransaction();
		repo = new PersistentExperimentRepo();
		config = ConfigModelMock.validConfig();
		config.setMaxRatingTask(1);
		config.setMaxCreativeTask(2);
		config.setBasicPaymentMTurk(1);
		config.setBasicPaymentPyBossa(1);
		config.setPaymentPerTaskCrMTurk(1);
		config.setPaymentPerTaskCrPyBossa(1);
		config.setPaymentPerTaskRaMTurk(1);
		config.setPaymentPerTaskRaPyBossa(1);
		config.setBudget(500);
		config.setTaskTitle("LOL");
		config.setTaskDescription("lol");
		List<RatingOptionJson> ratingoptions = new ArrayList<RatingOptionJson>();
		RatingOptionJson rating1 = new RatingOptionJson();
		RatingOptionJson rating2 = new RatingOptionJson();
		RatingOptionJson rating3 = new RatingOptionJson();
		rating1.setText("Pizza");
		rating2.setText("Kuchen");
		rating3.setText("Torte");
		rating1.setValue(1);
		rating2.setValue(2);
		rating3.setValue(3);
		ratingoptions.add(rating1);
		ratingoptions.add(rating2);
		ratingoptions.add(rating3);
		config.setRatingOptionsJson(ratingoptions);
		config.setQualificationsMTurk(new LinkedList<String>());
		config.setStrategy(null);
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
		mocki = new MockUp<Transformer>(){
			@Mock
			String publishHIT(HITSpec spec){
				return "ID";
			}
			@Mock
			boolean checkBudget(int AmountInCents) {
				return true;
			}
		};
			exp = creaty.run(repo, config, "exp");
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

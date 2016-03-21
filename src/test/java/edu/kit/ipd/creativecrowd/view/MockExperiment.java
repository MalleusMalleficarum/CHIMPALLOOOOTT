package edu.kit.ipd.creativecrowd.view;

import edu.kit.ipd.creativecrowd.mturk.AssignmentId;
import edu.kit.ipd.creativecrowd.mturk.HITSpec;
import edu.kit.ipd.creativecrowd.mturk.MTurkConnection;
import edu.kit.ipd.creativecrowd.mutablemodel.ExperimentRepo;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAssignment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableCreativeTask;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRatingOption;
import edu.kit.ipd.creativecrowd.operations.strategies.DefaultAPOC;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.persistentmodel.PersistentExperimentRepo;
import edu.kit.ipd.creativecrowd.readablemodel.RatingOption;
import edu.kit.ipd.creativecrowd.util.GlobalApplicationConfig;


public class MockExperiment {
	MutableExperiment exp;
	ExperimentRepo repo;
	public MockExperiment() throws DatabaseException {
		GlobalApplicationConfig.configureFromServletContext(null);
		repo = new PersistentExperimentRepo();
		try{
			repo.deleteExperiment("Test");
		}catch(Exception exc){
			
		}
		
		exp = repo.createExperiment("Test");
		MutableCreativeTask ct = exp.addCreativeTask();
		ct.setDescription("Do a backflip");
		//metadata setting
		exp.setMaxNumberOfAnswersPerAssignment(1);
		exp.setMaxNumberOfRatingsPerAssignment(1);
		//sets all Payment variables
		exp.setBasicPaymentHIT(1);
		exp.setBasicPaymentAnswer(1);
		exp.setBasicPaymentRating(1);
		exp.setBonusPayment(10);
		exp.setBudget(500);
		exp.setHITTitle("LOL");
		exp.setHITDescription("lol");
		exp.setDescription("Your Task");
		//initialise RatingOptions
			MutableRatingOption mro = exp.addRatingOption();
			mro.setText("Pizza");
			mro.setValue(1);
			MutableRatingOption kro = exp.addRatingOption();
			kro.setText("Kuchen");
			kro.setValue(2);
			MutableRatingOption pro = exp.addRatingOption();
			pro.setText("Torte");
			pro.setValue(3);		
		
	}
	public MutableExperiment getExperiment() {
		return exp;
	}
	private MutableAssignment addCompleteAssignment() throws DatabaseException {
		MutableAssignment ass = exp.addAssignment();
		ass.setAssignmentID(new AssignmentId("test"));
		return ass;
		
	}

}

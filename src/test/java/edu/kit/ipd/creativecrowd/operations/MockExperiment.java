package edu.kit.ipd.creativecrowd.operations;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import edu.kit.ipd.creativecrowd.mturk.AssignmentId;
import edu.kit.ipd.creativecrowd.mturk.HITSpec;
import edu.kit.ipd.creativecrowd.mturk.MTurkConnection;
import edu.kit.ipd.creativecrowd.mutablemodel.ExperimentRepo;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAssignment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableCreativeTask;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRating;
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
		Calendar cal = GregorianCalendar.getInstance();
		String experimentName = cal.getTime().toString().replace(" ", "");
		exp = repo.createExperiment(experimentName);
		MutableCreativeTask ct = exp.addCreativeTask();
		ct.setDescription("Do a backflip");
		// metadata setting
		exp.setMaxNumberOfAnswersPerAssignment(2);
		exp.setMaxNumberOfRatingsPerAssignment(1);
		// sets all Payment variables
		exp.setBasicPaymentHIT(1);
		exp.setBasicPaymentAnswer(1);
		exp.setBasicPaymentRating(1);
		exp.setBonusPayment(10);
		exp.setBudget(500);
		exp.setHITTitle("LOL");
		exp.setHITDescription("lol");
		// initialise RatingOptions
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

	public void deleteExperiment() throws DatabaseException {
		repo.deleteExperiment(exp.getID());
	}

	public void setStrategyparams() throws DatabaseException {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("apoc_class",
				"edu.kit.ipd.creativecrowd.operations.strategies.DefaultAPOC");
		params.put("aqic_class",
				"edu.kit.ipd.creativecrowd.operations.strategies.AverageRating");
		params.put("ftg_class",
				"edu.kit.ipd.creativecrowd.operations.strategies.FeedbackWithoutRatingText");
		params.put("rqic_class",
				"edu.kit.ipd.creativecrowd.operations.strategies.EnsureRatingDiversity");
		params.put("rsd_class",
				"edu.kit.ipd.creativecrowd.operations.strategies.FixedRatingsPerAnswerDecider");
		params.put("tacc_class",
				"edu.kit.ipd.creativecrowd.operations.strategies.NAssignments");
		params.put(
				"tcm_class",
				"edu.kit.ipd.creativecrowd.operations.strategies.FreeformTaskConstellationMutator");
		exp.setStrategyParams(params);
	}

		
	
}

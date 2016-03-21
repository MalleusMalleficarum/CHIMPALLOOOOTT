package edu.kit.ipd.creativecrowd.operations;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import edu.kit.ipd.chimpalot.jsonclasses.ConfigModelJson;
import edu.kit.ipd.chimpalot.jsonclasses.RatingOptionJson;
import edu.kit.ipd.chimpalot.util.GlobalApplicationConfig;
import edu.kit.ipd.creativecrowd.mutablemodel.ExperimentRepo;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableCreativeTask;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.persistentmodel.PersistentConfigModelRepo;
import edu.kit.ipd.creativecrowd.persistentmodel.PersistentExperimentRepo;
import edu.kit.ipd.creativecrowd.readablemodel.ConfigModelMock;

public class MockExperiment {
	MutableExperiment exp;
	ExperimentRepo repo;

	public MockExperiment() throws Exception {
		GlobalApplicationConfig.configure(true);
		repo = new PersistentExperimentRepo();
		Calendar cal = GregorianCalendar.getInstance();
		String experimentName = cal.getTime().toString().replace(" ", "");
		ConfigModelJson config = ConfigModelMock.validConfig();
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
		config.setStrategy(null);
		exp = repo.createExperiment(experimentName, config);
		MutableCreativeTask ct = exp.addCreativeTask();
		ct.setDescription("Do a backflip");
	}

	public MutableExperiment getExperiment() {
		return exp;
	}

	public void deleteExperiment() throws DatabaseException {
		repo.deleteExperiment(exp.getID());
	}

	public static void setStrategyparams(MutableExperiment exp, HashMap<String, String> params) throws DatabaseException {
//		HashMap<String, String> params = new HashMap<String, String>();
//		params.put("apoc_class",
//				"edu.kit.ipd.creativecrowd.operations.strategies.DefaultAPOC");
//		params.put("aqic_class",
//				"edu.kit.ipd.creativecrowd.operations.strategies.AverageRating");
//		params.put("ftg_class",
//				"edu.kit.ipd.creativecrowd.operations.strategies.FeedbackWithoutRatingText");
//		params.put("rqic_class",
//				"edu.kit.ipd.creativecrowd.operations.strategies.EnsureRatingDiversity");
//		params.put("rsd_class",
//				"edu.kit.ipd.creativecrowd.operations.strategies.FixedRatingsPerAnswerDecider");
//		params.put("tacc_class",
//				"edu.kit.ipd.creativecrowd.operations.strategies.NAssignments");
//		params.put(
//				"tcm_class",
//				"edu.kit.ipd.creativecrowd.operations.strategies.FreeformTaskConstellationMutator");
		ConfigModelJson config = new ConfigModelJson(exp.getConfig());
		config.setID(exp.getConfig().getID());
		config.setStrategy(params);
		PersistentConfigModelRepo repo = new PersistentConfigModelRepo();
		repo.deleteConfigModel(exp.getConfig().getID());
		exp.setConfig(config);
	}
}

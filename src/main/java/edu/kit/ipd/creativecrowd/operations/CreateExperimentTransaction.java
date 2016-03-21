package edu.kit.ipd.creativecrowd.operations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.kit.ipd.creativecrowd.connector.ModelException;
import edu.kit.ipd.creativecrowd.mturk.ConnectionFailedException;
import edu.kit.ipd.creativecrowd.mturk.HITSpec;
import edu.kit.ipd.creativecrowd.mturk.IllegalInputException;
import edu.kit.ipd.creativecrowd.mturk.MTurkConnection;
import edu.kit.ipd.creativecrowd.mutablemodel.ExperimentRepo;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableCreativeTask;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRatingOption;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.ExperimentSpec;
import edu.kit.ipd.creativecrowd.readablemodel.RatingOption;
import edu.kit.ipd.creativecrowd.util.GlobalApplicationConfig;
/**
 * 
 * @author Anika & Jonas
 * This class is used by the Connector to start a new Experiment
 */
public class CreateExperimentTransaction extends Transaction  {
	/**
	 * The run method organises the order of starting an experiment and publishs the HIT.
	 * The method sets all attributes of Experiment to their initial values
	 * @param repo here is the new Experiment stored
	 * @param spec contains the informations about the params for the new Experiment
	 * @return the new Experiment
	 * @throws StrategyNotFoundException if a strategy is chosen which doesn�t exist the exception is thrown
	 * @throws ConnectionFailedException if the connection to MTurk fails
	 * @throws DatabaseException if an internal database error has occurred 
	 * @throws IllegalInputException if illegal parameters for the MTurk hit are given
	 * @throws ModelException 
	 */
	public MutableExperiment run(ExperimentRepo repo, ExperimentSpec spec) throws StrategyNotFoundException, ConnectionFailedException, DatabaseException, IllegalInputException, ModelException {
		this.configureAndLoadStrategies(spec.getStrategyParams());
		MutableExperiment experiment;
		try {
			experiment = repo.createExperiment(spec.getID());
		} catch(DatabaseException e) {
			throw new ModelException("The experiment named '"+spec.getID()+"' already exists.");
		}
		experiment.setStrategyParams(spec.getStrategyParams());
		// initialise creative task
		MutableCreativeTask ct = experiment.addCreativeTask();
		ct.setDescription(spec.getQuestion());
		ct.setAccordingRatingTaskDescription(spec.getRatingTaskQuestion());
		ct.setPicture(spec.getPicture(), spec.getPictureLicense());
		//metadata setting
		experiment.setMaxNumberOfAnswersPerAssignment(spec.getMaxNumberOfAnswersPerAssignment());
		experiment.setMaxNumberOfRatingsPerAssignment(spec.getMaxNumberOfRatingsPerAssignment());
		//sets all Payment variables
		experiment.setBasicPaymentHIT(spec.getBasicPaymentHITCents());
		experiment.setBasicPaymentAnswer(spec.getBasicPaymentAnswerCents());
		experiment.setBasicPaymentRating(spec.getBasicPaymentRatingCents());
		experiment.setBonusPayment(spec.getBonusPaymentCents());
		experiment.setBudget(spec.getBudgetCents());
		experiment.setHITTitle(spec.getHITTitle());
		experiment.setHITDescription(spec.getHITDescription());
		experiment.setTags(spec.getTags());
		experiment.setQualifications(spec.getQualifications());
		experiment.setDescription(spec.getDescription());
		//initialise RatingOptions
		experiment.setRatingTaskViewClass(spec.getRatingTaskViewClass());
		try {
		for(RatingOption ro: spec.getRatingOptions()) {
			MutableRatingOption mro = experiment.addRatingOption();
			mro.setText(ro.getText());
			mro.setValue(ro.getValue());
		}
		} catch (Exception e) {
			repo.deleteExperiment(spec.getID());
		}
		AssignmentPaymentOutcomeCalculator apoc = this.loadAPOCStrategy(experiment.getStrategyParams());
		TotalAssignmentCountController tacc = this.loadTACCStrategy(experiment.getStrategyParams());
		/*-?|Anika|Anika|c1|?*/
		int nrOfAssignments = tacc.run(experiment, apoc.worstCasePayoutPerAssignment(experiment), apoc.getBonusPool());
		MTurkConnection connect = new MTurkConnection();
		if(!connect.checkBudget(experiment.getBudgetCents())) {
			repo.deleteExperiment(spec.getID());
			throw new ModelException("Entered budget is too high");
		}
		//(String title, String description, int rewardCents,int numAssignments,
		//String externalQuestionShem, String externalQuestionURL, int frameHeight)
		String url = GlobalApplicationConfig.getPublicBaseURL()+"/assignment/"+experiment.getID();
		HITSpec hit = new HITSpec(
				experiment.getHITTitle(),
				experiment.getHITDescription(),
				experiment.getBasicPaymentHITCents(),
				nrOfAssignments,
				url,
				3600, // estimate
				86400, // timeout
				experiment.getQualifications());
		List<String> keywords = (List<String>) experiment.getTags();
		String[] keys = new String[keywords.size()];
		keywords.toArray(keys);
		hit.setKeywords(keys);
		String hitId = connect.publishHIT(hit);
		experiment.setHitID(hitId);
		
		return experiment;
	}
	/**
	 * is called in the beginning to test whether all strategies are specified correctly
	 * @param params the in config filed specified params in a map
	 * @throws StrategyNotFoundException if a strategyclass disappeard
	 */
	private void configureAndLoadStrategies(Map<String, String> params) throws StrategyNotFoundException {
		// set the default strategy class names for strategies that haven't been configured
		if(params.get("apoc_class") == null) {
			params.put("apoc_class", "edu.kit.ipd.creativecrowd.operations.strategies.DefaultAPOC");
		}
		
		if(params.get("aqic_class") == null) {
			params.put("aqic_class", "edu.kit.ipd.creativecrowd.operations.strategies.AverageRating");
		}
		
		if(params.get("ftg_class") == null) {
			params.put("ftg_class", "edu.kit.ipd.creativecrowd.operations.strategies.FeedbackWithoutRatingText");
		}
		
		if(params.get("rqic_class") == null) {
			params.put("rqic_class", "edu.kit.ipd.creativecrowd.operations.strategies.EnsureRatingDiversity");
		}
		
		if(params.get("rsd_class") == null) {
			params.put("rsd_class", "edu.kit.ipd.creativecrowd.operations.strategies.FixedRatingsPerAnswerDecider");
		}
		
		if(params.get("tacc_class") == null) {
			params.put("tacc_class", "edu.kit.ipd.creativecrowd.operations.strategies.NAssignments");
		}
		
		if(params.get("tcm_class") == null) {
			params.put("tcm_class", "edu.kit.ipd.creativecrowd.operations.strategies.FreeformTaskConstellationMutator");
		}

		// test loading all strategies once; if the user attempts to specify an invalid class name,
		// this will raise.
		this.loadAPOCStrategy(params);
		this.loadAQICStrategy(params);
		this.loadFTGStrategy(params);
		this.loadRQICStrategy(params);
		this.loadRSDStrategy(params);
		this.loadTACCStrategy(params);
		this.loadTCMStrategy(params);
	}

}

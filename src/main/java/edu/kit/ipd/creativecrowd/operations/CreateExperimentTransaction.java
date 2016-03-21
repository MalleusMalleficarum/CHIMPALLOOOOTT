package edu.kit.ipd.creativecrowd.operations;

import java.util.List;
import java.util.Map;

import edu.kit.ipd.chimpalot.jsonclasses.ConfigModelJson;
import edu.kit.ipd.chimpalot.util.GlobalApplicationConfig;
import edu.kit.ipd.chimpalot.util.Logger;
import edu.kit.ipd.creativecrowd.connector.ModelException;
import edu.kit.ipd.creativecrowd.crowdplatform.PlatformIdentity;
import edu.kit.ipd.creativecrowd.crowdplatform.ConnectionFailedException;
import edu.kit.ipd.creativecrowd.crowdplatform.HITSpec;
import edu.kit.ipd.creativecrowd.crowdplatform.IllegalInputException;
import edu.kit.ipd.creativecrowd.mutablemodel.ConfigModelRepo;
import edu.kit.ipd.creativecrowd.mutablemodel.ExperimentRepo;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableControlQuestion;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableCreativeTask;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutablePossibleControlAnswer;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.persistentmodel.ExperimentWatchdog;
import edu.kit.ipd.creativecrowd.persistentmodel.PersistentConfigModelRepo;
import edu.kit.ipd.creativecrowd.readablemodel.ConfigModel;
import edu.kit.ipd.creativecrowd.readablemodel.ControlQuestion;
import edu.kit.ipd.creativecrowd.transformer.ConfigFileInterpreter;
import edu.kit.ipd.creativecrowd.transformer.Transformer;
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
	 * @param config contains the informations about the params for the new Experiment
	 * @return the new Experiment
	 * @throws StrategyNotFoundException if a strategy is chosen which doesn�t exist the exception is thrown
	 * @throws ConnectionFailedException if the connection to MTurk or PyBossa fails
	 * @throws DatabaseException if an internal database error has occurred
	 * @throws IllegalInputException if illegal parameters for the MTurk hit are given
	 * @throws ModelException
	 */
	public MutableExperiment run(ExperimentRepo repo, ConfigModel config, String expid) throws StrategyNotFoundException,
	ConnectionFailedException, DatabaseException, IllegalInputException, ModelException {
		VerifyConfigTransaction verify = new VerifyConfigTransaction();
		if (!verify.run(config).isEmpty()) {
			throw new ModelException("The config '" + config.getID() + "' is invalid.");
		}
		if (config.getStrategy().get("tcm_class") != null && config.getStrategy().get("tcm_class")
				.equals("edu.kit.ipd.creativecrowd.operations.strategies.OnlyRatingTCM")) {
			//OnlyRatingTCM is not allowed when creating a new experiment
			throw new StrategyNotFoundException("The strategy OnlyRatingTCM is not allowed when creating a new experiment.", null);
		}
		this.configureAndLoadStrategies(config.getStrategy());
		MutableExperiment experiment;
		try {
			experiment = repo.createExperiment(expid, config);
			for (ControlQuestion cq : config.getControlQuestions()) {
				MutableControlQuestion mcq = experiment.addControlQuestion();
				mcq.setQuestion(cq.getQuestion());
				for (MutablePossibleControlAnswer pca : cq.getPossibleAnswers()) {
					MutablePossibleControlAnswer mpca = mcq.addPossibleAnswer();
					mpca.setText(pca.getText());
					mpca.setIsTrue(pca.getIsTrue());
				}
			}

		} catch(DatabaseException e) {
			Logger.logException(e.getMessage());
			throw new ModelException("The experiment named '" + expid + "' already exists.");
		}
		
		ExperimentWatchdog watchdog = new ExperimentWatchdog(expid);
		watchdog.start();
		MutableCreativeTask ct = experiment.addCreativeTask();
		ct.setDescription(config.getTaskQuestion());
		ct.setAccordingRatingTaskDescription(config.getRatingTaskQuestion());
		ct.setPicture(config.getPictureURL(), config.getTaskSourceURL());
		AssignmentPaymentOutcomeCalculator apoc = this.loadAPOCStrategy(experiment.getStrategyParams());
		TotalAssignmentCountController tacc = this.loadTACCStrategy(experiment.getStrategyParams());
		/*-?|Anika|Anika|c1|?*/
		int nrOfAssignments = tacc.run(experiment, apoc.worstCasePayoutPerAssignment(experiment), apoc.getBonusPool());
		/*
		 * Originally this was directed to mturk only, now it runs over the transformer
		 * @Robin 
		 */
		Transformer connect = ConfigFileInterpreter.getTransformer(experiment);
		if(!connect.checkBudget(experiment.getBudgetCents())) {
			repo.deleteExperiment(config.getID());
			throw new ModelException("Entered budget is too high");
		}
		//(String title, String description, int rewardCents,int numAssignments,
		//String externalQuestionShem, String externalQuestionURL, int frameHeight)
		String urlMTurk = GlobalApplicationConfig.getPublicBaseURL()+"/assignment/"+experiment.getID() + "/"
				+ PlatformIdentity.MTurk.name();
		HITSpec mturkHIT = new HITSpec(
				experiment.getHITTitle(),
				experiment.getHITDescription(),
				experiment.getBasicPaymentHITCents(PlatformIdentity.MTurk),
				nrOfAssignments,
				urlMTurk,
				3600, // estimate
				86400, // timeout
				experiment.getQualifications(PlatformIdentity.MTurk));
		List<String> keywords = (List<String>) experiment.getTags();
		String[] keys = new String[keywords.size()];
		keywords.toArray(keys);
		mturkHIT.setKeywords(keys);
		/*
		 * Originally this was directed to mturk only, now it runs over the transformer
		 * @Robin 
		 */
		Transformer transformer = ConfigFileInterpreter.getTransformer(experiment);
		String hitId = transformer.publishHIT(mturkHIT);
		experiment.setHitID(hitId);


		return experiment;
	}
	
	/**
	 * This method allows a running experiment to be amended, without stopping the old experiment.
	 * The specification of the HIT and the payment cannot be changed, this will throw an {@code ModelException} if
	 * it is attempted. The new config {@code config} has to be complete, empty fields are NOT automatically filled
	 * with the values from the previous configuration of the experiment.
	 * 
	 * @param repo the repo of the experiment.
	 * @param config the new configuration of the experiment.
	 * @param expid the id of the experiment which id wished to be amended
	 * @return the experiment
	 * * @throws StrategyNotFoundException if a strategy is chosen which doesn�t exist the exception is thrown
	 * @throws ConnectionFailedException if the connection to MTurk or PyBossa fails
	 * @throws DatabaseException if an internal database error has occurred
	 * @throws IllegalInputException if illegal parameters for the MTurk hit are given
	 * @throws ModelException if either {@code config} is not suitable, or the experiment has already finished.
	 * @author Thomas Friedel 
	 */
	public MutableExperiment amendExperiment(ExperimentRepo repo, ConfigModel config, String expid) throws
	StrategyNotFoundException, DatabaseException, IllegalInputException, ModelException, ConnectionFailedException {
		VerifyConfigTransaction verify = new VerifyConfigTransaction();
		MutableExperiment experiment = repo.loadExperiment(expid);
		if (experiment.isFinished()) {
			throw new ModelException("The experiment named '" + expid + "' is already finished");
		}
		if (!verify.compare(config, experiment.getConfig()).isEmpty()) {
			throw new ModelException("The config '" + config.getID() + "' is invalid.");
		}
		
		//TODO Block  access to experiment
		
		//Nr of assignments has to stay the same as before.
		AssignmentPaymentOutcomeCalculator apoc = this.loadAPOCStrategy(experiment.getStrategyParams());
		TotalAssignmentCountController tacc = this.loadTACCStrategy(experiment.getStrategyParams());
		int oldNrOfAssignments = tacc.run(experiment, apoc.worstCasePayoutPerAssignment(experiment), apoc.getBonusPool());
		
		
		this.configureAndLoadStrategies(config.getStrategy());
		ConfigModel temp = experiment.getConfig();
		ConfigModelJson oldConfig = new ConfigModelJson(temp); //Just to save the old config
		ConfigModelRepo configRepo = new PersistentConfigModelRepo();
		configRepo.deleteConfigModel(temp.getID());
		boolean pyBossaOld = oldConfig.getSendCreativeTo(PlatformIdentity.PyBossa);
		boolean mTurkOld = oldConfig.getSendCreativeTo(PlatformIdentity.MTurk);
		boolean pyBossaNew = config.getSendCreativeTo(PlatformIdentity.PyBossa);
		boolean mTurkNew = config.getSendCreativeTo(PlatformIdentity.MTurk);
		experiment.setConfig(config);
		Transformer connect = ConfigFileInterpreter.getTransformer(experiment);
		
		
		connect.update(PlatformIdentity.PyBossa, pyBossaOld, pyBossaNew);
		connect.update(PlatformIdentity.MTurk, mTurkOld, mTurkNew);
		
		//Check budget
		int maxNrOfAssignments = tacc.run(experiment, apoc.worstCasePayoutPerAssignment(experiment), apoc.getBonusPool());
		if (maxNrOfAssignments < oldNrOfAssignments) {
			//Seems like the budget is not enough with new parameters but same nr of assignments. Abort
			experiment.setConfig(oldConfig); //revert
			throw new ModelException("Entered budget is too low");
		}
		if(!connect.checkBudget(experiment.getBudgetCents())) {
			experiment.setConfig(oldConfig); //revert
			throw new ModelException("Entered budget is too high");
		}
	    
		//Reassign other values
		
		MutableCreativeTask ct = experiment.getCreativeTask();
		ct.setDescription(config.getTaskQuestion());
		ct.setAccordingRatingTaskDescription(config.getRatingTaskQuestion());
		ct.setPicture(config.getPictureURL(), config.getTaskSourceURL());
		
		for (ControlQuestion cq : config.getControlQuestions()) {
			MutableControlQuestion mcq = experiment.addControlQuestion();
			mcq.setQuestion(cq.getQuestion());
			for (MutablePossibleControlAnswer pca : cq.getPossibleAnswers()) {
				MutablePossibleControlAnswer mpca = mcq.addPossibleAnswer();
				mpca.setText(pca.getText());
				mpca.setIsTrue(pca.getIsTrue());
			}
		}
		
		
		//TODO Free access to experiment
		
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

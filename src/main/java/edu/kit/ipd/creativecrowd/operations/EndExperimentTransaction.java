package edu.kit.ipd.creativecrowd.operations;

import java.util.HashMap;
import java.util.Map;

import edu.kit.ipd.chimpalot.jsonclasses.ConfigModelJson;
import edu.kit.ipd.chimpalot.util.Alert;
import edu.kit.ipd.creativecrowd.crowdplatform.ConnectionFailedException;
import edu.kit.ipd.creativecrowd.crowdplatform.IllegalInputException;
import edu.kit.ipd.creativecrowd.mutablemodel.ConfigModelRepo;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAssignment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.persistentmodel.PersistentConfigModelRepo;
import edu.kit.ipd.creativecrowd.transformer.ConfigFileInterpreter;
import edu.kit.ipd.creativecrowd.transformer.Transformer;

/**
 * This class provides the order of ending an experiment it disables the hit, and evaluates workers payment.
 * 
 * @author Anika
 */
public class EndExperimentTransaction extends Transaction {
	/**
	 * ends an experiment
	 * 
	 * @param ex experiment to be ended
	 * @throws ConnectionFailedException if theres no connection to MTurk this exception is thrown
	 * @throws DatabaseException if something went wrong in the databse
	 * @throws IllegalInputException if the hitId was saved in awrong way or disappeared
	 * @throws StrategyNotFoundException if a strategy disappeard
	 */
	public void run(MutableExperiment ex) throws ConnectionFailedException,
			DatabaseException, IllegalInputException {
		// disables HIT on Mturk
		/*
		 * Originally this was directed to mturk only, now it runs over the transformer
		 * @Robin 
		 */
		Transformer connect = ConfigFileInterpreter.getTransformer(ex);
		connect.endHIT(ex.getHitID());

		RatingQualityIndexCalculator rqic;
		AnswerQualityIndexCalculator aqic;
		AssignmentPaymentOutcomeCalculator apoc;
		FeedbackTextGenerator ftg;

		try {
			rqic = this.loadRQICStrategy(ex.getStrategyParams());
			aqic = this.loadAQICStrategy(ex.getStrategyParams());
			apoc = this.loadAPOCStrategy(ex.getStrategyParams());
			ftg = this.loadFTGStrategy(ex.getStrategyParams());
		} catch (StrategyNotFoundException e) {
			throw new Error("Strategy class disappeared! " + e.getMessage());
		}

		// cals qualityIndex of all Ratings
		rqic.run(ex);
		// calc qualityIndex of all Answers
		aqic.run(ex);
		// calc Paymentoutcome for all Assignments in the experiment
		apoc.run(ex);

		// calc Feedback, send Payment and Bonusses where necessary
		for (MutableAssignment as : ex.getAssignments()) {
			if (as.isSubmitted()) {
				String feedback = ftg.generateFeedback(as);
				if (as.getPaymentOutcome().isApproved()) {
					
					connect.approveHIT(as.getMTurkAssignmentID(), as.getWorkerID(), feedback);
				}
				if (as.getPaymentOutcome().getBonusPaidCents() > 0) {
					String bonusMessage = ftg.generateBonusMessage(as);
					connect.sendBonus(as.getMTurkAssignmentID(), as.getWorkerID(), as.getPaymentOutcome().getBonusPaidCents(), bonusMessage);
				}
				if (!as.getPaymentOutcome().isApproved()) {
					connect.rejectAssignment(as.getMTurkAssignmentID(), feedback);
				}
				as.markAsPaid();/*-?|Anika|Anika|c5|?*/
			}
		}
		ex.markAsFinished();/*-?|Anika|Anika|c4|?*/
		Alert.notifyForTermination(ex.getID());
		//pay pybossa workers over threshold
		WorkerPaymentChecker checker = new WorkerPaymentChecker();
		checker.payAllWorkersOverThreshold();
	}
	
	/**
	 * Stops the experiment from handing out creative tasks. The workers will only rate already given answers.
	 * If all answers are already sufficiently rated, the experiment will be stopped.
	 * 
	 * @throws DatabaseException if the SQL request fails
 	 * @throws ConnectionFailedException if theres no connection to the crowdplatform
	 * @throws IllegalInputException if the hitId was saved in a wrong way or disappeared
	 * @author Thomas Friedel
	 */
	public void stopCreativeTasks(MutableExperiment exp) throws DatabaseException,
	ConnectionFailedException, IllegalInputException {
		ConfigModelJson config = new ConfigModelJson(exp.getConfig());
		Map<String, String> strategy = new HashMap<String, String>();
		for (String key : config.getStrategy().keySet()) {
			if (!key.startsWith("tcm_")) {
				strategy.put(key, config.getStrategy().get(key));
			}
		}
		strategy.put("tcm_class", "edu.kit.ipd.creativecrowd.operations.strategies.OnlyRatingTCM");
		ConfigModelRepo repo = new PersistentConfigModelRepo();
		repo.deleteConfigModel(exp.getConfig().getID());
		config.setStrategy(strategy);
		exp.setConfig(config);
		
		boolean sufficient = true;
		for (MutableAnswer ans : exp.getCreativeTask().getAnswers()) {
			if (!ans.isSufficientlyRated()) {
				sufficient = false;
			}
		}
		if (sufficient) {
			EndExperimentTransaction end = new EndExperimentTransaction();
			end.run(exp);
		}
	}

}

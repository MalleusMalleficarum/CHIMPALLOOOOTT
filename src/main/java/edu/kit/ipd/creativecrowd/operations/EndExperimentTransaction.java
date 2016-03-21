package edu.kit.ipd.creativecrowd.operations;

import edu.kit.ipd.creativecrowd.mturk.ConnectionFailedException;
import edu.kit.ipd.creativecrowd.mturk.IllegalInputException;
import edu.kit.ipd.creativecrowd.mturk.MTurkConnection;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAssignment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;

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
		MTurkConnection connect = new MTurkConnection();
		connect.endHIT(ex.getHitID());
		int count = 0;

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
					connect.approveHIT(as.getMTurkAssignmentID(), feedback);
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

	}

}

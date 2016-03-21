package edu.kit.ipd.creativecrowd.operations;

import edu.kit.ipd.creativecrowd.mturk.ConnectionFailedException;
import edu.kit.ipd.creativecrowd.mturk.IllegalInputException;
import edu.kit.ipd.creativecrowd.mturk.MTurkConnection;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAssignment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.Rating;

/**
 * This class organises the order of submitting an assignment marks assignment
 * submitted recalls the ccalculation of the totalAssignmentCount tests whether
 * answers are sufficiently rated now in order to not use them again in a
 * ratingTask
 * 
 * @author Anika
 */
public class SubmitAssignmentTransaction extends Transaction {
	/**
	 * performs the submit
	 * 
	 * @param ass
	 * @param experiment
	 * @throws DatabaseException
	 *         -
	 * @throws ConnectionFailedException
	 * @throws IllegalInputException
	 */
	public void run(MutableAssignment ass, MutableExperiment experiment)
			throws DatabaseException, ConnectionFailedException,
			IllegalInputException {
		ass.setSubmitted();
		TotalAssignmentCountController tacc;
		try {
			tacc = this.loadTACCStrategy(experiment.getStrategyParams());
		} catch (StrategyNotFoundException e) {
			// If this happens now, it's bad. We validate the strategy existence
			// while creating the experiment.
			throw new Error("Strategy class disappeared! " + e.getMessage());
		}
		RatingSufficiencyDecider rsd;
		try {
			rsd = this.loadRSDStrategy(experiment.getStrategyParams());
		} catch (StrategyNotFoundException e) {
			// If this happens now, it's bad. We validate the strategy existence
			// while creating the experiment.
			throw new Error("Strategy class disappeared! " + e.getMessage());
		}
		// look if a answer is now sufficiently rated
		for (MutableAnswer answer : experiment.getCreativeTask().getAnswers()) {
			for (Rating rating : ass.getTaskConstellation().getRatings()) {
				if (rating.getAnswer().equals(answer)) {
					if (rsd.hasEnoughRatings(answer, experiment)) {
						answer.markAsRated();
					}
				}
			}
		}
		AssignmentPaymentOutcomeCalculator apoc;
		try {
			apoc = this.loadAPOCStrategy(experiment.getStrategyParams());
		} catch (StrategyNotFoundException e) {
			// If this happens now, it's bad. We validate the strategy existence
			// while creating the experiment.
			throw new Error("Strategy class disappeared! " + e.getMessage());
		}
		int nrofA = tacc.run(experiment,
				apoc.worstCasePayoutPerAssignment(experiment),
				apoc.getBonusPool());

		boolean sufficient = true;
		/*-?|Anika|Anika|c0|?*/
		// look if everything is sufficiently rated
		// or all available assignments are submitted and the experiment needs to be ended
		for (MutableAnswer ans : experiment.getCreativeTask().getAnswers()) {
			if (!ans.isSufficientlyRated()) {
				sufficient = false;
			}
		}
		int submitted = 0;
		for (MutableAssignment as : experiment.getAssignments()) {
			if (as.isSubmitted()) {
				submitted++;
			}
		}
		if (sufficient || nrofA <= submitted) {
			EndExperimentTransaction end = new EndExperimentTransaction();
			end.run(experiment);
		}
		else {
			MTurkConnection connect = new MTurkConnection();
			connect.extendAssignmentNumber(nrofA, experiment.getHitID());
		}
	}
}

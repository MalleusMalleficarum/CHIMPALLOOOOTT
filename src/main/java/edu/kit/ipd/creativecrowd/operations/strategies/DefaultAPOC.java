package edu.kit.ipd.creativecrowd.operations.strategies;

import edu.kit.ipd.creativecrowd.mutablemodel.MutableAssignment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.operations.AssignmentPaymentOutcomeCalculator;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.Answer;
import edu.kit.ipd.creativecrowd.readablemodel.Experiment;
import edu.kit.ipd.creativecrowd.readablemodel.Rating;

/**
 * This APOC pays out the basic payment, plus a fixed amount
 * for each answer and rating given in the assignment.
 * Every assignment is approved.
 * 
 * @author jonas
 */
public class DefaultAPOC extends StrategyWithParams implements AssignmentPaymentOutcomeCalculator {

	@Override
	public int worstCasePayoutPerAssignment(Experiment exp) throws DatabaseException {
		// we pay out these two using a bonus
		int answerPayments = exp.getMaxNumberOfAnswersPerAssignment() * exp.getBasicPaymentAnswerCents();
		/*-?|Anika|Anika|c9|?*/
		int ratingPayments = exp.getMaxNumberOfRatingsPerAssignment() * exp.getBasicPaymentRatingCents();

		// so the total budget hit is the basic HIT approval payment plus these worst-case bonuses
		return exp.getBasicPaymentHITCents() + answerPayments + ratingPayments;
	}

	@Override
	public void run(MutableExperiment ex) throws DatabaseException {
		for (MutableAssignment as : ex.getAssignments()) {
			as.setPaymentOutcome(true, getBonusPayOutBasedOnCompletedTask(ex, as));
		}
	}

	protected int getBonusPayOutBasedOnCompletedTask(MutableExperiment ex,
			MutableAssignment as) throws DatabaseException {
		int nAnswers = 0;
		int nRatings = 0;

		for (@SuppressWarnings("unused")
		Rating r : as.getTaskConstellation().getRatings()) {
			nRatings++;
		}

		for (@SuppressWarnings("unused")
		Answer a : as.getTaskConstellation().getAnswers()) {
			nAnswers++;
		}

		int bonusAmount = ex.getBasicPaymentAnswerCents() * nAnswers
				+ ex.getBasicPaymentRatingCents() * nRatings;

		int bonusCap = worstCasePayoutPerAssignment(ex) - ex.getBasicPaymentHITCents();
		int result = Math.min(bonusAmount, bonusCap);
		return result;
	}

	@Override
	public int getBonusPool() {
		return 0;
	}

}

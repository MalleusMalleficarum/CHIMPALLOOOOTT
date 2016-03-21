package edu.kit.ipd.creativecrowd.operations.strategies;

import edu.kit.ipd.creativecrowd.crowdplatform.PlatformIdentity;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAnswer;
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
		
		int maxBasicPaymentAnswer = 0;
		int maxBasicPaymentRating = 0;
		int maxBasicPaymentHIT = 0;
		for (PlatformIdentity platform : PlatformIdentity.values()) {
			if (exp.getConfig().getSendCreativeTo(platform)) {
				maxBasicPaymentAnswer = maxBasicPaymentAnswer < exp.getBasicPaymentAnswerCents(platform)?
						exp.getBasicPaymentAnswerCents(platform) : maxBasicPaymentAnswer;
			}
			if (exp.getConfig().getSendRatingTo(platform)) {
				maxBasicPaymentRating = maxBasicPaymentRating < exp.getBasicPaymentRatingCents(platform)?
						exp.getBasicPaymentRatingCents(platform) : maxBasicPaymentRating;
			}
			if (exp.getConfig().getSendCreativeTo(platform) || exp.getConfig().getSendRatingTo(platform)) {
				maxBasicPaymentHIT = maxBasicPaymentHIT < exp.getBasicPaymentHITCents(platform)?
						exp.getBasicPaymentHITCents(platform) : maxBasicPaymentHIT;
			}
		}
		// we pay out these two using a bonus
		int answerPayments = exp.getMaxNumberOfAnswersPerAssignment() * maxBasicPaymentAnswer;
		/*-?|Anika|Anika|c9|?*/
		int ratingPayments = exp.getMaxNumberOfRatingsPerAssignment() * maxBasicPaymentRating;

		// so the total budget hit is the basic HIT approval payment plus these worst-case bonuses
		return maxBasicPaymentHIT + answerPayments + ratingPayments;
	}

	@Override
	public void run(MutableExperiment ex) throws DatabaseException {
		for (MutableAssignment as : ex.getAssignments()) {
			int bonus = getBonusPayOutBasedOnCompletedTask(ex, as);
			for (MutableAnswer a : as.getTaskConstellation().getAnswers()) {
				if (a.isInvalid()) {
					as.setPaymentOutcome(false, 0);
					return;
				}
			}
			if(bonus == 0) {
				as.setPaymentOutcome(true, 0);
			}
			else {
			as.setPaymentOutcome(true, bonus);
			}
		}
	}

	protected int getBonusPayOutBasedOnCompletedTask(MutableExperiment ex,
			MutableAssignment as) throws DatabaseException {
		
		PlatformIdentity platform = PlatformIdentity.getIdentityFromPrefix(as.getMTurkAssignmentID().getId());
		
		int nAnswers = 0;
		int nRatings = 0;

		for (@SuppressWarnings("unused")
		Rating r : as.getTaskConstellation().getRatings()) {
			nRatings++;
		}

		for (Answer a : as.getTaskConstellation().getAnswers()) {
			if(!a.isInvalid() && a.getFinalQualityIndex() != 0) {
				nAnswers++;
			}
			else {
				if (a.isInvalid()) {
					return 0;
				}
			}
		}

		int bonusAmount = ex.getBasicPaymentAnswerCents(platform) * nAnswers
				+ ex.getBasicPaymentRatingCents(platform) * nRatings;
		
		int bonusCap = worstCasePayoutPerAssignment(ex) - ex.getBasicPaymentHITCents(platform);
		int result = Math.min(bonusAmount, bonusCap);
		return result;
	}

	@Override
	public int getBonusPool() {
		return 0;
	}

}

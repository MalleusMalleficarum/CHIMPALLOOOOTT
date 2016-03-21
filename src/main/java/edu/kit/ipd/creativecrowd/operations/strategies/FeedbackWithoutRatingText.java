package edu.kit.ipd.creativecrowd.operations.strategies;

import edu.kit.ipd.creativecrowd.mutablemodel.MutableAssignment;
import edu.kit.ipd.creativecrowd.operations.FeedbackTextGenerator;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;

/**
 * default feedback generator, generates feedback messages with the text specified by the requester or with default values
 * 
 * @author Anika
 *         StrategyParams
 *         -- rejection_message : message for workers whose work is rejected
 *         -- approval_message : message for workers whose work was approved
 *         -- bonus_granted_message : message for workers who got an additional bonus
 */
public class FeedbackWithoutRatingText extends StrategyWithParams implements FeedbackTextGenerator {
	private static String REJECT = "Dear Worker,\nwe regret to inform you that your work is not up to our standards! ";
	private static String APPROVE = "Dear Worker,\nwe´re happy to approve your work. Thanks for participating in our studies!";
	private static String BONUSGRANTED = "Dear Worker,\nwe´re happy to grant you a bonus. Please participate in future tasks again!";

	@Override
	public String generateFeedback(MutableAssignment as) throws DatabaseException {
		if (!as.getPaymentOutcome().isApproved()) {

			return this.getStringParam("ftg.rejection_message", REJECT);
		}
		else {
			return this.getStringParam("ftg.approval_message", APPROVE);
		}
	}

	@Override
	public String generateBonusMessage(MutableAssignment as) throws DatabaseException {
		return this.getStringParam("ftg.bonus_granted_message", BONUSGRANTED);

	}

}

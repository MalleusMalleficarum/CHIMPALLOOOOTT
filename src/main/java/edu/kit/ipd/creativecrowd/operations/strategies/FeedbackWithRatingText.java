package edu.kit.ipd.creativecrowd.operations.strategies;

import edu.kit.ipd.creativecrowd.mutablemodel.MutableAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAssignment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRating;
import edu.kit.ipd.creativecrowd.operations.FeedbackTextGenerator;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;

/**
 * This FTG adds free text answers given by workers who submitted ratings to the feedbackmessage
 * 
 * @author Anika
 *         * StrategyParams
 *         -- rejection_message : message for workers whose work is rejected
 *         -- approval_message : message for workers whose work was approved
 *         -- bonus_granted_message : message for workers who got an additional bonus
 */
public class FeedbackWithRatingText extends StrategyWithParams implements
		FeedbackTextGenerator {
	private static String REJECT = "Dear Worker,\nwe regret to inform you that your work is not up to our standards! ";
	private static String APPROVE = "Dear Worker,\nwe´re happy to approve your work. Thanks for participating in our studies!";
	private static String BONUSGRANTED = "Dear Worker,\nwe´re happy to grant you a bonus. Please participate in future tasks again!";

	@Override
	public String generateFeedback(MutableAssignment as)
			throws DatabaseException {
		String feedback = "";
		if (!as.getPaymentOutcome().isApproved()) {
			feedback += this.getStringParam("ftg.rejection_message", REJECT) + "\n";
		} else {
			feedback += this.getStringParam("ftg.approval_message", APPROVE) + "\n";
		}
		boolean first = true;
		for (MutableAnswer ans : as.getTaskConstellation().getAnswers()) {
			if (first) {
				feedback += ans.getCreativeTask().getDescription() + "\n";
				first = false;
			}
			feedback += "Your answer was:" + ans.getText() + "\n";
			for (MutableRating rat : ans.getRatings()) {
				feedback += "Another worker said about it:" + rat.getText() + "\n";
			}
		}
		return feedback;
	}

	@Override
	public String generateBonusMessage(MutableAssignment as)
			throws DatabaseException {
		return this.getStringParam("ftg.bonus_granted_message", BONUSGRANTED);

	}

}

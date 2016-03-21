package edu.kit.ipd.creativecrowd.operations;

import edu.kit.ipd.creativecrowd.mutablemodel.MutableAssignment;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;

/**
 * Strategyinterface for generating a text which is send to a worker as feedback
 * 
 * @author Anika
 */
public interface FeedbackTextGenerator extends AbstractStrategy {
	/**
	 * generates feedback message for worker submitting this assignment
	 * 
	 * @param as the assignment which was submitted by the worker
	 * @return the feedback text
	 * @throws DatabaseException if the assignment couldn´t be loaded succesfully
	 */
	String generateFeedback(MutableAssignment as) throws DatabaseException;

	/**
	 * generates a message to be send when a worker gets an bonus
	 * 
	 * @param as assignment submitted by the worker to get bonus
	 * @return the message to be send
	 * @throws DatabaseException if the assignment couldn´t bel loaded succesfully
	 */
	String generateBonusMessage(MutableAssignment as) throws DatabaseException;

}

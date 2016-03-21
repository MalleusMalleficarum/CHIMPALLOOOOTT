package edu.kit.ipd.creativecrowd.operations;

import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.Experiment;

/**
 * strategy interface for calculating the payment of all assignments
 * 
 * @author Anika & Jonas
 */
public interface AssignmentPaymentOutcomeCalculator extends AbstractStrategy {
	/**
	 * calculates the payment for all submitted assignment in the given experiment
	 * 
	 * @param ex experiment whose assignment need to be paid
	 * @throws DatabaseException if the assignments can´t be loaded successfully from database
	 */
	void run(MutableExperiment ex) throws DatabaseException;

	/**
	 * calculates the upper bound for a payment outcome per one assignment
	 * 
	 * @param exp experiment whose assignment can´t cost more than calculated
	 * @return an amount of cents to be paid in worst case
	 * @throws DatabaseException if the experiment data can´t be loaded successfully
	 */
	int worstCasePayoutPerAssignment(Experiment exp) throws DatabaseException;

	/**
	 * @return the amount of cents the requester wants to give as a bonus for good work
	 */
	int getBonusPool();

}

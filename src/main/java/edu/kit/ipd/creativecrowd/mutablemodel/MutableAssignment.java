package edu.kit.ipd.creativecrowd.mutablemodel;

import edu.kit.ipd.creativecrowd.mturk.*;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.*;

/**
 * adds setter-methods to Assignment objects.
 * 
 * @see edu.kit.ipd.creativecrowd.readablemodel.Assignment
 * @author simon
 */
public interface MutableAssignment extends Assignment {

	/**
	 * Sets the submitted.
	 *
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public void setSubmitted() throws DatabaseException;

	/**
	 * Sets the payment outcome.
	 *
	 * @param receives_base_payment the receives_base_payment
	 * @param bonusPaymentAmountInCents the bonus payment amount in cents
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public void setPaymentOutcome(boolean receives_base_payment, int bonusPaymentAmountInCents) throws DatabaseException;

	/**
	 * Sets the worker.
	 *
	 * @param id the new worker
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public void setWorker(WorkerId id) throws DatabaseException;

	/**
	 * Mark as paid.
	 *
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public void markAsPaid() throws DatabaseException;

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.Assignment#getTaskConstellation()
	 */
	public MutableTaskConstellation getTaskConstellation() throws DatabaseException;

	/**
	 * Sets the assignment id.
	 *
	 * @param id the new assignment id
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public void setAssignmentID(AssignmentId id) throws DatabaseException;
}

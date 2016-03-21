package edu.kit.ipd.creativecrowd.readablemodel;

import edu.kit.ipd.creativecrowd.mturk.AssignmentId;
import edu.kit.ipd.creativecrowd.mturk.WorkerId;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;

// TODO: Auto-generated Javadoc

/**
 * provides read-only methods for an assignment.
 *
 * @author simon
 */
public interface Assignment {

	/**
	 * Checks if this Assignment is in state "submitted"
	 *
	 * @return true, if is submitted
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public boolean isSubmitted() throws DatabaseException;

	/**
	 * Gets the task constellation on which this assignment works on
	 *
	 * @return the task constellation
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public TaskConstellation getTaskConstellation() throws DatabaseException;

	/**
	 * Checks if this is Assignment is paid
	 *
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public boolean isPaid() throws DatabaseException;

	/**
	 * Gets the m turk assignment id.
	 *
	 * @return the m turk assignment id
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public AssignmentId getMTurkAssignmentID() throws DatabaseException;

	/**
	 * Gets the database-internal id.
	 *
	 * @return the id
	 */
	public String getID();

	/**
	 * Gets the mturk worker id.
	 *
	 * @return the mturk worker id
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public WorkerId getWorkerID() throws DatabaseException;

	/**
	 * Gets the payment outcome belonging to this assignment
	 *
	 * @return the payment outcome
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public PaymentOutcome getPaymentOutcome() throws DatabaseException;

	/**
	 * gets the Experiment this Assignment is working on
	 * 
	 * @return an EXperiment
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public Experiment getExperiment() throws DatabaseException;
}

package edu.kit.ipd.creativecrowd.readablemodel;

import edu.kit.ipd.creativecrowd.mturk.AssignmentId;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;

/**
 * provides read-only information about an experiment. An Experiment represents the collection of data belonging to a HIT on MTurk?
 *
 * @author simon
 */
public interface Experiment extends AbstractExperiment {

	/**
	 * gets all RatingTasks which search ratings for a CreativeTask that belongs to this experiment
	 *
	 * @return all RatingTasks belonging to this experiment as Iterable
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public Iterable<? extends RatingTask> getRatingTasks() throws DatabaseException;

	/**
	 * Gets the CreativeTask which belongs to this experiment.
	 *
	 * @return the creative task
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	// to implement more than one CreativeTask, change this Method to getCreativeTasks() and reimplement it
	public CreativeTask getCreativeTask() throws DatabaseException;

	/**
	 * Gets some statistics about this experiment
	 * 
	 * @see edu.kit.ipd.creativecrowd.readablemodel.Stats
	 * @return the stats
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public Stats getStats() throws DatabaseException;

	/**
	 * Retrieves the assignment which has the given internal database assignment id
	 * 
	 * @param assgId internal database assignment-id (aka Assignment.getId() )
	 * @return the Assignment.
	 * @throws DatabaseException if the given assignment ID is invalid.
	 */
	public Assignment getAssignment(String assgId) throws DatabaseException;

	/**
	 * gets all Assignment which work on Creative-/Rating-Tasks which belong to this experiment
	 * 
	 * @return all Assignments working on this Experiment
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public Iterable<? extends Assignment> getAssignments() throws DatabaseException;

	/**
	 * is the experiment marked as finished?
	 * 
	 * @return true if finished, false if not
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public boolean isFinished() throws DatabaseException;

	/**
	 * Gets the Assignment which has the given ID on MTurk
	 * 
	 * @param mtrkid the Assignment's id on MTurk
	 * @return the fitting assignment, or null if assignment with the given mturk id doesn't exist
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public Assignment getAssignmentWithMturkId(AssignmentId mtrkid) throws DatabaseException;

	/**
	 * returns all available RatingOptions for an Experiment
	 * 
	 * @return an Iterable containing all RatingOptions
	 */
	public Iterable<? extends RatingOption> getRatingOptions() throws DatabaseException;

	/**
	 * Gets this experiment's MTurk-Id
	 * 
	 * @return the Id of the HIT on Mturk belonging to this experiment
	 */
	public String getHitID() throws DatabaseException;

}

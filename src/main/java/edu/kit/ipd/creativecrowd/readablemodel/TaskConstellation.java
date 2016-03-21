package edu.kit.ipd.creativecrowd.readablemodel;

import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;

// TODO: Auto-generated Javadoc
/**
 * information about a TaskConstellation that a worker is currently working on.
 *
 * @author simon
 */
public interface TaskConstellation {

	/**
	 * Gets the id of a task constellation
	 *
	 * @return the id of a task constellation
	 */
	public String getID();

	/**
	 * Gets the tasks.
	 *
	 * @return the tasks
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public Iterable<? extends Task> getTasks() throws DatabaseException;

	/**
	 * Next button exists.
	 *
	 * @return true, if successful
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public boolean nextButtonExists() throws DatabaseException;

	/**
	 * Again button exists.
	 *
	 * @return true, if successful
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public boolean againButtonExists() throws DatabaseException;

	/**
	 * Submit button exists.
	 *
	 * @return true, if successful
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public boolean submitButtonExists() throws DatabaseException;

	/**
	 * Gets the current task.
	 *
	 * @return the current task
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public Task getCurrentTask() throws DatabaseException;

	/**
	 * gets the index of the current task in the taskconstellation
	 * 
	 * @return index of the current task, -1 if the taskconstellation is empty
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public int getCurrentTaskIndex() throws DatabaseException;

	/**
	 * gets answers which belong to this TaskConstellation
	 * 
	 * @return
	 * @throws DatabaseException
	 */
	public Iterable<? extends Answer> getAnswers() throws DatabaseException;

	/**
	 * gets Ratings which belong to this TC
	 * 
	 * @return
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public Iterable<? extends Rating> getRatings() throws DatabaseException;

	/**
	 * gets the assignment of the TaskConstellation
	 * 
	 * @return the assignment of the task constellation
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public Assignment getAssignment() throws DatabaseException;
}

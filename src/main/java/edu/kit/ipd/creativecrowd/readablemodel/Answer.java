package edu.kit.ipd.creativecrowd.readablemodel;

import edu.kit.ipd.creativecrowd.mutablemodel.MutableWorker;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;

// TODO: Auto-generated Javadoc
/**
 * the Answer interface, has read-only methods about Answer objects.
 *
 * @author simon
 */
public interface Answer {

	/**
	 * Gets the timestamp.
	 *
	 * @return the timestamp in SQL-Format : YYYY-MM-DD HH:MI:SS
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public String getTimestampBegin() throws DatabaseException;

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getID();

	/**
	 * Gets the text which was written as content for this Answer
	 *
	 * @return the text
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public String getText() throws DatabaseException;

	/**
	 * Gets all Ratings which rate this Answer
	 *
	 * @return the ratings
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public Iterable<? extends Rating> getRatings() throws DatabaseException;

	/**
	 * Checks if this Answer was set to "sufficientlyRated"
	 *
	 * @return true, if is sufficiently rated
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public boolean isSufficientlyRated() throws DatabaseException;

	/**
	 * Checks if this Answer belongs to an Assignment which has already been submitted
	 *
	 * @return true, if is submitted
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public boolean isSubmitted() throws DatabaseException;

	/**
	 * get the final Quality Index, which was evaluated by the ratings
	 *
	 * @return the answer's quality Index
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public float getFinalQualityIndex() throws DatabaseException;
	
	/**
	 * get the worker who has written this answer
	 *
	 * @return worker
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public MutableWorker getWorker() throws DatabaseException;
	
	public boolean isInvalid() throws DatabaseException;
}

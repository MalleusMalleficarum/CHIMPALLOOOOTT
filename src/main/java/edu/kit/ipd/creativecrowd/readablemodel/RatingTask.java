package edu.kit.ipd.creativecrowd.readablemodel;

import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;

// TODO: Auto-generated Javadoc
/**
 * get information about a RatingTask being worked on by a worker.
 *
 * @author simon
 */
public interface RatingTask extends Task {

	/**
	 * Gets the ratings.
	 *
	 * @return the ratings
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public Iterable<? extends Rating> getRatings() throws DatabaseException;

	/**
	 * Gets the answers to be rated.
	 *
	 * @return the answers to be rated
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public Iterable<? extends Answer> getAnswersToBeRated() throws DatabaseException;

}

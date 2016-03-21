package edu.kit.ipd.creativecrowd.mutablemodel;

import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.RatingTask;

/**
 * adds setter-functionality to the RatingTask class.
 * 
 * @see edu.kit.ipd.creativecrowd.readablemodel.RatingTask
 * @author simon
 */
public interface MutableRatingTask extends RatingTask {

	/**
	 * Adds the answer to be rated.
	 *
	 * @param as the as
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public void addAnswerToBeRated(MutableAnswer as) throws DatabaseException;

	/**
	 * gets the Answer with the given id, which was added to this ratingtask previously
	 * 
	 * @param ansId the answer's id
	 * @return the Answer which has the given id
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public MutableAnswer getAnswerToBeRated(String ansId) throws DatabaseException;

	/**
	 * gets all answers, which were added to this ratingtask previously
	 * 
	 * @return a list of all Answers of the RatingTask
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public Iterable<? extends MutableAnswer> getAnswersToBeRated() throws DatabaseException;

	/*-?|Test Repo-Review|Philipp|c3|?*/

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.RatingTask#getRatings()
	 */
	public Iterable<? extends MutableRating> getRatings() throws DatabaseException;

}

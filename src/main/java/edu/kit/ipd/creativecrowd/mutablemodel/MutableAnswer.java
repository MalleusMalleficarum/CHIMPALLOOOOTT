package edu.kit.ipd.creativecrowd.mutablemodel;

import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.*;

/**
 * contains setter-methods for Answer objects.
 * 
 * @see edu.kit.ipd.creativecrowd.readablemodel.Answer
 * @author simon
 */
public interface MutableAnswer extends Answer {

	/**
	 * Sets the text.
	 *
	 * @param answer the new text
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public void setText(String answer) throws DatabaseException;

	/**
	 * Adds the rating.
	 *
	 * @param rating the rating
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public void addRating(MutableRating rating) throws DatabaseException;

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.Answer#getRatings()
	 */
	public Iterable<? extends MutableRating> getRatings() throws DatabaseException;

	/**
	 * Mark as rated.
	 *
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public void markAsRated() throws DatabaseException;

	/**
	 * Set the final quality index of the answer.
	 * 
	 * @throws DatabaseException
	 */
	public void setFinalQualityIndex(float idx) throws DatabaseException;

	/**
	 * Returns the creative task this answer belongs to.
	 * (Jonas: ich glaube, das brauchen wir. Bitte hauen falls Protest)
	 * 
	 * @return the mutable creative task
	 */
	public MutableCreativeTask getCreativeTask() throws DatabaseException;

}

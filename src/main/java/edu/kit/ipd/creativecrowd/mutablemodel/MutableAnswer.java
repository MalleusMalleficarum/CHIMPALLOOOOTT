package edu.kit.ipd.creativecrowd.mutablemodel;

import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.*;

/**
 * contains setter-methods for Answer objects.
 * 
 * @see edu.kit.ipd.creativecrowd.readablemodel.Answer
 * @author Bastian, simon
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
	
	
	/**
	 * sets the ID of the worker who has written this answer
	 * 
	 * @param workerid
	 */
	public void setWorkerID(String workerID) throws DatabaseException;
	
	/**
	 * Returns the mturk assignment ID of the mturk assignment the answer belongs to
	 * @return the mturk assignment ID 
	 * @throws DatabaseException 
	 */
	public String getMturkAssignmentId() throws DatabaseException;
	/**
	 * sets the invalid flag of an answer, this is done if the answer is a duplicate or spoof
	 * @throws DatabaseException 
	 */
	public void markAsInvalid() throws DatabaseException;
	/**
	 * returns the invalid flag of this answer
	 * @return whether the answer is invalid
	 * @throws DatabaseException 
	 */
	public boolean isInvalid() throws DatabaseException;

}

package edu.kit.ipd.creativecrowd.mutablemodel;

import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.Rating;
import edu.kit.ipd.creativecrowd.readablemodel.RatingOption;

/**
 * adds setter functionality to the Rating class.
 * 
 * @see edu.kit.ipd.creativecrowd.readablemodel.Rating
 * @author simon
 */
public interface MutableRating extends Rating {

	/**
	 * Sets the text.
	 *
	 * @param text the new text
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public void setText(String text) throws DatabaseException;

	/**
	 * Sets the rating option.
	 *
	 * @param rOptFin the new rating option
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public void setRatingOption(RatingOption rOptFin) throws DatabaseException;

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.Rating#getSelectedRatingOption()
	 */
	public MutableRatingOption getSelectedRatingOption() throws DatabaseException;

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.Rating#getAnswer()
	 */
	/*-?|Test Repo-Review|MainUser|c0|?*/
	public MutableAnswer getAnswer() throws DatabaseException;

	/**
	 * Set the final quality index of the rating.
	 * 
	 * @param idx the final quality index
	 * @throws DatabaseException
	 */
	public void setFinalQualityIndex(float idx) throws DatabaseException;
	

	
	/* 
	 * sets the ID of the worker who has done this rating
	 * 
	 * @param workerid
	 */
	public void setWorkerID(String workerID) throws DatabaseException;
}

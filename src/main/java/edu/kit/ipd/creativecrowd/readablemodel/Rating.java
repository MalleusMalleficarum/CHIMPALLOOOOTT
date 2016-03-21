package edu.kit.ipd.creativecrowd.readablemodel;

import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;

// TODO: Auto-generated Javadoc
/**
 * get information about a particular rating given by a worker.
 *
 * @author simon
 */
public interface Rating {

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getID();

	/**
	 * Gets the selected rating option.
	 *
	 * @return the selected rating option
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public RatingOption getSelectedRatingOption() throws DatabaseException;

	/**
	 * Gets the text.
	 *
	 * @return the text
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public String getText() throws DatabaseException;

	/**
	 * Gets the answer.
	 *
	 * @return the answer
	 * @throws DatabaseException
	 */
	public Answer getAnswer() throws DatabaseException;

	/**
	 * Returns the final quality index set for the rating.
	 * 
	 * @return the final quality index
	 * @throws DatabaseException
	 */
	public float getFinalQualityIndex() throws DatabaseException;
}

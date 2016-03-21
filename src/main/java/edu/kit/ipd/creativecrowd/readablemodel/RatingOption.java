package edu.kit.ipd.creativecrowd.readablemodel;

import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;

// TODO: Auto-generated Javadoc
/**
 * get information about a RatingOption chosen by a worker.
 *
 * @author simon
 */
public interface RatingOption {

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getID();

	/**
	 * Gets the value.
	 *
	 * @return the value
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public float getValue() throws DatabaseException;

	/**
	 * Gets the text.
	 *
	 * @return the text
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public String getText() throws DatabaseException;
}

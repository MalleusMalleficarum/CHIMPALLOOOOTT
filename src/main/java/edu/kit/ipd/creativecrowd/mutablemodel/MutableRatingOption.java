package edu.kit.ipd.creativecrowd.mutablemodel;

import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.RatingOption;

/**
 * adds setter functionality to the RatingOption class.
 * 
 * @see edu.kit.ipd.creativecrowd.readablemodel.RatingOption
 * @author simon
 */
public interface MutableRatingOption extends RatingOption {

	/**
	 * Sets the text.
	 *
	 * @param name the new text
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public void setText(String name) throws DatabaseException;

	/**
	 * Sets the value.
	 *
	 * @param rating the new value
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public void setValue(float rating) throws DatabaseException;
}

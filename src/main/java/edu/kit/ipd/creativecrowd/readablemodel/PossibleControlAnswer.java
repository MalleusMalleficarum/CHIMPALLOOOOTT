package edu.kit.ipd.creativecrowd.readablemodel;

import edu.kit.ipd.creativecrowd.mutablemodel.MutableControlQuestion;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;

/**
 * the  PossibleCalibrationAnswer interface, has read-only methods about PossibleCalibrationAnswer objects.
 *
 * @author basti
 */
public interface PossibleControlAnswer {
	
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
	 * Gets true if the answer is a correct answer.
	 * else gets false
	 *
	 * @return isTrue
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public boolean getIsTrue() throws DatabaseException;
	
	/* 
	 * gets the ID of the calibrationquestion that contains the answer
	 * 
	 * @param calibrationquestionid
	 */
	public MutableControlQuestion getControlQuestion() throws DatabaseException;

}

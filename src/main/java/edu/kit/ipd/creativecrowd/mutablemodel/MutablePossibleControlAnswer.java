/**
 * 
 */
package edu.kit.ipd.creativecrowd.mutablemodel;

import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.PossibleControlAnswer;

/**
 * @author basti
 *
 */
public interface MutablePossibleControlAnswer extends PossibleControlAnswer {

	/**
	 * Sets the text.
	 *
	 * @param answer the new text
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public void setText(String answer) throws DatabaseException;
	
	
	/**
	 * sets isTrue
	 * 
	 * @param isTrue
	 */
	public void  setIsTrue(boolean isTrue) throws DatabaseException;
	
	/* 
	 * sets Controlquestion wich contains this answer
	 * 
	 * @param controlquestionid
	 */
	public void setControlQuestion(String controlquestID) throws DatabaseException;
}

/**
 * 
 */
package edu.kit.ipd.creativecrowd.mutablemodel;

import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.ControlAnswer;

/**
 * @author basti
 *
 */
public interface MutableControlAnswer extends ControlAnswer {
	/**
	 * Sets the correctness
	 *
	 * @param boolean to set correct
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public void setIsCorrect(boolean correct) throws DatabaseException;
	

	
	/* 
	 * sets Controlquestion wich contains this answer
	 * 
	 * @param controlquestionid
	 */
	public void setControlQuestion(String controlquestID) throws DatabaseException;
	

}

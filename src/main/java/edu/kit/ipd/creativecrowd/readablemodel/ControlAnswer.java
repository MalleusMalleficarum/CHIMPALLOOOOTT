package edu.kit.ipd.creativecrowd.readablemodel;

import edu.kit.ipd.creativecrowd.mutablemodel.MutableControlQuestion;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;

/**
 * This class represents a controlanswer from a controlquestion  done by a worker
 * 
 * @author Bastian
 *
 */
public interface ControlAnswer {
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getID();
	
	/**
	 * Gets the answer.
	 * @return answer
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public boolean getIsCorrect()throws DatabaseException;
	
	
	/**
	  *  Gets the id of the controlquestion which has  this answer
	 * @return controlquestion
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public MutableControlQuestion getControlQuestion()throws DatabaseException;

}

package edu.kit.ipd.creativecrowd.mutablemodel;

import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.CalibrationAnswer;

/**
 * adds setter functionality to the CalibrationAnswer class.
 * 
 * @see edu.kit.ipd.creativecrowd.readablemodel.CalibrationAnswer
 * @author Bastian
 */
public interface MutableCalibrationAnswer extends CalibrationAnswer {

	/**
	 * Sets the answer
	 *
	 * @param answer the new answer
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public void setAnswer(String answer) throws DatabaseException;
	
	/**
	 * Gets the answer. If the answer is an integer, it will be marked with /INT/
	 *
	 * @return answer  
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public String getAnswer() throws DatabaseException;

	/**
	 * Sets the answer
	 *
	 * @param answer the new answer
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public void setAnswer(int answer) throws DatabaseException;
	
	
	
	/** 
	 * sets the ID of the worker who has done this rating
	 * 
	 * @param workerid
	 */
	public void setWorkerID(String workerID) throws DatabaseException;
	
	/** 
	 * sets Calibrationquestion wich contains this answer
	 * 
	 * @param calibrationquestionid
	 */
	public void setCalibrationQuestion(String calibquestID) throws DatabaseException;
	
	/**
	 * is answer correct?
	 * @return true if correct, false else
	 * @throws DatabaseException
	 */
	public boolean isCorrect() throws DatabaseException;
	
}
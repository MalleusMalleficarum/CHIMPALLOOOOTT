package edu.kit.ipd.creativecrowd.readablemodel;

import edu.kit.ipd.creativecrowd.mutablemodel.MutableCalibrationQuestion;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableWorker;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;

/**
 * This class represents a calibrationanswer from a calibrationquestion  written by a worker
 * 
 * @author Thomas Friedel, Bastian
 *
 */
public interface CalibrationAnswer {
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getID();
	
	/**
	 * Gets the answer. If the answer is an integer, it will be marked as /INT/
	 * @return answer
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public String getAnswer()throws DatabaseException;
	
	/**
	 *  Gets the  worker who has written this answer
	 * @return worker
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public MutableWorker getWorker()throws DatabaseException;
	
	/**
	 *  Gets the id of the calibrationquestion which has  this answer
	 * @return calibrationquestion
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public MutableCalibrationQuestion getCalibrationQuestion()throws DatabaseException;

}

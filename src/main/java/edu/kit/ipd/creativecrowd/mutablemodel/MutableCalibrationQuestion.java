package edu.kit.ipd.creativecrowd.mutablemodel;

import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.*;

/**
 * adds setter-functionality to the CalibrationQuestion class.
 * 
 * @see edu.kit.ipd.creativecrowd.readablemodel.CalibrationQuestion
 * @author Bastian
 */
public interface MutableCalibrationQuestion extends CalibrationQuestion {

	/**
	 * Sets the question.
	 *
	 * @param question
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public void setQuestion(String question) throws DatabaseException;
	

	/**
	 * adds a correct answer
	 *
	 * @param answer
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public MutablePossibleCalibrationAnswer addPossibleAnswer() throws DatabaseException;
	

	
	/**
	 * adds an answer from a worker
	 *
	 * @param answer
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public MutableCalibrationAnswer addAnswer() throws DatabaseException;
	
	
	
	/**
	 * Removes the given possible answer
	 * 
	 * @param id answer to be removed
	 * @throws DatabaseException
	 */
	public void removePossibleAnswer(String id) throws DatabaseException;

	/**
	 * removes the experiment
	 * @param name experiment to remove
	 * @throws DatabaseException
	 */
	public void removeExperiment(String name) throws DatabaseException;

	/**
	 * adds an experiment
	 * @param name experiment to add
	 * @throws DatabaseException
	 */
	public void addExperiment(String name) throws DatabaseException;
}

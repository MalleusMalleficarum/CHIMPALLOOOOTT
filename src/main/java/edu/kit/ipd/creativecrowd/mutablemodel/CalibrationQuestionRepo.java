package edu.kit.ipd.creativecrowd.mutablemodel;

import java.util.List;

import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.persistentmodel.IDAlreadyUsedException;
import edu.kit.ipd.creativecrowd.persistentmodel.IDNotFoundException;

/**
 * Contains and manages multiple CalibrationQuestions, represents a persistent storage
 * for CalibrationQuestions. Using this repo one can create, load, and delete
 * CalibrationQuestions. The class builds up the database structure.
 * 
 * @author Thomas Friedel
 * 
 */
public interface CalibrationQuestionRepo {
	
	/**
	 * Creates a new CalibrationQuestion
	 * 
	 * @param id the id of the CalibrationQuestion
	 * @return the CalibrationQuestion
	 * @throws DatabaseException if the SQL request fails
	 * @throws IDAlreadyUsedException if the id is already used by another calibration question 
	 */
	public MutableCalibrationQuestion createCalibrationQuestion(String id) throws DatabaseException, IDAlreadyUsedException;

	/**
	 * Loads an existing CalibrationQuestion
	 * 
	 * @param id the id of the CalibrationQuestion
	 * @return the CalibrationQuestion
	 * @throws DatabaseException if the SQL query fails
	 * @throws IDNotFoundException if an CalibrationQuestion with that ID does not exist
	 */
	public MutableCalibrationQuestion loadCalibrationQuestion(String id) throws DatabaseException, IDNotFoundException;
	
	/**
	 * Deletes a CalibrationQuestion
	 * 
	 * @param id the id of the CalibrationQuestion
	 * @throws DatabaseException if the SQL request fails (maybe due to the non-existence of the CalibrationQuestion)
	 */
	public void deleteCalibrationQuestion(String id) throws DatabaseException;
	
	/**
	 * Loads all existing CalibrationQuestions
	 * 
	 * @return a list of all CalibrationQuestions
	 * @throws DatabaseException if the SQL request fails
	 */
	public List<MutableCalibrationQuestion> loadAllCalibrationQuestions() throws DatabaseException;
	
}

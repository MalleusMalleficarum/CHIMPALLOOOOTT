package edu.kit.ipd.creativecrowd.mutablemodel;

import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.*;

/**
 * contains setter-methods for Answer objects.
 * 
 * @see edu.kit.ipd.creativecrowd.readablemodel.PossibleCalibrationAnswer
 * @author Bastian
 */
public interface MutablePossibleCalibrationAnswer extends PossibleCalibrationAnswer {
	
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
	 * sets Calibrationquestion wich contains this answer
	 * 
	 * @param calibrationquestionid
	 */
	public void setCalibrationQuestion(String calibquestID) throws DatabaseException;
}

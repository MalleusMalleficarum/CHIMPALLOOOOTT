package edu.kit.ipd.creativecrowd.mutablemodel;

import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.*;

/**
 * adds setter-functionality to the ControlQuestion class.
 * 
 * @see edu.kit.ipd.creativecrowd.readablemodel.ControlQuestion
 * @author Bastian
 */
public interface MutableControlQuestion extends ControlQuestion {

	/**
	 * Sets the question.
	 *
	 * @param question
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public void setQuestion(String question) throws DatabaseException;
	
	/**
	 * gets the question
	 * 
	 * @return question
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public String getQuestion() throws DatabaseException;
	
	/**
	 * adds a possible answer for the worker to choose if he must answer this question
	 *
	 * @param answer
	 * @param isTrue if answer is correct
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public MutablePossibleControlAnswer addPossibleAnswer() throws DatabaseException;
	
	/**
	 * Gets the possible answers for this question
	 *
	 * @return all possible answers
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public Iterable<? extends MutablePossibleControlAnswer> getPossibleAnswers() throws DatabaseException;
	
	/**
	 * adds a answer from a worker
	 *
	 * @param answer
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public MutableControlAnswer addAnswer() throws DatabaseException;
	
	/**
	 * Gets the answers for this question
	 *
	 * @return all answers
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public Iterable<? extends MutableControlAnswer> getAnswers() throws DatabaseException;
	
	/**
	 * removes a possible answer
	 * @param answer to be removed
	 * @throws DatabaseException
	 */
	public void removePossibleAnswer(String answer) throws DatabaseException;

}

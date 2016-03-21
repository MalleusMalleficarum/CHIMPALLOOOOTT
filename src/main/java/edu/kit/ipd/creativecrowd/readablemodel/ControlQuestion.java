package edu.kit.ipd.creativecrowd.readablemodel;

import edu.kit.ipd.creativecrowd.mutablemodel.MutablePossibleControlAnswer;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;

/**
 * provides read-only methods about a controlquestion. A controlquestion represents a questiom which provide that the worker has understand the task
 *
 * @author Bastian, Thomas Friedel
 */
public interface ControlQuestion extends Task {
	
	/**
	 * gets the Question 
	 * 
	 * @return Question
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public String getQuestion() throws DatabaseException;
	
	/**
	 * Gets the possible answers for this question
	 *
	 * @return all possible answers
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public Iterable<? extends MutablePossibleControlAnswer> getPossibleAnswers() throws DatabaseException;

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.Task#getID()
	 */
	@Override
	public String getID();

}

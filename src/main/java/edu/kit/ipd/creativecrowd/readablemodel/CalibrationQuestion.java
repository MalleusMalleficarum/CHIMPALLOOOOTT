package edu.kit.ipd.creativecrowd.readablemodel;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutablePossibleCalibrationAnswer;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;

/**
 * provides read-only methods about a calibrationquestion. A calibrationquestion represents a questiom which provide that the worker has the right classification for this task
 *
 * @author Bastian
 */
@JsonPropertyOrder({ "id", "question", "experimentnames", "possibleanswers"})
public interface CalibrationQuestion extends Task {
	
	/**
	 * Gets the experiments from this question
	 *
	 * @return all experiments
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public List<? extends MutableExperiment> getExperiments() throws DatabaseException;

	
	/**
	 * Gets the correct answers for this question
	 *
	 * @return all correct answers
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public Iterable<? extends MutablePossibleCalibrationAnswer> getPossibleAnswers() throws DatabaseException;
	
	/**
	 * Gets the answers which replied to this calibrationquestion
	 *
	 * @return all answers replying to this calibrationquestion
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public Iterable<? extends CalibrationAnswer> getCalibrationAnswers() throws DatabaseException;
	
	/**
	 * gets the Question 
	 * 
	 * @return Question
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	@JsonGetter("question")
	public String getQuestion() throws DatabaseException;

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.Task#getID()
	 */
	@JsonGetter("id")
	public String getID();
}
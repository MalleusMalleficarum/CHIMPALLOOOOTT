package edu.kit.ipd.creativecrowd.mutablemodel;

import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.TaskConstellation;

/**
 * adds setter functionality to the TaskConstellation class.
 * 
 * @see edu.kit.ipd.creativecrowd.readablemodel.TaskConstellation
 * @author simon
 */
public interface MutableTaskConstellation extends TaskConstellation {

	/**
	 * Sets the next button.
	 *
	 * @param next the new next
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public void setNextButton(boolean next) throws DatabaseException;

	/**
	 * Sets the again button.
	 *
	 * @param again the new again
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public void setAgainButton(boolean again) throws DatabaseException;

	/**
	 * Sets the submit button.
	 *
	 * @param submit the new submit
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public void setSubmitButton(boolean submit) throws DatabaseException;

	/**
	 * Sets the current task.
	 *
	 * @param taskPosition the new current task
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public void setCurrentTask(int taskPosition) throws DatabaseException;

	/**
	 * Adds the given CreativeTask to this TaskConstellation. If there is only one CreativeTask in the whole experiment,
	 * type yourTaskConstellation.getAssignment().getExperiment().getCreativeTask(); Otherwise this would have been done internally
	 * 
	 * @param ct the CreativeTask to be added to this TC
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public void addCreativeTask(MutableCreativeTask ct) throws DatabaseException;

	/**
	 * Gets the mutable creative task.
	 *
	 * @param taskPosition the task position
	 * @return the mutable creative task
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public MutableCreativeTask getMutableCreativeTask(int taskPosition) throws DatabaseException;

	/**
	 * Adds a rating task.
	 *
	 * @param task the rating task to be added
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public void addRatingTask(MutableRatingTask task) throws DatabaseException;

	/**
	 * Gets the mutable rating task.
	 *
	 * @param taskPosition the task position
	 * @return the mutable rating task. returns null if task at given position is not a ratingTask
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public MutableRatingTask getMutableRatingTask(int taskPosition) throws DatabaseException;

	/**
	 * Gets the Task Count.
	 *
	 * @return the current amount of tasks in this task constellation
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public int getTaskCount() throws DatabaseException;

	/**
	 * Answers the Creative Task in this TaskConstellation at given index
	 * 
	 * @param index which Task should be answered
	 * @return null if given index is >= this.getTaskCount(), else: a new created Answer (pointing to the Task with the given index) which has to be filled with values
	 * @throws DatabaseExcetpion if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public MutableAnswer answerCreativeTaskAt(int index) throws DatabaseException;

	/**
	 * Rates the RatingTask at the given index.
	 * 
	 * @param index which Task should be rated
	 * @return a new created Rating (pointing to the Task with the given index) which has to be filled with values
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public MutableRating addRatingToRatingTaskAt(int index) throws DatabaseException;

	/**
	 * gets answers, which reply to the CreativeTask at specified position
	 * 
	 * @param atTaskPosition the CreativeTask's position in this TaskConstellation
	 * @return if the Task at given position is a creativeTask: a List of all Answers the Worker gave this Task,
	 *         if the given index is out of range of this TaskConstellation: null
	 * @throws if the Task at given position is not a CreativeTask
	 */
	public Iterable<MutableAnswer> getAnswers() throws DatabaseException;

	/**
	 * gets ratings, which reply to the RatingTask at specified position
	 * 
	 * @param atTaskPosition the RatingTask's position in this TaskConstellation
	 * @return if the Task at given position is a RatingTask: a List of all Ratings the Worker gave this task,
	 *         if the given index is out of range of this TaskConstellation: null
	 * @throws if the Task at given position is not a RatingTask
	 */
	public Iterable<MutableRating> getRatings() throws DatabaseException;

	/**
	 * gets the assignment of the TaskConstellation
	 * 
	 * @return the assignment of the task constellation
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public MutableAssignment getAssignment() throws DatabaseException;
	
	/**
	 * Adds the given Controlquestion to this TaskConstellation. 
	 * 
	 * @param coq the Controlquestion to be added to this TC
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public void addControlQuestion(MutableControlQuestion coq) throws DatabaseException;

	/**
	 * Gets the mutablecontrolquestion.
	 *
	 * @param taskPosition the task position
	 * @return the mutablecontrolquestion
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public MutableControlQuestion getMutableControlQuestion(int taskPosition) throws DatabaseException;
	
	/**
	 * Adds the given Calibrationquestion to this TaskConstellation. 
	 * 
	 * @param caq the Calibrationquestion to be added to this TC
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public void addCalibrationQuestion(MutableCalibrationQuestion coq) throws DatabaseException;

	/**
	 * Gets the mutablecalibrationquestion.
	 *
	 * @param taskPosition the task position
	 * @return the mutablecalibrationquestion
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public MutableCalibrationQuestion getMutableCalibrationQuestion(int taskPosition) throws DatabaseException;
	
	
	/**
	 * Answers the MutableControlQuestionin this TaskConstellation at given index
	 * 
	 * @param index which Task should be answered
	 * @return null if given index is >= this.getTaskCount(), else: a new created Answer (pointing to the Task with the given index) which has to be filled with values
	 * @throws DatabaseExcetpion if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public MutableControlAnswer answerControlQuestionAt(int index) throws DatabaseException;
	/**
	 * Answers the MutableCalibrationQuestion in this TaskConstellation at given index
	 * 
	 * @param index which Task should be answered
	 * @return null if given index is >= this.getTaskCount(), else: a new created Answer (pointing to the Task with the given index) which has to be filled with values
	 * @throws DatabaseExcetpion if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public MutableCalibrationAnswer answerCalibrationQuestionAt(int index) throws DatabaseException;
	
	/**
	 * Answers the MutableCalibrationQuestion in this TaskConstellation at given id
	 * 
	 * @param id which Task should be answered
	 * @return null if given id is not in database , else: a new created Answer (pointing to the Task with the given index) which has to be filled with values
	 * @throws DatabaseExcetpion if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public MutableCalibrationAnswer answerCalibrationQuestionAt(String calibrationquestid) throws DatabaseException;
	
	/**
	 * Answers the Creative Task in this TaskConstellation at given id
	 * 
	 * @param id which Task should be answered
	 * @return null if given id is not in database , else: a new created Answer (pointing to the Task with the given index) which has to be filled with values
	 * @throws DatabaseExcetpion if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public MutableAnswer answerCreativeTaskAt(String creativeTaskId) throws DatabaseException;
	
	/**
	 * Rates the RatingTask at the given id.
	 * 
	 * @param id which Task should be rated
	 * @return a new created Rating (pointing to the Task with the given id) which has to be filled with values
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public MutableRating addRatingToRatingTaskAt(String ratingTaskId) throws DatabaseException;



}

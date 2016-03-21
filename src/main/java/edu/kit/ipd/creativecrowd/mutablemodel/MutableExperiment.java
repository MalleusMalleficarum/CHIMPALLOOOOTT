package edu.kit.ipd.creativecrowd.mutablemodel;


import edu.kit.ipd.creativecrowd.crowdplatform.WorkerId;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.ConfigModel;
import edu.kit.ipd.creativecrowd.readablemodel.Experiment;

/**
 * adds setter functionality to the Experiment class.
 * 
 * @see edu.kit.ipd.creativecrowd.readablemodel.Experiment
 * @author simon
 */
public interface MutableExperiment extends Experiment {

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.Experiment#getStats()
	 */
	public MutableStats getStats() throws DatabaseException;

	/**
	 * Adds the assignment.
	 *
	 * @return the mutable assignment
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public MutableAssignment addAssignment() throws DatabaseException;

	/**
	 * Gets the assignments.
	 *
	 * @return the mutable assignments
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public Iterable<? extends MutableAssignment> getAssignments() throws DatabaseException;

	/**
	 * Gets the assignment with the specified ID.
	 *
	 * @param assignmentid the assignmentid
	 * @return the mutable assignment
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public MutableAssignment getAssignment(String assignmentid) throws DatabaseException;

	/**
	 * adds a new CreativeTask to this Experiment.
	 *
	 * @return the mutable creative task
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public MutableCreativeTask addCreativeTask() throws DatabaseException;

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.Experiment#getCreativeTask()
	 */
	public MutableCreativeTask getCreativeTask() throws DatabaseException;

	/**
	 * Adds the rating task.
	 *
	 * @return the mutable rating task
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public MutableRatingTask addRatingTask() throws DatabaseException;

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.Experiment#getRatingTasks()
	 */
	public Iterable<? extends MutableRatingTask> getRatingTasks() throws DatabaseException;

	/**
	 * Mark as finished.
	 *
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public void markAsFinished() throws DatabaseException;

	/**
	 * Mark experiment as soft finished.
	 *
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public void markExperimentAsSoftFinished() throws DatabaseException;

	/**
	 * Sets the hit id.
	 *
	 * @param id the new hit id
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public void setHitID(String id) throws DatabaseException;
	
	/**
	 * Adds a controlquestion.
	 *
	 * @return the controlquestion
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public MutableControlQuestion addControlQuestion() throws DatabaseException;
	
	/**
	 * Gets the controlquestions for this experiment
	 *
	 * @return an iterable with the controlquestions
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public Iterable<? extends MutableControlQuestion> getControlQuestions() throws DatabaseException;
	
	/**
	 * Adds a calibrationquestion.
	 *
	 * @return the calibrationquestion
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public MutableCalibrationQuestion addCalibrationQuestion() throws DatabaseException;
	
	/**
	 * Gets the calibrationquestions for this experiment
	 *
	 * @return an iterable with the calibrationquestions
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public Iterable<? extends MutableCalibrationQuestion> getCalibrationQuestions() throws DatabaseException;
	
	/**
	 * Assigns the config
	 * 
	 * @param conf the config for this experiment
	 * @throws DatabaseException if the SQL request fails
	 * @author Thomas Friedel
	 */
	public void setConfig(ConfigModel conf) throws DatabaseException;

	/**
	 * removes the controlquest
	 * @param quest to remove
	 * @throws DatabaseException
	 */
	public void removeControlQuestion(String quest) throws DatabaseException;
	
	/**
	 * removes the calibquest
	 * @param quest to remove
	 * @throws DatabaseException
	 */
	public void removeCalibrationQuestion(String quest) throws DatabaseException;
	
	/**
	 * returns all workers who are not allowed to take part in this experiment
	 * @return the list of workers
	 * @throws DatabaseException 
	 */
	public Iterable<WorkerId> getBlockedWorkers() throws DatabaseException;
}

package edu.kit.ipd.creativecrowd.readablemodel;

import java.util.Map;

import edu.kit.ipd.creativecrowd.crowdplatform.PlatformIdentity;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;

/**
 * Provides read-only information about a config model.
 * 
 * @author Thomas Friedel, Pascal Gabriel
 *
 */
public interface ConfigModel {
	
	/**
	 * Gets the ID of this config model.
	 * @return the ID
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist)
	 */
	public String getID() throws DatabaseException;
	
	/**
	 * If this configfile is used by an experiment, this method will return it. 
	 * A configfile can only be used by one or no experiment, if multiple experiments should use the
	 * same config, than a copy of the configfile has to be used. This prevents unwanted modification
	 * (and possible corruption) of a running experiment by altering the configfile.
	 * 
	 * @return the associated experiment or {@code null} if it is not associated with one
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist)
	 */
	public String getExperimentID() throws DatabaseException;
	
	/**
	 * Gets the question of the task.
	 * @return the question
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist)
	 */
	public String getTaskQuestion() throws DatabaseException;
	
	/**
	 * Gets the question of the rating task.
	 * @return the question
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist)
	 */
	public String getRatingTaskQuestion() throws DatabaseException;
	
	/**
	 * Gets the description of the task.
	 * @return the description
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist)
	 */
	public String getTaskDescription() throws DatabaseException;
	
	/**
	 * Gets the title of the task.
	 * @return the title
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist)
	 */
	public String getTaskTitle() throws DatabaseException;
	
	/**
	 * Gets the tags of the task.
	 * @return the tags
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist)
	 */
	public String[] getTaskTags() throws DatabaseException;
	
	/**
	 * Gets the URL of the picture used for the task.
	 * @return the URL
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist)
	 */
	public String getPictureURL() throws DatabaseException;
	
	/**
	 * Gets the source URL of the task.
	 * @return the URL
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist)
	 */
	public String getTaskSourceURL() throws DatabaseException;
	
	/**
	 * Gets the calibration questions of the task.
	 * @return the questions
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist)
	 */
	public CalibrationQuestion[] getCalibQuestions() throws DatabaseException;
	
	/**
	 * Gets the control questions of the task.
	 * @return the questions
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist)
	 */
	public ControlQuestion[] getControlQuestions() throws DatabaseException;
	
	/**
	 * Gets the max count of creative tasks per assignment.
	 * @return the count
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist)
	 */
	public int getMaxCreativeTask() throws DatabaseException;
	
	/**
	 * Gets the max count of rating tasks per assignment.
	 * @return the count
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist)
	 */
	public int getMaxRatingTask() throws DatabaseException;
	
	/**
	 * Gets the budget of the task in cents.
	 * @return the budget
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist)
	 */
	public int getBudget() throws DatabaseException;
	
	/**
	 * Tells if creative tasks are sent to the chosen platform.
	 * @param platform The platform on which to check
	 * @return {@code true} if sent, {@code false} otherwise
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist)
	 */
	public boolean getSendCreativeTo(PlatformIdentity platform) throws DatabaseException;
	
	/**
	 * Tells if rating tasks are sent to the chosen platform.
	 * @param platform The platform on which to check
	 * @return {@code true} if sent, {@code false} otherwise
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist)
	 */
	public boolean getSendRatingTo(PlatformIdentity platform) throws DatabaseException;
	
	/**
	 * Gets the basic payment for the chosen platform.
	 * @param platform The platform on which to check
	 * @return the basic payment
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist)
	 */
	public int getBasicPayment(PlatformIdentity platform) throws DatabaseException;
	
	/**
	 * Gets the payment per task for the chosen platform.
	 * @param platform The platform on which to check
	 * @return the payment per task
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist)
	 */
	public int getPaymentPerTask(PlatformIdentity platform, TypeOfTask type) throws DatabaseException;
	
	/**
	 * Gets the qualifications for the chosen platform.
	 * @param platform The platform on which to check
	 * @return the qualifications
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist)
	 */
	public Iterable<String> getQualifications(PlatformIdentity platform) throws DatabaseException;
	
	/**
	 * Gets the evaluation type.
	 * @return the evaluation type
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist)
	 */
	public String getEvaluationType() throws DatabaseException;
	
	/**
	 * Gets the rating options.
	 * @return the rating options
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist)
	 */
	public Map<String, Float> getRatingOptions() throws DatabaseException;
	
	/**
	 * Gets the strategies.
	 * @return the strategies as <key, value>
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist)
	 */
	public Map<String, String> getStrategy() throws DatabaseException;
	
	/**
	 * Gets the threshold a pybossa worker needs to surpass in his average Rating.
	 * @return the threshold
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist)
	 */
	public float getAverageRatingThreshold() throws DatabaseException; //TODO Not used. DELETE?
	
	/**
	 * Gets the threshold a pybossa worker needs to surpass in his total count of tasks.
	 * @return the threshold
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist)
	 */
	public int getTotalTaskCountThreshold() throws DatabaseException; //TODO Not used. DELETE?

	/**
	 * Gets the type of the experiment.
	 * 
	 * @return the experiment type
	 * @throws DatabaseException if the SQL request fails
	 */
	public ExperimentType getExperimentType() throws DatabaseException;
	
	/**
	 * Gets for the experiment blocked worker
	 *  
	 * @return the blocked workers
	 * @throws DatabaseException if the SQL request fails
	 */
	public Iterable<Worker> getBlockedWorkers() throws DatabaseException;
}

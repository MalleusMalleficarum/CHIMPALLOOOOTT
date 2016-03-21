package edu.kit.ipd.creativecrowd.mutablemodel;

import java.util.Map;

import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
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
	 * Adds the rating option.
	 *
	 * @return the mutable rating option
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public MutableRatingOption addRatingOption() throws DatabaseException;

	/**
	 * Gets the rating options.
	 *
	 * @return the rating options
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public Iterable<? extends MutableRatingOption> getRatingOptions() throws DatabaseException;

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
	 * Sets the description.
	 *
	 * @param description the new description
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public void setDescription(String description) throws DatabaseException;

	/**
	 * Sets the budget.
	 *
	 * @param cents the new budget
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public void setBudget(int cents) throws DatabaseException;

	/**
	 * Sets the basic payment.
	 *
	 * @param cents the new basic payment
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public void setBasicPaymentHIT(int cents) throws DatabaseException;

	/**
	 * Sets the basic payment of an answer.
	 *
	 * @param cents the new creative task payment
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public void setBasicPaymentAnswer(int cents) throws DatabaseException;

	/**
	 * Sets the rating task payment.
	 *
	 * @param cents the new rating task payment
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public void setBasicPaymentRating(int cents) throws DatabaseException;

	/**
	 * Sets the HIT title.
	 *
	 * @param title the new HIT title
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public void setHITTitle(String title) throws DatabaseException;

	/**
	 * Sets the HIT description.
	 *
	 * @param description the new HIT description
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public void setHITDescription(String description) throws DatabaseException;

	/**
	 * Sets the tags.
	 *
	 * @param tags the new tags
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public void setTags(Iterable<String> tags) throws DatabaseException;

	/**
	 * Sets the qualifications.
	 *
	 * @param qualifications the new qualifications
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public void setQualifications(Iterable<String> qualifications) throws DatabaseException;

	/**
	 * Sets the bonus payment.
	 *
	 * @param cents the new bonus payment
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public void setBonusPayment(int cents) throws DatabaseException;

	/**
	 * Sets the hit id.
	 *
	 * @param id the new hit id
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public void setHitID(String id) throws DatabaseException;

	/**
	 * Sets the strategy params.
	 *
	 * @param params the params
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public void setStrategyParams(Map<String, String> params) throws DatabaseException;

	/**
	 * Sets the maximum number of ratings per assignment.
	 *
	 * @param the maximum number of ratings per assignment
	 * @throws DatabaseException
	 */
	public void setMaxNumberOfRatingsPerAssignment(int maxRatings) throws DatabaseException;

	/**
	 * Sets the maximum number of answers per assignment.
	 *
	 * @param the maximum number of answers per assignment
	 * @throws DatabaseException
	 */
	public void setMaxNumberOfAnswersPerAssignment(int maxAnswers) throws DatabaseException;

	/**
	 * Sets the name of the class that is used by the view to display the RatingOptions
	 * 
	 * @return a String in the form "edu.kit.ipd.creativecrowd.view.RatingView"
	 * @throws DatabaseException
	 */
	public void setRatingTaskViewClass(String ratingTaskViewClassName) throws DatabaseException;

}

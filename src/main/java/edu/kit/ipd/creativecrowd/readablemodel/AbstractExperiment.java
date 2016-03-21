package edu.kit.ipd.creativecrowd.readablemodel;

import java.util.Map;

import edu.kit.ipd.creativecrowd.crowdplatform.PlatformIdentity;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;

/**
 * provides read-only information about the data that is stored from the
 * configuration file.
 *
 * @author simon
 */
public interface AbstractExperiment {

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getID();

	/**
	 * Gets the description.
	 *
	 * @return the description
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public String getDescription() throws DatabaseException;

	/**
	 * Gets the tags.
	 *
	 * @return the tags
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public Iterable<String> getTags() throws DatabaseException;

	/**
	 * Gets the qualifications.
	 *
	 * @return the qualifications
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public Iterable<String> getQualifications(PlatformIdentity platform) throws DatabaseException;

	/**
	 * Gets the strategy params.
	 *
	 * @return the strategy params
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public Map<String, String> getStrategyParams() throws DatabaseException;

	/**
	 * Gets the budget cents.
	 *
	 * @return the budget cents
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public int getBudgetCents() throws DatabaseException;

	/**
	 * Gets the basic payment hit cents.
	 *
	 * @return the basic payment hit cents
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public int getBasicPaymentHITCents(PlatformIdentity platform) throws DatabaseException;

	/**
	 * Gets the basic payment answer cents.
	 *
	 * @return the basic payment answer cents
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public int getBasicPaymentAnswerCents(PlatformIdentity platform) throws DatabaseException;

	/**
	 * Gets the basic payment rating cents.
	 *
	 * @return the basic payment rating cents
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public int getBasicPaymentRatingCents(PlatformIdentity platform) throws DatabaseException;

	/**
	 * Gets the HIT title.
	 *
	 * @return the HIT title
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public String getHITTitle() throws DatabaseException;

	/**
	 * Gets the HIT description.
	 *
	 * @return the HIT description
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public String getHITDescription() throws DatabaseException;

	/**
	 * Gets the maximum number of answers per assignment.
	 *
	 * @return the maximum number of answers per assignment
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public int getMaxNumberOfAnswersPerAssignment() throws DatabaseException;

	/**
	 * Gets the maximum number of ratings per assignment.
	 *
	 * @return the maximum number of ratings per assignment
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public int getMaxNumberOfRatingsPerAssignment() throws DatabaseException;

	/**
	 * Gets the name of the class that is used by the view to display the RatingOptions
	 * 
	 * @return a String in the form "edu.kit.ipd.creativecrowd.view.RatingView"
	 * @throws DatabaseException
	 */
	public String getRatingTaskViewClass() throws DatabaseException;
}

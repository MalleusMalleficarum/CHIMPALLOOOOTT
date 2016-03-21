package edu.kit.ipd.creativecrowd.readablemodel;

import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;

// TODO: Auto-generated Javadoc
/**
 * get statistics about an experiment.
 *
 * @author simon
 */
public interface Stats {

	/**
	 * Gets the cancelled count.
	 *
	 * @return the cancelled count
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public int getCancelledCount() throws DatabaseException;

	/**
	 * Gets the preview clicks count.
	 *
	 * @return the preview clicks count
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public int getPreviewClicksCount() throws DatabaseException;

	/**
	 * Gets the accepted hit count.
	 *
	 * @return the accepted hit count
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public int getAcceptedHitCount() throws DatabaseException;

	/**
	 * Gets the submission count.
	 *
	 * @return the submission count
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public int getSubmissionCount() throws DatabaseException;

	/**
	 * Gets the rating count.
	 *
	 * @return the rating count
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public int getRatingCount() throws DatabaseException;

	/**
	 * Gets the timestamp begin.
	 *
	 * @return the timestamp begin
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public String getTimestampBegin() throws DatabaseException;
}

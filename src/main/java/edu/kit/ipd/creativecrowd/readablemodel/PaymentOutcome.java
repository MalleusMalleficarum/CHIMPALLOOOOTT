package edu.kit.ipd.creativecrowd.readablemodel;

import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;

// TODO: Auto-generated Javadoc
/**
 * information about how much money was paid to a worker.
 *
 * @author simon
 */
public interface PaymentOutcome {

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getID();

	/**
	 * Basic payment was paid.
	 *
	 * @return true, if successful
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public boolean isApproved() throws DatabaseException;

	/**
	 * Gets the bonus paid cents.
	 *
	 * @return the bonus paid cents
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public int getBonusPaidCents() throws DatabaseException;
}

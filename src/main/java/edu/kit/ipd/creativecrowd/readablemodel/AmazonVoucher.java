package edu.kit.ipd.creativecrowd.readablemodel;

import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;

/**
 * Represents a readable Amazon Voucher.
 * @author Pascal Gabriel
 *
 */
public interface AmazonVoucher extends Comparable<AmazonVoucher>{
	/**
	 * Gets the id of the voucher.
	 * @return the id
	 */
	public String getID();
	
	/**
	 * Gets the voucher code.
	 * @return the code
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public String getCode() throws DatabaseException;
	
	/**
	 * Gets the value of the voucher.
	 * @return the value
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public int getValue() throws DatabaseException;
}

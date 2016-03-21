package edu.kit.ipd.creativecrowd.mutablemodel;

import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.AmazonVoucher;

/**
 * Represents a mutable Amazon Voucher
 * @author Pascal Gabriel
 *
 */
public interface MutableAmazonVoucher extends AmazonVoucher {
	/**
	 * Sets the code of the Voucher
	 * @param code the code
	 * @return {@code true} if successful, {@code false} if input was wrong
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public boolean setCode(String code) throws DatabaseException;
	
	/**
	 * Sets the value of the voucher.
	 * @param value the value
	 * @return {@code true} if successful, {@code false} if input was wrong
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public boolean setValue(int value) throws DatabaseException;
}

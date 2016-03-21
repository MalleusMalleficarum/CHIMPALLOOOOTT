package edu.kit.ipd.creativecrowd.mutablemodel;

import edu.kit.ipd.creativecrowd.readablemodel.AmazonVoucherRepo;

import java.util.Map;

import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.persistentmodel.IDNotFoundException;

/**
 * Represents a mutable Repo for Amazon Vouchers.
 * @author Pascal Gabriel
 *
 */
public interface MutableAmazonVoucherRepo extends AmazonVoucherRepo {
	
	/**
	 * Adds a Voucher specified by the params to the database.
	 * @param code the voucher code
	 * @param value	the value in cents
	 * @return {@code true} if successful, {@code false} if input was wrong
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public boolean addVoucher(String code, int value) throws DatabaseException;
	
	/**
	 * Adds a multiple Vouchers to the database. 
	 * @param vouchers Map<Voucher code, value in cents>
	 * @return {@code true} if successful, {@code false} if input was wrong
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public boolean addVouchers(Map<String, Integer> vouchers) throws DatabaseException;
	
	/**
	 * Returns the specified voucher and deletes it from the database.
	 * @param id id of the voucher
	 * @return the voucher
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 * @throws IDNotFoundException if the id could not be matched to a voucher.
	 */
	public Map<String, Integer> useVoucher(String id) throws DatabaseException, IDNotFoundException; //removes the voucher from the database, returns its values

	/**
	 * Adds the vouchers represented by this string to the database.
	 * @param input a subsequent list of 'vouchercode:vouchervalue(in cent);'
	 * @return {@code true} if all were added successfully, {@code false} if the format for at least one was wrong
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public boolean addVouchersAsString(String input) throws DatabaseException;
}

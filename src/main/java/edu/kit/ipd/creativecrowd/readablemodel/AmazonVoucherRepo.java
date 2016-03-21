package edu.kit.ipd.creativecrowd.readablemodel;

import java.util.List;

import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.persistentmodel.IDNotFoundException;

/**
 * Represents a readable Repo for AmazonVouchers.
 * @author Pascal Gabriel
 *
 */
public interface AmazonVoucherRepo {
	/**
	 * Gets all Vouchers stored in the database.
	 * @return the vouchers
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public List<AmazonVoucher> getAllVouchers() throws DatabaseException;
	
	/**
	 * Gets the first voucher worth the specified value.
	 * @param value the value
	 * @return the voucher
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public AmazonVoucher getVoucherWorth(int value) throws DatabaseException;
	
	/**
	 * Returns a list of AmazonVouchers which total sum of values is close to {@code value}. For optimal result
	 * all vouchers in the database should be structured like this x, 2x, .., 2*n*x, with n being a Natural Number and x 
	 * the smallest value.
	 * @param value the value to get close to
	 * @return the list of vouchers matching the criteria
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public List<AmazonVoucher> getVouchersWorthCloseTo(int value) throws DatabaseException;
	
	/**
	 * Returns the voucher with the given id.
	 * @param id the id
	 * @return the voucher
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 * @throws IDNotFoundException if the id could not be matched to a voucher.
	 */
	public AmazonVoucher getVoucher(String id) throws DatabaseException, IDNotFoundException;
}

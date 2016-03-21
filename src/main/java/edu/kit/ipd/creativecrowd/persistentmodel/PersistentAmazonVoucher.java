package edu.kit.ipd.creativecrowd.persistentmodel;

import java.sql.SQLException;

import edu.kit.ipd.chimpalot.util.Logger;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAmazonVoucher;
import edu.kit.ipd.creativecrowd.readablemodel.AmazonVoucher;
import edu.kit.ipd.creativecrowd.database.DatabaseConnection;
import edu.kit.ipd.creativecrowd.database.Value;

/**
 * Implements a AmazonVoucher.
 * @author Pascal Gabriel
 *
 */
public class PersistentAmazonVoucher implements MutableAmazonVoucher {
	
	private String id;
	private DatabaseConnection connection;
	
	/**
	 * Sets the internal id and connection.
	 * @param ID the id
	 * @param dbConnection the connection
	 */
	public PersistentAmazonVoucher(String ID, DatabaseConnection dbConnection) {
		this.id = ID;
		this.connection = dbConnection;
	}
	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.AmazonVoucher#getID()
	 */
	@Override
	public String getID() {
		return this.id;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.AmazonVoucher#getCode()
	 */
	@Override
	public String getCode() throws DatabaseException{
		String result;
		try {
			String sql = connection.formatString("SELECT code FROM amazonvouchers WHERE id = {?};", Value.fromString(id));
			Iterable<Iterable<Value>> values = connection.query(sql);
			result = values.iterator().next().iterator().next().asString();
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.AmazonVoucher#getValue()
	 */
	@Override
	public int getValue() throws DatabaseException{
		int result;
		try {
			String sql = connection.formatString("SELECT value FROM amazonvouchers WHERE id = {?};", Value.fromString(id));
			Iterable<Iterable<Value>> values = connection.query(sql);
			result = values.iterator().next().iterator().next().asInt();
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableAmazonVoucher#setCode(java.lang.String)
	 */
	@Override
	public boolean setCode(String code) throws DatabaseException {
		if (code == null) {
			return false;
		}
		try {
			String sql = connection.formatString("UPDATE amazonvouchers SET code = {?} WHERE id = {?};", Value.fromString(code), Value.fromString(id));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return true; 
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableAmazonVoucher#setValue(int)
	 */
	@Override
	public boolean setValue(int value) throws DatabaseException {
		if (value <= 0) {
			return false;
		}
		try {
			String sql = connection.formatString("UPDATE amazonvouchers SET value = {?} WHERE id = {?};", Value.fromInt(value), Value.fromString(id));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return true;
	}
	/*
	 * Sorts two Vouchers, giving the higher value precedence
	 * this makes it a lot easier to sort them.
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(AmazonVoucher arg0) {
		try {
			return Integer.compare(arg0.getValue(), this.getValue());
		} catch (DatabaseException e) {
			Logger.logException(e.getMessage());
		}
		return 0;
	}
	
	

}

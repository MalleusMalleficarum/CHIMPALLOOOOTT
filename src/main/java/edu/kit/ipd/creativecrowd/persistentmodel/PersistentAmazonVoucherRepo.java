package edu.kit.ipd.creativecrowd.persistentmodel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.kit.ipd.chimpalot.util.GlobalApplicationConfig;
import edu.kit.ipd.chimpalot.util.Logger;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAmazonVoucherRepo;
import edu.kit.ipd.creativecrowd.readablemodel.AmazonVoucher;
import edu.kit.ipd.creativecrowd.database.DatabaseConnection;
import edu.kit.ipd.creativecrowd.database.SQLiteDatabaseConnection;
import edu.kit.ipd.creativecrowd.database.Value;

/**
 * Implements the methods for storage control of the Amazon Vouchers.
 * @author Pascal Gabriel
 *
 */
public class PersistentAmazonVoucherRepo implements MutableAmazonVoucherRepo {

	/*The connection to the database*/
	private DatabaseConnection connection;
	
	/**
	 * Sets up the connection and the creates the databasestructure if needed.
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public PersistentAmazonVoucherRepo() throws DatabaseException{
		connection = SQLiteDatabaseConnection.getInstance();

		// set Up connection
		try {
			connection.setUpDatabaseConnection(GlobalApplicationConfig
					.getDBPath());
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}

		// build the table
		try {
			//AmazonVouchers
			String sql = "CREATE TABLE IF NOT EXISTS amazonvouchers"
					+ "(id TEXT PRIMARY KEY NOT NULL,"
					+ " code TEXT,"
					+ " value INT);";
			connection.query(sql);
				
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.AmazonVoucherRepo#getAllVouchers()
	 */
	@Override
	public List<AmazonVoucher> getAllVouchers() throws DatabaseException{
		List<AmazonVoucher> ret = new LinkedList<AmazonVoucher>();
		try {
			String sql = "SELECT id FROM amazonvouchers";
			Iterable<Iterable<Value>> voucherIds = connection.query(sql);
			Iterator<Iterable<Value>> namesIterator = voucherIds.iterator();
		
			while (namesIterator.hasNext()) {
				String voucherID = namesIterator.next().iterator().next().asString();
				AmazonVoucher voucher = new PersistentAmazonVoucher(voucherID, this.connection);
				ret.add(voucher);
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.AmazonVoucherRepo#getVoucherWorth(int)
	 */
	@Override
	public AmazonVoucher getVoucherWorth(int value) throws DatabaseException{
		try {
			String sql = connection.formatString("SELECT id FROM amazonvouchers WHERE value = {?};",Value.fromInt(value));
			Iterable<Iterable<Value>> voucherIds = connection.query(sql);
			Iterator<Iterable<Value>> namesIterator = voucherIds.iterator();
		
			if (namesIterator.hasNext()) {
				String voucherID = namesIterator.next().iterator().next().asString();
				AmazonVoucher voucher = new PersistentAmazonVoucher(voucherID, this.connection);
				return voucher;
			} else {
				throw new DatabaseException("No voucher worth " + value + "has been found.");
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.AmazonVoucherRepo#getVouchersWorthCloseTo(int)
	 */
	@Override
	public List<AmazonVoucher> getVouchersWorthCloseTo(int value) throws DatabaseException{
		//ordered list from small to greater
		List<AmazonVoucher> allVouchers = this.orderByValue(this.getAllVouchers());
		List<AmazonVoucher> ret = new LinkedList<AmazonVoucher>();
		int remainingValue = value;
		for(AmazonVoucher v : allVouchers) {
			if(remainingValue == 0) {
				break;
			}
			if(v.getValue() <= remainingValue) {
				ret.add(v);
				remainingValue -= v.getValue();
			}
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableAmazonVoucherRepo#addVoucher(java.lang.String, int)
	 */
	@Override
	public boolean addVoucher(String code, int value) throws DatabaseException{
		if (code == null || value <= 0) {
			return false;
		}
		try {
			String sql = null;
			String voucherId = connection.generateID("amazonvouchers");
			List<Value> args = new ArrayList<Value>();
			args.add(Value.fromString(voucherId));
			args.add(Value.fromString(code));
			args.add(Value.fromInt(value));
			sql = connection.formatString("INSERT INTO amazonvouchers (id,code,value) VALUES ({?},{?},{?});", args);
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return true;
	}

	@Override
	public boolean addVouchers(Map<String, Integer> vouchers) throws DatabaseException{
		if (vouchers == null) {
			return false;
		}
		for (Map.Entry<String, Integer> entry : vouchers.entrySet()) {
			if(entry.getKey() == null || entry.getValue() == null) {
				Logger.error("NULL is not a valid code, nor is it a number");
				break;
			}
			this.addVoucher(entry.getKey(), entry.getValue());
		}
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableAmazonVoucherRepo#addVouchersAsString(java.lang.String)
	 */
	@Override
	public boolean addVouchersAsString(String input) throws DatabaseException{
		if (input == null) {
			return false;
		}
		
		input = input.trim(); //Get rid of all whitespace.
		Pattern p = Pattern.compile("((\\w)+:(\\d)+;)*(\\w)+:(\\d)+");
		Matcher m = p.matcher(input);
		if(m.matches()) {
			input = input.replaceAll(":", ";");
			Logger.debug("input " + input);
			String[] results = input.split(";");
			
			for(int i = 0; i < results.length; i = i + 2) {
				Logger.debug(results[i] + " " + results[i + 1] + " i: " +i);
				this.addVoucher(results[i], Integer.parseInt(results[i + 1]));
			}
			return true;
		}
		Logger.debug("String malformed: " + input);
		return false;
		
			

	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableAmazonVoucherRepo#useVoucher(java.lang.String)
	 */
	@Override
	public Map<String, Integer> useVoucher(String id) throws DatabaseException, IDNotFoundException{
		//get the code and value
		AmazonVoucher voucher = this.getVoucher(id);
		Map<String, Integer> ret = new HashMap<String, Integer>();
		ret.put(voucher.getCode(), voucher.getValue());
		//delete the voucher
		try {
			String sql = connection.formatString("DELETE FROM amazonvouchers WHERE id = {?};", Value.fromString(id));
			connection.query(sql);
		} catch(SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.AmazonVoucherRepo#getVoucher(java.lang.String)
	 */
	@Override
	public AmazonVoucher getVoucher(String id) throws DatabaseException, IDNotFoundException{
		AmazonVoucher ret = null;
		try {
			String sql = connection.formatString("SELECT * FROM amazonvouchers WHERE id = {?};", Value.fromString(id));
			Iterable<Iterable<Value>> persistentAmazonVouchers = connection.query(sql);
			if (persistentAmazonVouchers.iterator().hasNext()) {
				ret = new PersistentAmazonVoucher(id, this.connection);
			} else {
				throw new IDNotFoundException("requested voucher does not exist");
			}

		} catch (SQLException e ) {
			throw new DatabaseException(e.getMessage());
		}
		return ret;
	}
	
	/*
	 * Orders the AmazonVouchers in the List by Value, from small to greater
	 * ROBIN: now it orders from greater to small @see PersistentAmazonVoucher
	 */
	private List<AmazonVoucher> orderByValue(List<AmazonVoucher> vouchers) throws DatabaseException{
		
		Collections.sort(vouchers);
		return vouchers;

	}

}

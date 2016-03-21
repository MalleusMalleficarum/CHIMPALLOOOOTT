package edu.kit.ipd.creativecrowd.database;

import java.sql.SQLException;
import java.sql.SQLTimeoutException;

public interface DatabaseConnection {
	public Iterable<Iterable<Value>> query(String sql) throws SQLException, SQLTimeoutException;

	public void setUpDatabaseConnection(String url) throws SQLException, SQLTimeoutException,
			ClassNotFoundException;

	/**
	 * generates a unique ID for given Table
	 * 
	 * @param tableName for which an id has to be generated
	 * @return a unique id
	 */
	public String generateID(String tableName);

	public String formatString(String expression, Iterable<Value> args) throws SQLException;

	public String formatString(String expression, Value arg) throws SQLException;

	public String formatString(String expression, Value arg1, Value arg2) throws SQLException;
}

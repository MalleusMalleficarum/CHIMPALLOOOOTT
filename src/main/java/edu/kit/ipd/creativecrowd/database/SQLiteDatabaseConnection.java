package edu.kit.ipd.creativecrowd.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringEscapeUtils;

import edu.kit.ipd.chimpalot.util.Logger;

public class SQLiteDatabaseConnection implements DatabaseConnection {

	Connection connection;
	private static SQLiteDatabaseConnection instance;
	
	private SQLiteDatabaseConnection() {
		connection = null;
	}

	public static synchronized SQLiteDatabaseConnection getInstance() {
		if (instance == null) {
			instance = new SQLiteDatabaseConnection();
		}
		return instance;
	}
	
	public void setUpDatabaseConnection(String url) throws SQLException,
	ClassNotFoundException {
		if (connection == null) {
			Class.forName("org.sqlite.JDBC");
			// example url "jdbc:sqlite:CreativeCrowd.db"
			connection = DriverManager.getConnection(url);
			connection.setAutoCommit(false);
		}
	}

	public synchronized Iterable<Iterable<Value>> query(String sql) throws SQLException {/*-?|Simon|simon|c2|?*/
		Logger.debug("DB: "+sql);
		List<Iterable<Value>> ret = new LinkedList<Iterable<Value>>();
		// perform sql-query
		Statement stmt = connection.createStatement();

		if (sql.startsWith("SELECT")) {
			ResultSet res = stmt.executeQuery(sql);
			// parse return-object into required type
			ResultSetMetaData meta = res.getMetaData();
			int tableWidth = meta.getColumnCount();
			// for each row
			while (res.next()) {
				LinkedList<Value> row = new LinkedList<Value>();
				// for each column
				for (int i = 1; i <= tableWidth; i++) {
					Value val = null;
					switch(meta.getColumnType(i)) {
					case Types.INTEGER:
						val = Value.fromInt(res.getInt(i));
						break;
					case Types.TIMESTAMP:
						val = Value.fromLong(res.getTimestamp(i).getTime());
						break;
					case Types.REAL:
						val = Value.fromFloat(res.getFloat(i));
						break;
					case Types.FLOAT:
						val = Value.fromFloat(res.getFloat(i));
						break;
					default:
						val = Value.fromString(res.getString(i));

						break;
					}
					row.add(val);
				}
				ret.add(row);

			}
		} else {
			stmt.execute(sql);
		}

		connection.commit();
		stmt.close();
		return ret;
	}

	@Override
	public String formatString(String expression, Value arg) throws SQLException {
		String ret = expression;

		String repl = "";
		if (arg.asFloat() != null)
			repl = arg.asFloat().toString();
		else if (arg.asLong() != null)
			repl = arg.asLong().toString();
		else if (arg.asString() != null) {
				String sec = this.escape(arg.asString());
				repl = "'" + sec + "'";
			}
		else {
			throw new SQLException("\"" + arg.toString() + "\"" + "formatString: one of the args is null");
		}
		ret = ret.replaceFirst("\\{\\?\\}", java.util.regex.Matcher.quoteReplacement(repl) );
		return ret;

	}

	@Override
	public String formatString(String expression, Value arg1, Value arg2) throws SQLException {
		String ret = expression;
		ret = formatString(ret, arg1);
		ret = formatString(ret, arg2);
		return ret;
	}

	@Override
	public String formatString(String expression, Iterable<Value> args) throws SQLException {
		String ret = expression;

		for (Value val : args) {
			ret = formatString(ret, val);

		}
		return ret;

	}

	@Override
	public String generateID(String tableName) {
		return tableName + "_" + UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	private String escape(String str) {
		String ret = str;
		ret = StringEscapeUtils.escapeSql(ret);
		return ret;
	}

}

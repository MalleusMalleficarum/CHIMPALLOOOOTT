package edu.kit.ipd.creativecrowd.persistentmodel;

import java.sql.SQLException;

import edu.kit.ipd.creativecrowd.database.DatabaseConnection;
import edu.kit.ipd.creativecrowd.database.Value;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRatingOption;

/**
 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableRatingOption
 * @see edu.kit.ipd.creativecrowd.readablemodel.RatingOption
 * @author Philipp + Alexis
 */
class PersistentRatingOption implements MutableRatingOption {

	/** The unique id of a persistent rating option. */
	private String id;

	/** The connection to the database. */
	private DatabaseConnection connection;

	/**
	 * Instantiates a new persistent rating option.
	 *
	 * @param id, a unique id of a persistent rating option
	 * @param connection, a connection to the database
	 */
	PersistentRatingOption(String id, DatabaseConnection connection) {
		this.connection = connection;
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.RatingOption#getValue()
	 */
	@Override
	public float getValue() throws DatabaseException {
		float result = 0;
		try {
			String sql = connection.formatString("SELECT value FROM ratingoption WHERE id = {?};", Value.fromString(id));
			Iterable<Iterable<Value>> values = connection.query(sql);
			if (values.iterator().hasNext()) {
				result = values.iterator().next().iterator().next().asFloat();
			}
			else {
				throw new DatabaseException("wanted Experiment does not exist");
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.RatingOption#getText()
	 */
	@Override
	public String getText() throws DatabaseException {
		String result = null;
		try {
			String sql = connection.formatString("SELECT text FROM ratingoption WHERE id = {?};", Value.fromString(id));
			Iterable<Iterable<Value>> texts = connection.query(sql);
			if (texts.iterator().hasNext()) {
				result = texts.iterator().next().iterator().next().asString();
			}
			else {
				throw new DatabaseException("wanted Experiment does not exist");
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableRatingOption#setValue(float)
	 */
	@Override
	public void setValue(float rating) throws DatabaseException {
		try {
			String sql = connection.formatString("UPDATE ratingoption SET value = {?} WHERE id = {?};", Value.fromFloat(rating), Value.fromString(id));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.RatingOption#getID()
	 */
	@Override
	public String getID() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		return this.id.equals(obj.toString());
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableRatingOption#setText(java.lang.String)
	 */
	@Override
	public void setText(String name) throws DatabaseException {
		try {
			String sql = connection.formatString("UPDATE ratingoption SET text = {?} WHERE id = {?};", Value.fromString(name), Value.fromString(id));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}
}

package edu.kit.ipd.creativecrowd.persistentmodel;

import java.sql.SQLException;

import edu.kit.ipd.creativecrowd.database.DatabaseConnection;
import edu.kit.ipd.creativecrowd.database.Value;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRating;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRatingOption;

/**
 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableRating
 * @see edu.kit.ipd.creativecrowd.readablemodel.Rating
 * @author Philipp + Alexis
 */
class PersistentRating implements MutableRating {

	/** The unique id of a persistent rating. */
	private String id;

	/** The connection to the database. */
	private DatabaseConnection connection;

	/**
	 * Instantiates a new persistent rating.
	 *
	 * @param id, a unique id of a persistent rating
	 * @param connection, a connection to the database
	 */
	PersistentRating(String id, DatabaseConnection connection) {
		this.connection = connection;
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableRating#getSelectedRatingOption()
	 */
	@Override
	public MutableRatingOption getSelectedRatingOption() throws DatabaseException {
		MutableRatingOption option = null;
		try {
			String sql = connection.formatString("SELECT ratingoptionid FROM rating WHERE id = {?};", Value.fromString(id));
			Iterable<Iterable<Value>> ratingoptions = connection.query(sql);
			if (ratingoptions.iterator().hasNext()) {
				option = new PersistentRatingOption(ratingoptions.iterator().next().iterator().next().asString(), this.connection);
			}
			else {
				throw new DatabaseException("wanted Experiment does not exist");
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return option;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.Rating#getText()
	 */
	@Override
	public String getText() throws DatabaseException {
		String result = null;
		try {
			String sql = connection.formatString("SELECT text FROM rating WHERE id = {?};", Value.fromString(id));
			Iterable<Iterable<Value>> ratingoptions = connection.query(sql);
			if (ratingoptions.iterator().hasNext()) {
				result = ratingoptions.iterator().next().iterator().next().asString();
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
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableRating#setText(java.lang.String)
	 */
	@Override
	public void setText(String text) throws DatabaseException {
		try {
			String sql = connection.formatString("UPDATE rating SET text = {?} WHERE id = {?};", Value.fromString(text), Value.fromString(id));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableRating#setRatingOption(edu.kit.ipd.creativecrowd.mutablemodel.MutableRatingOption)
	 */
	@Override
	public void setRatingOption(MutableRatingOption option) throws DatabaseException {
		try {
			String sql = connection.formatString("UPDATE rating SET ratingoptionid = {?} where id = {?};", Value.fromString(option.getID()), Value.fromString(id));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.Rating#getID()
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
	 * @see edu.kit.ipd.creativecrowd.readablemodel.MutableRating#getAnswer()
	 */
	/*-{?}|Test Repo-Review|MainUser|c1|{?}*/
	@Override
	public MutableAnswer getAnswer() throws DatabaseException {
		MutableAnswer ret = null;
		try {
			String sql = connection.formatString("SELECT answerid FROM rating WHERE id = {?};", Value.fromString(this.id));
			String answerid = connection.query(sql).iterator().next().iterator().next().asString();
			ret = new PersistentAnswer(answerid, connection);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ret;

	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.Rating#setFinalQualityIndex()
	 */
	@Override
	public void setFinalQualityIndex(float idx) throws DatabaseException {
		try {
			String sql = connection.formatString("UPDATE rating SET final_quality_index = {?} WHERE id = {?};", Value.fromFloat(idx), Value.fromString(this.id));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	@Override
	public float getFinalQualityIndex() throws DatabaseException {
		float ret = 0;
		try {
			String sql = connection.formatString("SELECT final_quality_index FROM rating WHERE id = {?};", Value.fromString(this.id));
			ret = connection.query(sql).iterator().next().iterator().next().asFloat();
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ret;
	}
}

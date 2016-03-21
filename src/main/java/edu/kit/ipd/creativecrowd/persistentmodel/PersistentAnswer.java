package edu.kit.ipd.creativecrowd.persistentmodel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.kit.ipd.creativecrowd.database.DatabaseConnection;
import edu.kit.ipd.creativecrowd.database.Value;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableCreativeTask;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRating;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableWorker;

/**
 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableAnswer
 * @see edu.kit.ipd.creativecrowd.readablemodel.Answer
 * @author Philipp & Alexis
 */
public class PersistentAnswer implements MutableAnswer {

	/** The connection to the database. */
	private DatabaseConnection connection;

	/** The unique id of a persistent answer. */
	private String id;

	/**
	 * Instantiates a new persistent answer.
	 *
	 * @param id, a unique id of a persistent answer
	 * @param connection, a connection to the database
	 */
	public PersistentAnswer(String id, DatabaseConnection connection) {
		this.id = id;
		this.connection = connection;
	}

	/*-?|Test Repo-Review|Philipp|c4|?*/
	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.Answer#getTimestamp()
	 */
	@Override
	public String getTimestampBegin() throws DatabaseException {
		String ret = "";
		try {
			/*-?|Test Repo-Review|Philipp|c6|?*/
			String sql = connection.formatString("SELECT sqltime FROM answer WHERE id = {?};", Value.fromString(this.id));
			ret = connection.query(sql).iterator().next().iterator().next().asString();
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.Answer#getText()
	 */
	@Override
	public String getText() throws DatabaseException {
		String ret = "";
		try {
			String sql = connection.formatString("SELECT text FROM answer WHERE id = {?};", Value.fromString(this.id));
			ret = connection.query(sql).iterator().next().iterator().next().asString();

		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableAnswer#setText(java.lang.String)
	 */
	@Override
	public void setText(String answer) throws DatabaseException {
		try {
			String sql = connection.formatString("UPDATE answer SET text = {?} WHERE id = {?};", Value.fromString(answer), Value.fromString(this.id));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableAnswer#addRating(edu.kit.ipd.creativecrowd.mutablemodel.MutableRating)
	 */
	@Override
	public void addRating(MutableRating rating) throws DatabaseException {
		try {
			String sql = connection.formatString("UPDATE rating SET answerid = {?} WHERE id = {?};", Value.fromString(this.id), Value.fromString(rating.getID()));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableAnswer#getRatings()
	 */
	@Override
	public Iterable<? extends MutableRating> getRatings() throws DatabaseException {
		List<MutableRating> ret = new ArrayList<MutableRating>();
		try {
			String sql = connection.formatString("SELECT id FROM rating WHERE answerid = {?};", Value.fromString(this.id));
			for (Iterable<Value> rows : connection.query(sql)) {
				ret.add(new PersistentRating(rows.iterator().next().asString(), this.connection));
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableAnswer#markAsRated()
	 */
	@Override
	public void markAsRated() throws DatabaseException {
		try {
			String sql = connection.formatString("UPDATE answer SET marked_as_rated = 1 WHERE id = {?};", Value.fromString(this.id));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.Answer#getID()
	 */
	@Override
	public String getID() {
		return this.id;
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
	 * @see edu.kit.ipd.creativecrowd.readablemodel.Answer#isSufficientlyRated()
	 */
	@Override
	public boolean isSufficientlyRated() throws DatabaseException {
		boolean ret = false;
		try {
			String sql = connection.formatString("SELECT marked_as_rated FROM answer WHERE id = {?}", Value.fromString(this.id));
			ret = (connection.query(sql).iterator().next().iterator().next().asInt() == 1);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.Answer#isSubmitted()
	 */
	@Override
	public boolean isSubmitted() throws DatabaseException {
		boolean ret = false;
		try {
			// lookup the containscreativeid on which this answer points at
			String sql = connection.formatString(
					"SELECT containscreativeid FROM answer WHERE id = {?};",
					Value.fromString(id));
			String containscreativeid = connection.query(sql).iterator().next()
					.iterator().next().asString();
			// lookup for the taskconstellationid in the containscreative-table:
			sql = connection
					.formatString(
							"SELECT taskconstellationid FROM containscreative WHERE id = {?};",
							Value.fromString(containscreativeid));
			String tcId = connection.query(sql).iterator().next().iterator()
					.next().asString();
			// get the fitting assignment for the found taskconstellationid
			sql = connection.formatString(
					"SELECT assignmentid FROM taskconstellation WHERE id = {?};",
					Value.fromString(tcId));
			String assId = connection.query(sql).iterator().next().iterator()
					.next().asString();
			// is the found assignment submitted? return
			sql = connection.formatString(
					"SELECT is_submitted FROM assignment WHERE id = {?};",
					Value.fromString(assId));
			ret = (connection.query(sql).iterator().next().iterator().next()
					.asInt() == 1);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ret;
	}

	@Override
	public void setFinalQualityIndex(float idx) throws DatabaseException {
		try {
			String sql = connection.formatString("UPDATE answer SET final_quality_index = {?} WHERE id = {?};", Value.fromFloat(idx), Value.fromString(this.id));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	@Override
	public float getFinalQualityIndex() throws DatabaseException {
		float ret = 0;
		try {
			String sql = connection.formatString("SELECT final_quality_index FROM answer WHERE id = {?};", Value.fromString(this.id));
			ret = connection.query(sql).iterator().next().iterator().next().asFloat();
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ret;
	}

	@Override
	public MutableCreativeTask getCreativeTask() throws DatabaseException {
		MutableCreativeTask ret = null;
		try {
			// lookup the constainscreativeid on which this answer points at
			String sql = connection.formatString(
					"SELECT containscreativeid FROM answer WHERE id = {?};",
					Value.fromString(id));
			String containscreativeid = connection.query(sql).iterator().next()
					.iterator().next().asString();
			// lookup for the creativetaskid in the containscreative-table:
			sql = connection
					.formatString(
							"SELECT creativetaskid FROM containscreative WHERE id = {?};",
							Value.fromString(containscreativeid));
			String ccid = connection.query(sql).iterator().next().iterator()
					.next().asString();
			ret = new PersistentCreativeTask(ccid, connection);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}

		return ret;
	}

	@Override
	public MutableWorker getWorker() throws DatabaseException {
		MutableWorker ret = null;
		try {
			String sql = connection.formatString(
					"SELECT workerid FROM answer WHERE id = {?};",
					Value.fromString(id));
			String ccid = connection.query(sql).iterator().next().iterator()
					.next().asString();
			ret = new PersistentWorker(connection, ccid);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}

		return ret;
	}

	@Override
	public void setWorkerID(String workerID) throws DatabaseException {
		try {
			String sql = connection.formatString(
					"UPDATE answer SET workerid = {?} WHERE id = {?};",Value.fromString(workerID),
					Value.fromString(id));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		
	}
	
	@Override
	public String getMturkAssignmentId() throws DatabaseException {
		try {
			// lookup the containscreativeid on which this answer points at
			String sql = connection.formatString(
					"SELECT containscreativeid FROM answer WHERE id = {?};",
					Value.fromString(id));
			String containscreativeid = connection.query(sql).iterator().next()
					.iterator().next().asString();
			// lookup for the taskconstellationid in the containscreative-table:
			sql = connection
					.formatString(
							"SELECT taskconstellationid FROM containscreative WHERE id = {?};",
							Value.fromString(containscreativeid));
			String tcId = connection.query(sql).iterator().next().iterator()
					.next().asString();
			// get the fitting assignment for the found taskconstellationid
			sql = connection.formatString(
					"SELECT assignmentid FROM taskconstellation WHERE id = {?};",
					Value.fromString(tcId));
			String assId = connection.query(sql).iterator().next().iterator()
					.next().asString();
			sql = connection.formatString("SELECT mturkid FROM assignment WHERE id = {?}",Value.fromString(assId));
			String mturk = connection.query(sql).iterator().next().iterator().next().asString();
		return mturk;
	}catch (SQLException e) {
		throw new DatabaseException(e.getMessage());
	}
	}

	@Override
	public void markAsInvalid() throws DatabaseException {
		try {
			String sql = connection.formatString("UPDATE answer SET marked_as_invalid = {?} WHERE id = {?};", Value.fromInt(1), Value.fromString(this.id));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}
		

	@Override
	public boolean isInvalid() throws DatabaseException {
		int ret = 0;
		try {
			String sql = connection.formatString("SELECT marked_as_invalid FROM answer WHERE id = {?};", Value.fromString(this.id));
			ret = connection.query(sql).iterator().next().iterator().next().asInt();
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		if (ret == 0) {
			return false;
	}
		else 
			return true;
	}


}

package edu.kit.ipd.creativecrowd.persistentmodel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.kit.ipd.creativecrowd.database.DatabaseConnection;
import edu.kit.ipd.creativecrowd.database.Value;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRating;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRatingTask;
import edu.kit.ipd.creativecrowd.readablemodel.CreativeTask;

/**
 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableRatingTask
 * @see edu.kit.ipd.creativecrowd.readablemodel.RatingTask
 * @author Philipp + Alexis
 */
class PersistentRatingTask implements MutableRatingTask {

	/** The unique id of a persistent RatingTask. */
	private String id;

	/** The connection to the database. */
	private DatabaseConnection connection;

	/**
	 * Instantiates a new persistent rating task.
	 *
	 * @param id, a unique id of a persistent rating task
	 * @param connection, a connection to the database
	 */
	PersistentRatingTask(String id, DatabaseConnection connection) {
		this.connection = connection;
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * edu.kit.ipd.creativecrowd.readablemodel.RatingTask#getAnswersToBeRated()
	 */
	@Override
	public Iterable<MutableAnswer> getAnswersToBeRated() throws DatabaseException {

		List<MutableAnswer> ret = new ArrayList<MutableAnswer>();
		try {
			String sql = connection.formatString(
					"SELECT answerid FROM findratingsfor WHERE ratingtaskid = {?} ORDER BY answerid ASC;",
					Value.fromString(this.id));
			for (Iterable<Value> row : connection.query(sql)) {
				ret.add(new PersistentAnswer(row.iterator().next().asString(), connection));
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * edu.kit.ipd.creativecrowd.mutablemodel.MutableRatingTask#getRatings()
	 */
	@Override
	public Iterable<MutableRating> getRatings() throws DatabaseException {
		List<MutableRating> ret = new ArrayList<MutableRating>();
		try {
			// get the containsevaluativeid where this ratingtaskid occurs
			String sql = connection
					.formatString(
							"SELECT id FROM containsevaluative WHERE ratingtaskid = {?};",
							Value.fromString(this.id));
			String containsevaluativeid = connection.query(sql).iterator()
					.next().iterator().next().asString();
			// get all ratings which point on the found containsevaluativeid and add them to ret
			sql = connection.formatString(
					"SELECT id FROM rating WHERE containsevaluativeid = {?};",
					Value.fromString(containsevaluativeid));
			for (Iterable<Value> row : connection.query(sql)) {
				ret.add(new PersistentRating(row.iterator().next().asString(),
						connection));
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.Task#getID()
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

	public int getNumAnswersToBeRated() throws DatabaseException {
		int ret = 0;
		try {
			String sql = connection.formatString(
					"SELECT id FROM findratingsfor WHERE ratingtaskid = {?};",
					Value.fromString(this.id));
			/*-{?}|Test Repo-Review|MainUser|c1|{?}*/
			for (@SuppressWarnings("unused") Iterable<Value> row : connection.query(sql)) {
				ret++;
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * edu.kit.ipd.creativecrowd.mutablemodel.MutableRatingTask#addAnswerToBeRated
	 * (edu.kit.ipd.creativecrowd.mutablemodel.MutableAnswer)
	 */
	@Override
	public void addAnswerToBeRated(MutableAnswer as) throws DatabaseException {
		try {
			List<Value> args = new ArrayList<Value>();
			args.add(Value.fromString(connection.generateID("findratingsfor")));
			args.add(Value.fromString(this.id));
			args.add(Value.fromString(as.getID()));
			args.add(Value.fromInt(getNumAnswersToBeRated()));
			String sql = connection
					.formatString(
							"INSERT INTO findratingsfor (id,ratingtaskid,answerid,answer_position) VALUES ({?},{?},{?},{?});",
							args);
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	@Override
	public MutableAnswer getAnswerToBeRated(String ansId)
			throws DatabaseException {
		MutableAnswer ret = null;
		try {
			String sql = connection.formatString("SELECT ratingtaskid FROM findratingsfor WHERE answerid = {?};", Value.fromString(ansId));
			Iterable<Iterable<Value>> rows = connection.query(sql);
			boolean ansExists = false;
			for (Iterable<Value> row : rows) {
				if (row.iterator().next().asString().equals(this.id)) {
					ansExists = true;
				}
			}
			if (ansExists) {
				ret = new PersistentAnswer(ansId, connection);
			}
			else {
				throw new DatabaseException("Answer is not to be rated in this RatingTask");
			}

		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ret;
	}

	@Override
	public CreativeTask getCreativeTask() throws DatabaseException {
		try {
			String sql = connection.formatString("SELECT creativetask.id FROM ratingtask , creativetask WHERE creativetask.experimentid = ratingtask.experimentid AND ratingtask.id = {?}", Value.fromString(id));
			return new PersistentCreativeTask(connection.query(sql).iterator()
					.next().iterator().next().asString(), this.connection);
			
		}catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	@Override
	public String getDescription() throws DatabaseException {
		try {
			String sql = connection.formatString("SELECT description FROM ratingtask WHERE ratingtask.id = {?}", Value.fromString(id));
			return connection.query(sql).iterator()
					.next().iterator().next().asString();
			
		}catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

}

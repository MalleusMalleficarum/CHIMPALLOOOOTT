package edu.kit.ipd.creativecrowd.persistentmodel;

import java.sql.SQLException;

import edu.kit.ipd.creativecrowd.database.DatabaseConnection;
import edu.kit.ipd.creativecrowd.database.Value;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAssignment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableStats;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableTaskConstellation;

/**
 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableStats
 * @see edu.kit.ipd.creativecrowd.readablemodel.Stats
 * @author Philipp + Alexis
 */
class PersistentStats implements MutableStats {

	static int workerTimeoutSeconds = 1800;

	/** The unique id of the persistent stats. */
	private String id;

	/** The connection to the database. */
	private DatabaseConnection connection;

	/**
	 * Instantiates a new persistent stats.
	 *
	 * @param id, a unique id of the persistent stats
	 * @param connection, a connection to the database
	 */
	public PersistentStats(String id, DatabaseConnection connection) {
		this.connection = connection;
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.Stats#getCancelledCount()
	 */
	@Override
	public int getCancelledCount() throws DatabaseException {
		int ret = 0;
		try {
			String sql = connection.formatString(
					"SELECT id FROM assignment WHERE is_submitted = 0 AND  strftime('%s','now') - strftime('%s',sqltime) < {?} AND experimentid = {?};",
					Value.fromLong(workerTimeoutSeconds), Value.fromString(this.getExperiment().getID()));
			for (Iterable<Value> row : connection.query(sql)) {
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
	 * edu.kit.ipd.creativecrowd.readablemodel.Stats#getPreviewClicksCount()
	 */
	@Override
	public int getPreviewClicksCount() throws DatabaseException {
		int ret;
		try {
			String sql = connection.formatString(
					"SELECT preview_clicks_count FROM stats WHERE id = {?}; ",
					Value.fromString(this.id));
			ret = connection.query(sql).iterator().next().iterator().next()
					.asInt();

		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.Stats#getAcceptedHitCount()
	 */
	@Override
	public int getAcceptedHitCount() throws DatabaseException {
		int ret;
		try {
			String sql = connection.formatString(
					"SELECT COUNT(id) FROM assignment WHERE experimentid = {?};",
					Value.fromString(this.getExperiment().getID()));
			ret = connection.query(sql).iterator().next().iterator().next().asInt();

		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.Stats#getSubmissionCount()
	 */
	@Override
	public int getSubmissionCount() throws DatabaseException {
		int ret;
		try {
			String sql = connection.formatString(
					"SELECT COUNT(id) FROM assignment WHERE is_submitted = 1 AND experimentid = {?};",
					Value.fromString(this.getExperiment().getID()));
			ret = connection.query(sql).iterator().next().iterator().next().asInt();

		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.Stats#getRatingCount()
	 */
	@Override
	public int getRatingCount() throws DatabaseException {
		int ret = 0;
		try {
			for (MutableAssignment assignment : this.getExperiment().getAssignments()) {
				MutableTaskConstellation tc = assignment.getTaskConstellation();
				String sql = connection.formatString("SELECT id FROM containsevaluative WHERE taskconstellationid = {?};", Value.fromString(tc.getID()));
				// for each containsevaluative add ratings containing this containsevaluativeid to ret
				for (Iterable<Value> containsevaluativeRow : connection.query(sql)) {
					String containsevaluativeid = containsevaluativeRow.iterator().next().asString();
					sql = connection.formatString("SELECT COUNT(id) FROM rating WHERE containsevaluativeid = {?};", Value.fromString(containsevaluativeid));
					ret += connection.query(sql).iterator().next().iterator().next().asInt();
				}
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.Stats#getTimestampBegin()
	 */
	@Override
	public String getTimestampBegin() throws DatabaseException {
		String ret;
		try {
			String sql = connection.formatString(
					"SELECT sqltime FROM stats WHERE id = {?}; ",
					Value.fromString(this.id));
			ret = connection.query(sql).iterator().next().iterator().next()
					.asString();

		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * edu.kit.ipd.creativecrowd.mutablemodel.MutableStats#addPreviewClick()
	 */
	@Override
	public void addPreviewClick() throws DatabaseException {
		try {
			String sql = connection
					.formatString(
							"UPDATE stats SET preview_clicks_count = preview_clicks_count + 1 WHERE id = {?}; ",
							Value.fromString(this.id));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
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
	 * @see
	 * edu.kit.ipd.creativecrowd.mutablemodel.MutableStats#getExperiment()
	 */
	@Override
	public MutableExperiment getExperiment() throws DatabaseException {
		MutableExperiment ret = null;
		try {
			String sql = connection.formatString("SELECT id FROM experiment WHERE statsid = {?};", Value.fromString(this.id));
			ret = new PersistentExperiment(connection.query(sql).iterator().next().iterator().next().asString(), this.connection);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ret;
	}

}

package edu.kit.ipd.creativecrowd.persistentmodel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.kit.ipd.creativecrowd.database.DatabaseConnection;
import edu.kit.ipd.creativecrowd.database.Value;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableCreativeTask;

/**
 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableCreativeTask
 * @see edu.kit.ipd.creativecrowd.readablemodel.CreativeTask
 * @author Philipp & Alexis
 */
class PersistentCreativeTask implements MutableCreativeTask {

	/** The connection to the database. */
	private DatabaseConnection connection;

	/** The unique id of a persistent CreativeTask. */
	private String id;

	/**
	 * Instantiates a new persistent creative task.
	 *
	 * @param id, a unique id of a persistent creative task
	 * @param connection, a connection to the database
	 */
	PersistentCreativeTask(String id, DatabaseConnection connection) {
		this.connection = connection;
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.CreativeTask#getPictureURL()
	 */
	@Override
	public String getPictureURL() throws DatabaseException {
		String result = null;
		try {
			String sql = connection.formatString("SELECT picture_url FROM creativetask WHERE id = {?};", Value.fromString(id));
			Iterable<Iterable<Value>> pictureSourceURLs = connection.query(sql);
			result = pictureSourceURLs.iterator().next().iterator().next().asString();
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.CreativeTask#getPictureLicenseURL()
	 */
	@Override
	public String getPictureLicenseURL() throws DatabaseException {
		String result = null;
		try {
			String sql = connection.formatString("SELECT picture_source_url FROM creativetask WHERE id = {?};", Value.fromString(id));
			Iterable<Iterable<Value>> pictureURLs = connection.query(sql);
			result = pictureURLs.iterator().next().iterator().next().asString();
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.Task#getQuestion()
	 */
	@Override
	public String getDescription() throws DatabaseException {
		String result = null;
		try {
			String sql = connection.formatString("SELECT description FROM creativetask WHERE id = {?};", Value.fromString(id));
			Iterable<Iterable<Value>> pictureURLs = connection.query(sql);
			result = pictureURLs.iterator().next().iterator().next().asString();
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage() + " Wanted description does not exist");
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableCreativeTask#setQuestion(java.lang.String)
	 */
	@Override
	public void setDescription(String question) throws DatabaseException {
		try {
			String sql = connection.formatString("UPDATE creativetask SET description = {?} WHERE id = {?};", Value.fromString(question), Value.fromString(id));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableCreativeTask#setPicture(java.lang.String, java.lang.String)
	 */
	@Override
	public void setPicture(String urlImage, String urlLicense) throws DatabaseException {
		try {
			String sql = connection.formatString("UPDATE creativetask SET picture_url = {?} WHERE id = {?};", Value.fromString(urlImage), Value.fromString(id));
			connection.query(sql);
			sql = connection.formatString("UPDATE creativetask SET picture_source_url = {?} WHERE id = {?};", Value.fromString(urlLicense), Value.fromString(id));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableCreativeTask#getAnswers()
	 */
	@Override
	public Iterable<? extends MutableAnswer> getAnswers() throws DatabaseException {
		List<MutableAnswer> answers = new ArrayList<MutableAnswer>();

		try {
			// fetch the containscreativeids which contain this creativeTask
			String sql = connection.formatString("SELECT id FROM containscreative WHERE creativetaskid = {?}", Value.fromString(this.id));
			Iterable<Iterable<Value>> ret = connection.query(sql);
			if (ret.iterator().hasNext()) {
				for (Iterable<Value> cc : ret) {
					String ccid = cc.iterator().next().asString();
					// find all answers which refer to the found ccid
					sql = connection.formatString("SELECT id FROM answer WHERE containscreativeid = {?};", Value.fromString(ccid));
					for (Iterable<Value> row : connection.query(sql)) {
						answers.add(new PersistentAnswer(row.iterator().next().asString(), this.connection));
					}
				}
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return answers;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.CreativeTask#getID()
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

	@Override
	public String getAccordingRatingTaskDescription() throws DatabaseException {
		String result = null;
		try {
			String sql = connection.formatString("SELECT ratingTask_description FROM creativetask WHERE id = {?};", Value.fromString(id));
			Iterable<Iterable<Value>> description = connection.query(sql);
			result = description.iterator().next().iterator().next().asString();
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage() + " Wanted description does not exist");
		}
		return result;
	}

	@Override
	public void setAccordingRatingTaskDescription(String ratingTaskDescription) throws DatabaseException {
		try {
			String sql = connection.formatString("UPDATE creativetask SET ratingtask_description = {?} WHERE id = {?};", Value.fromString(ratingTaskDescription), Value.fromString(id));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

}

/**
 * 
 */
package edu.kit.ipd.creativecrowd.persistentmodel;

import java.sql.SQLException;

import edu.kit.ipd.creativecrowd.database.DatabaseConnection;
import edu.kit.ipd.creativecrowd.database.Value;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableControlAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableControlQuestion;
import edu.kit.ipd.creativecrowd.readablemodel.ControlAnswer;

/**
 * @author basti
 *
 */
public class PersistentControlAnswer implements MutableControlAnswer, ControlAnswer{
	
	/** The connection to the database. */
	private DatabaseConnection connection;

	/** The unique id of a persistent CalibrationAnswer. */
	private String id;

	/**
	 * 
	 * @param id, a unique id of a persistent calibration answer
	 * @param connection, a connection to the database
	 */
	public PersistentControlAnswer(DatabaseConnection connection, String id) {
		super();
		this.connection = connection;
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.ControlAnswer#getID()
	 */
	@Override
	public String getID() {
		return id;
	}

	/* (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.ControlAnswer#getIsCorrect()
	 */
	@Override
	public boolean getIsCorrect() throws DatabaseException {
		String ret = "";
		try {
			String sql = connection.formatString("SELECT istrue FROM controlanswer WHERE id = {?};", Value.fromString(this.id));
			ret = connection.query(sql).iterator().next().iterator().next().asString();

		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		if(ret == "true") return true;
		return false;
	}

	/* (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.ControlAnswer#getControlQuestionID()
	 */
	@Override
	public MutableControlQuestion getControlQuestion() throws DatabaseException {
		MutableControlQuestion ret = null;
		try {
			String sql = connection.formatString("SELECT controlquestionid FROM controlanswer WHERE id = {?};", Value.fromString(this.id));
			String ccid = connection.query(sql).iterator().next().iterator()
					.next().asString();
			ret = new PersistentControlQuestion(connection, ccid);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ret;
	}

	/* (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableControlAnswer#setWasCorrect(boolean)
	 */
	@Override
	public void setIsCorrect(boolean correct) throws DatabaseException {
		String t = "false";
		if(correct) t = "true";
		try {
			String sql = connection.formatString("UPDATE controlanswer SET istrue = {?} WHERE id = {?};", Value.fromString(t), Value.fromString(this.id));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}	
	}

	@Override
	public void setControlQuestion(String controlquestID) throws DatabaseException {
		try {
			String sql = connection.formatString("UPDATE controlanswer SET controlquestionid = {?} WHERE id = {?};", Value.fromString(controlquestID), Value.fromString(this.id));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}


}

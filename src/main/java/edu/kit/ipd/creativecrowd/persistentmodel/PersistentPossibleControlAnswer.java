/**
 * 
 */
package edu.kit.ipd.creativecrowd.persistentmodel;

import java.sql.SQLException;

import edu.kit.ipd.creativecrowd.database.DatabaseConnection;
import edu.kit.ipd.creativecrowd.database.Value;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableControlQuestion;
import edu.kit.ipd.creativecrowd.mutablemodel.MutablePossibleControlAnswer;
import edu.kit.ipd.creativecrowd.readablemodel.PossibleControlAnswer;

/**
 * @author GhostFaceKillah
 *
 */
public class PersistentPossibleControlAnswer implements PossibleControlAnswer, MutablePossibleControlAnswer {
	
	/** The connection to the database. */
	private DatabaseConnection connection;

	/** The unique id of a persistent answer. */
	private String id;

	
	/**
	 * Instantiates a new persistent possiblecontrolanswer.
	 *
	 * @param id, a unique id of a persistent possiblecontrolanswer
	 * @param connection, a connection to the database
	 */
	PersistentPossibleControlAnswer(String id, DatabaseConnection connection) {
		this.id = id;
		this.connection = connection;
	}

	/* (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutablePossibleControlAnswer#setText(java.lang.String)
	 */
	@Override
	public void setText(String answer) throws DatabaseException {
		try {
			String sql = connection.formatString("UPDATE possiblecontrolanswer SET answer = {?} WHERE id = {?};", Value.fromString(answer), Value.fromString(this.id));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutablePossibleControlAnswer#setIsTrue(boolean)
	 */
	@Override
	public void setIsTrue(boolean isTrue) throws DatabaseException {
		String t = "false";
		if(isTrue) t = "true";
		try {
			String sql = connection.formatString("UPDATE possiblecontrolanswer SET istrue = {?} WHERE id = {?};", Value.fromString(t), Value.fromString(this.id));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutablePossibleControlAnswer#setControlQuestion(java.lang.String)
	 */
	@Override
	public void setControlQuestion(String controlquestID) throws DatabaseException {
		try {
			String sql = connection.formatString("UPDATE possiblecontrolanswer SET controlquestionid = {?} WHERE id = {?};", Value.fromString(controlquestID), Value.fromString(this.id));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.PossibleControlAnswer#getID()
	 */
	@Override
	public String getID() {
		return id;
	}

	/* (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.PossibleControlAnswer#getText()
	 */
	@Override
	public String getText() throws DatabaseException {
		String ret = "";
		try {
			String sql = connection.formatString("SELECT answer FROM possiblecontrolanswer WHERE id = {?};", Value.fromString(this.id));
			ret = connection.query(sql).iterator().next().iterator().next().asString();

		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ret;
	}

	/* (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.PossibleControlAnswer#getIsTrue()
	 */
	@Override
	public boolean getIsTrue() throws DatabaseException {
		String ret = "";
		try {
			String sql = connection.formatString("SELECT istrue FROM possiblecontrolanswer WHERE id = {?};", Value.fromString(this.id));
			ret = connection.query(sql).iterator().next().iterator().next().asString();

		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		if(ret.equals("true")) return true;
		return false;
	}

	/* (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.PossibleControlAnswer#getControlQuestionID()
	 */
	@Override
	public MutableControlQuestion getControlQuestion() throws DatabaseException {
		MutableControlQuestion ret = null;
		try {
			String sql = connection.formatString("SELECT controlquestionid FROM possiblecontrolanswer WHERE id = {?};", Value.fromString(this.id));
			String ccid = connection.query(sql).iterator().next().iterator()
					.next().asString();
			ret = new PersistentControlQuestion(connection, ccid);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ret;
	}
}

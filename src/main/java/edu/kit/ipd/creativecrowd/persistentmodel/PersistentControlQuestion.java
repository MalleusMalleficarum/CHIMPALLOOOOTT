/**
 * 
 */
package edu.kit.ipd.creativecrowd.persistentmodel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.kit.ipd.creativecrowd.database.DatabaseConnection;
import edu.kit.ipd.creativecrowd.database.Value;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableControlAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableControlQuestion;
import edu.kit.ipd.creativecrowd.mutablemodel.MutablePossibleControlAnswer;
import edu.kit.ipd.creativecrowd.readablemodel.ControlQuestion;

/**
 *  @see edu.kit.ipd.creativecrowd.mutablemodel.MutableControllQuestion
 * @see edu.kit.ipd.creativecrowd.readablemodel.ControllQuestion
 * @author Bastian
 *
 */
public class PersistentControlQuestion implements ControlQuestion, MutableControlQuestion {
	
	/** The connection to the database. */
	private DatabaseConnection connection;

	/** The unique id of a persistent ControllQuestion. */
	private String id;

	/**
	 * 
	 * @param id, a unique id of a persistent ControllQuestion
	 * @param connection, a connection to the database
	 */
	public PersistentControlQuestion(DatabaseConnection connection, String id) {
		super();
		this.connection = connection;
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableControlQuestion#setQuestion(java.lang.String)
	 */
	@Override
	public void setQuestion(String question) throws DatabaseException {
		try {
			String sql = connection.formatString("UPDATE controlquestion SET question = {?} WHERE id = {?};", Value.fromString(question), Value.fromString(id));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableControlQuestion#addPossibleAnwer(java.lang.String, boolean)
	 */
	@Override
	public MutablePossibleControlAnswer addPossibleAnswer() throws DatabaseException {
		String pansid = connection.generateID("possiblecontrolanswer");
		try {
			String sql = connection.formatString("INSERT INTO possiblecontrolanswer (id,controlquestionid) VALUES ({?},{?});", Value.fromString(pansid), Value.fromString(id));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage() + " An error occured creating the RatingOption");
		}
		return new PersistentPossibleControlAnswer(pansid, this.connection);
	}

	/* (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableControlQuestion#getAnswers()
	 */
	@Override
	public Iterable<? extends MutablePossibleControlAnswer> getPossibleAnswers() throws DatabaseException {
		List<MutablePossibleControlAnswer> ctrlans = new ArrayList<MutablePossibleControlAnswer>();
		try {
			String sql = connection.formatString("SELECT id FROM possiblecontrolanswer WHERE controlquestionid = {?};", Value.fromString(id));
			for (Iterable<Value> row : connection.query(sql)) {
				ctrlans.add(new PersistentPossibleControlAnswer(row.iterator().next().asString(), this.connection));
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ctrlans;
	}

	/* (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.ControlQuestion#getQuestion()
	 */
	@Override
	public String getQuestion() throws DatabaseException {
		String quest = null;
		try {
			String sql = connection.formatString("SELECT question FROM controlquestion  WHERE id = {?};", Value.fromString(id));
			Iterable<Iterable<Value>> values = connection.query(sql);
			quest = values.iterator().next().iterator().next().asString();
			
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return quest;
	}

	/* (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.ControlQuestion#getID()
	 */
	@Override
	public String getID() {
				return id;
	}

	@Override
	public MutableControlAnswer addAnswer() throws DatabaseException {
		String ansid = connection.generateID("controlanswer");
		try {
			String sql = connection.formatString("INSERT INTO controlanswer (id,controlquestionid) VALUES ({?},{?});", Value.fromString(ansid), Value.fromString(id));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage() + " An error occured creating the RatingOption");
		}
		return new PersistentControlAnswer(this.connection, ansid);
	}

	@Override
	public Iterable<? extends MutableControlAnswer> getAnswers() throws DatabaseException {
		List<MutableControlAnswer> ctrlans = new ArrayList<MutableControlAnswer>();
		try {
			String sql = connection.formatString("SELECT id FROM controlanswer WHERE controlquestionid = {?};", Value.fromString(id));
			for (Iterable<Value> row : connection.query(sql)) {
				ctrlans.add(new PersistentControlAnswer(this.connection, row.iterator().next().asString()));
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ctrlans;
	}

	@Override
	public void removePossibleAnswer(String answer) throws DatabaseException {
		try {
			String sql = connection.formatString("DELETE FROM possiblecontrolanswer WHERE id ={?};", Value.fromString(answer));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		
	}
}

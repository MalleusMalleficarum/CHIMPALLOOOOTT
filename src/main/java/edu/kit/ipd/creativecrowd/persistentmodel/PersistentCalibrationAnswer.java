/**
 * 
 */
package edu.kit.ipd.creativecrowd.persistentmodel;

import java.sql.SQLException;

import edu.kit.ipd.creativecrowd.database.DatabaseConnection;
import edu.kit.ipd.creativecrowd.database.Value;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableCalibrationAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableCalibrationQuestion;
import edu.kit.ipd.creativecrowd.mutablemodel.MutablePossibleCalibrationAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableWorker;
import edu.kit.ipd.creativecrowd.readablemodel.CalibrationAnswer;

/**
 *  @see edu.kit.ipd.creativecrowd.mutablemodel.MutableCalibrationAnswer
 * @see edu.kit.ipd.creativecrowd.readablemodel.CalibrationAnswer
 * @author Bastian
 *
 */
public class PersistentCalibrationAnswer implements CalibrationAnswer, MutableCalibrationAnswer {
	
	/** The connection to the database. */
	private DatabaseConnection connection;

	/** The unique id of a persistent CalibrationAnswer. */
	private String id;

	/**
	 * 
	 * @param id, a unique id of a persistent calibration answer
	 * @param connection, a connection to the database
	 */
	public PersistentCalibrationAnswer(DatabaseConnection connection, String id) {
		super();
		this.connection = connection;
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableCalibrationAnswer#setAnswer(java.lang.String)
	 */
	@Override
	public void setAnswer(String answer) throws DatabaseException {
		try {
			String sql = connection.formatString(
					"UPDATE calibrationanswer SET answer = {?} WHERE id = {?};",Value.fromString(answer),
					Value.fromString(id));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		
	}

	/* (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableCalibrationAnswer#setAnswer(int)
	 */
	@Override
	public void setAnswer(int answer) throws DatabaseException {
		try {
			String answ = Integer.toString(answer);
			String sql = connection.formatString(
					"Update calibrationanswer set answer = {?} WHERE id = {?};",Value.fromString("/int/"+answ),
					Value.fromString(id));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		

	}

	/* (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableCalibrationAnswer#setWorkerID(int)
	 */
	@Override
	public void setWorkerID(String workerID) throws DatabaseException {
		try {
			String sql = connection.formatString(
					"Update calibrationanswer set workerid = {?} WHERE id = {?};",Value.fromString(workerID),
					Value.fromString(id));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		
	
	}

	/* (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.CalibrationAnswer#getAnswer()
	 */
	@Override
	public String getAnswer() throws DatabaseException {
		String answer = "";
		try {
			String sql = connection.formatString(
					"SELECT answer FROM calibrationanswer WHERE id = {?};",
					Value.fromString(id));
			answer = connection.query(sql).iterator().next()
					.iterator().next().asString();
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}

		return answer;
	
	}

	/* (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.CalibrationAnswer#getWorkerID()
	 */
	@Override
	public MutableWorker getWorker() throws DatabaseException {
		MutableWorker ret = null;
		try {
			String sql = connection.formatString(
					"SELECT workerid FROM calibrationanswer WHERE id = {?};",
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
	public void setCalibrationQuestion(String calibquestID) throws DatabaseException {
		try {
			String sql = connection.formatString("UPDATE calibrationanswer SET calibrationquestionid = {?} WHERE id = {?};", Value.fromString(calibquestID), Value.fromString(this.id));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}


	@Override
	public String getID() {
		return id;
	}

	@Override
	public MutableCalibrationQuestion getCalibrationQuestion() throws DatabaseException {
		MutableCalibrationQuestion ret = null;
		try {
			String sql = connection.formatString("SELECT calibrationquestionid FROM calibrationanswer WHERE id = {?};", Value.fromString(this.id));
			String ccid = connection.query(sql).iterator().next().iterator()
					.next().asString();
			ret = new PersistentCalibrationQuestion(ccid, connection);

		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ret;
	}

	@Override
	public boolean isCorrect() throws DatabaseException {
		Iterable <? extends MutablePossibleCalibrationAnswer> posans = this.getCalibrationQuestion().getPossibleAnswers();
		for (MutablePossibleCalibrationAnswer mpca : posans) {
			if (mpca.getText().equals(this.getAnswer())) {
				return mpca.getIsTrue();
			}
		}
		return false;
	}

}

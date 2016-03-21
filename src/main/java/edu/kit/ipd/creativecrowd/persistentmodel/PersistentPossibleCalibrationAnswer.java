package edu.kit.ipd.creativecrowd.persistentmodel;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;




import edu.kit.ipd.creativecrowd.database.DatabaseConnection;
import edu.kit.ipd.creativecrowd.database.Value;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableCalibrationQuestion;
import edu.kit.ipd.creativecrowd.mutablemodel.MutablePossibleCalibrationAnswer;
import edu.kit.ipd.creativecrowd.readablemodel.PossibleCalibrationAnswer;

public class PersistentPossibleCalibrationAnswer
		implements PossibleCalibrationAnswer, MutablePossibleCalibrationAnswer {
	
	/** The connection to the database. */
	private DatabaseConnection connection;

	/** The unique id of a persistent answer. */
	private String id;

	/**
	 * Instantiates a new persistent calibanswer.
	 *
	 * @param id, a unique id of a persistent calibanswer
	 * @param connection, a connection to the database
	 */
	PersistentPossibleCalibrationAnswer(String id, DatabaseConnection connection) {
		this.id = id;
		this.connection = connection;
	}

	@Override
	public void setText(String answer) throws DatabaseException {
		try {
			String sql = connection.formatString("UPDATE possiblecalibrationanswer SET answer = {?} WHERE id = {?};", Value.fromString(answer), Value.fromString(this.id));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	@Override
	public void setIsTrue(boolean isTrue) throws DatabaseException {
		String t = "false";
		if(isTrue) t = "true";
		try {
			String sql = connection.formatString("UPDATE possiblecalibrationanswer SET istrue = {?} WHERE id = {?};", Value.fromString(t), Value.fromString(this.id));
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
	public String getText() throws DatabaseException {
		String ret = "";
		try {
			String sql = connection.formatString("SELECT answer FROM possiblecalibrationanswer WHERE id = {?};", Value.fromString(this.id));
			ret = connection.query(sql).iterator().next().iterator().next().asString();

		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ret;
	}
	/**
	 * ROBIN: The return string can be null, and it cannot be compared
	 */
	@Override
	public boolean getIsTrue() throws DatabaseException {
		String ret = "";
		try {
			String sql = connection.formatString("SELECT istrue FROM possiblecalibrationanswer WHERE id = {?};", Value.fromString(this.id));
			ret = connection.query(sql).iterator().next().iterator().next().asString();

		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}

		if(ret == null) {
			return false;
		}
		if(ret.equals("true")) {

			return true;
		}
		return false;
	}

	@Override
	public MutableCalibrationQuestion getCalibrationQuestion() throws DatabaseException {
		MutableCalibrationQuestion ret =null;
		try {
			String sql = connection.formatString("SELECT calibrationquestionid FROM possiblecalibrationanswer WHERE id = {?};", Value.fromString(this.id));
			String ccid = connection.query(sql).iterator().next().iterator()
					.next().asString();
			ret = new PersistentCalibrationQuestion(ccid, connection);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ret;
	}

	@Override
	public void setCalibrationQuestion(String calibquestID) throws DatabaseException {
		try {
			String sql = connection.formatString("UPDATE possiblecalibrationanswer SET calibrationquestionid = {?} WHERE id = {?};", Value.fromString(calibquestID), Value.fromString(this.id));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}
	public Map<String, Object> writeToJson() throws DatabaseException{
		Map<String, Object> vals = new HashMap<String, Object>();
		vals.put("id", this.getID());
		vals.put("text", this.getText());
		vals.put("isTrue", this.getIsTrue());
		return vals;
	}
	
	public boolean readFromJson(Map<String, Object> vals, String calibquestid) throws DatabaseException {
		int i = 0;
		if (vals.get("text") instanceof String && ! vals.get("text").equals("")) i++;
		if (vals.get("isTrue") instanceof Boolean) i++;
		if(i == 2) {
			this.setIsTrue((boolean) vals.get("isTrue"));
			this.setText((String) vals.get("text"));
			this.setCalibrationQuestion(calibquestid);
			return true;
		}
		return false;
	}

}

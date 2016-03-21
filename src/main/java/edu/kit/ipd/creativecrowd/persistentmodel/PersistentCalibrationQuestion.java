/**
 * 
 */
package edu.kit.ipd.creativecrowd.persistentmodel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.kit.ipd.chimpalot.util.Logger;
import edu.kit.ipd.creativecrowd.database.DatabaseConnection;
import edu.kit.ipd.creativecrowd.database.Value;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableCalibrationAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableCalibrationQuestion;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutablePossibleCalibrationAnswer;
import edu.kit.ipd.creativecrowd.readablemodel.CalibrationQuestion;

/**
 *  @see edu.kit.ipd.creativecrowd.mutablemodel.MutableCalibrationQuestion
 * @see edu.kit.ipd.creativecrowd.readablemodel.CalibrationQuestion
 * @author Bastian
 *
 */
public class PersistentCalibrationQuestion implements CalibrationQuestion, MutableCalibrationQuestion {
	
	/** The connection to the database. */
	private DatabaseConnection connection;

	/** The unique id of a persistent CalibrationQuestion. */
	private String id;
	

	/**
	 * Initializes a PersistentCalibrationQuestion
	 * This does NOT change anything at all in the Database.
	 * So if you want to put a new shiny CalibrationQuestion in your Database, then this constructor
	 * DOES NOT HELP YOU AT ALL.
	 * 
	 * @param id, a unique id of a persistent calibration question
	 * @param connection, a connection to the database
	 */
	public PersistentCalibrationQuestion(String id, DatabaseConnection connection) {
		super();
		this.id = id;
		this.connection = connection;
	}
	
	
	/* (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableCalibrationQuestion#setQuestion(java.lang.String)
	 */
	@Override
	public void setQuestion(String question) throws DatabaseException {
		try {
			String sql = connection.formatString("UPDATE calibrationquestion SET question = {?} WHERE id = {?};", Value.fromString(question), Value.fromString(id));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		
		
	}



	/* (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.CalibrationQuestion#getCalibrationAnswers()
	 */
	@Override
	public Iterable<? extends MutableCalibrationAnswer> getCalibrationAnswers() throws DatabaseException {
		List<MutableCalibrationAnswer> ret = new ArrayList<MutableCalibrationAnswer>();
		try {
			String sql = connection.formatString("SELECT id FROM possiblecalibrationanswer WHERE calibrationquestionid = {?};", Value.fromString(this.id));
			for (Iterable<Value> rows : connection.query(sql)) {
				ret.add(new PersistentCalibrationAnswer( this.connection, rows.iterator().next().asString()));
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ret;
	}
	

	/* (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.CalibrationQuestion#getQuestion()
	 */
	@Override
	public String getQuestion() throws DatabaseException {
		String quest = null;
		try {
			String sql = connection.formatString("SELECT question FROM calibrationquestion  WHERE id = {?};", Value.fromString(id));
			Iterable<Iterable<Value>> values = connection.query(sql);
			quest = values.iterator().next().iterator().next().asString();
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return quest;
	}

	/* (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.CalibrationQuestion#getID()
	 */
	@Override
	public String getID()  {
		return id;
	}
	@Override
	public MutablePossibleCalibrationAnswer addPossibleAnswer() throws DatabaseException {
		String ansid = connection.generateID("possiblecalibrationanswer");
		try {
			String sql = connection.formatString("INSERT INTO possiblecalibrationanswer (id,calibrationquestionid) VALUES ({?},{?});", Value.fromString(ansid), Value.fromString(id));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage() + " An error occured creating the PossibleCalibrationAnswer");
		}
		return new PersistentPossibleCalibrationAnswer(ansid, this.connection);
		
	}
	@Override
	public Iterable<? extends MutablePossibleCalibrationAnswer> getPossibleAnswers() throws DatabaseException {
		List<MutablePossibleCalibrationAnswer> ret = new ArrayList<MutablePossibleCalibrationAnswer>();
		try {
			String sql = connection.formatString("SELECT id FROM possiblecalibrationanswer WHERE calibrationquestionid = {?};", Value.fromString(this.id));
			for (Iterable<Value> rows : connection.query(sql)) {
				ret.add(new PersistentPossibleCalibrationAnswer(rows.iterator().next().asString(), this.connection));
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ret;
	}
	
	@Override
	public MutableCalibrationAnswer addAnswer() throws DatabaseException {
		String ansid = connection.generateID("calibrationanswer");
		try {
			String sql = connection.formatString("INSERT INTO calibrationanswer (id,calibrationquestionid) VALUES ({?},{?});", Value.fromString(ansid), Value.fromString(id));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage() + " An error occured creating the RatingOption");
		}
		return new PersistentCalibrationAnswer(this.connection, ansid);
	}


	@Override
	public List<? extends MutableExperiment> getExperiments() throws DatabaseException {
		List<MutableExperiment> cqs = new ArrayList<MutableExperiment>();
		
		try {
			Logger.debug("AB HIER");
			String sql = connection.formatString("SELECT experiment.id FROM containcalib, experiment WHERE calibrationquestionid ={?}  AND experimentid = experiment.id;", Value.fromString(id));
			Logger.debug(sql);
			for (Iterable<Value> rows : connection.query(sql)) {
				
				cqs.add(new PersistentExperiment(rows.iterator().next().asString(),this.connection));
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return cqs;
	}
	@Override
	public void removePossibleAnswer(String id) throws DatabaseException {
		try {
			String sql = connection.formatString("DELETE FROM possiblecalibrationanswer WHERE id ={?};", Value.fromString(id));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		
	}
	@Override
	public void removeExperiment(String name) throws DatabaseException {
		try {
			String sql = connection.formatString("DELETE FROM containcalib WHERE calibrationquestiontid ={?}  AND experimentid ={?};", Value.fromString(id), Value.fromString(name));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}
	@Override
	public void addExperiment(String name) throws DatabaseException {
		String containcalibid = connection.generateID("containcalib");
		try {
			List<Value> args = new ArrayList<Value>();
			args.add(Value.fromString(containcalibid));
			args.add(Value.fromString(id));
			args.add(Value.fromString(name));
			String sql = connection.formatString("INSERT INTO containcalib (id,calibrationquestionid, experimentid) VALUES ({?},{?},{?});", args);
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}
}

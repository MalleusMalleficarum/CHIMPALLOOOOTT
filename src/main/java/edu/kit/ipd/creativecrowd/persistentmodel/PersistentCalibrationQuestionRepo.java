package edu.kit.ipd.creativecrowd.persistentmodel;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import edu.kit.ipd.chimpalot.util.GlobalApplicationConfig;
import edu.kit.ipd.creativecrowd.database.DatabaseConnection;
import edu.kit.ipd.creativecrowd.database.SQLiteDatabaseConnection;
import edu.kit.ipd.creativecrowd.database.Value;
import edu.kit.ipd.creativecrowd.mutablemodel.CalibrationQuestionRepo;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableCalibrationQuestion;

/**
 * @see edu.kit.ipd.creativecrowd.mutablemodel.CalibrationQuestionRepo
 * @author basti
 */
public class PersistentCalibrationQuestionRepo implements CalibrationQuestionRepo {


	/** The connection to the database. */
	DatabaseConnection connection;

	
	/**
	 * Instantiates a new persistent calibrationquestion repository.
	 * Builds up the tables if needed.
	 *
	 * @throws DatabaseException if any of the SQL commands fails
	 */
	public PersistentCalibrationQuestionRepo() throws DatabaseException {
		connection = SQLiteDatabaseConnection.getInstance();

		// set Up connection
		try {
			connection.setUpDatabaseConnection(GlobalApplicationConfig
					.getDBPath());
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}


		// build the tables etc.
		try {
			String sql;
			//Calibrationanswer
						sql = "CREATE TABLE IF NOT EXISTS  calibrationanswer"
								+ "(id TEXT PRIMARY 			KEY     NOT NULL,"
								+ "answer TEXT , "
								+ " sqltime		   TIMESTAMP   DEFAULT CURRENT_TIMESTAMP   NOT NULL, "
								+ " workerid TEXT, "
								+ " calibrationquestionid TEXT);";

						connection.query(sql);
			
			
			//Calibrationquestion
						sql = "CREATE TABLE IF NOT EXISTS  calibrationquestion"
								+ "(id TEXT PRIMARY 			KEY     NOT NULL,"
								+ "question TEXT );";

						connection.query(sql);
						
			//PossibleCalibrationanswer
						sql =  "CREATE TABLE IF NOT EXISTS  possiblecalibrationanswer"
								+ "(id TEXT PRIMARY 			KEY     NOT NULL,"
								+ "calibrationquestionid TEXT , "
								+ "istrue TEXT , "
								+ "answer TEXT );";						

						connection.query(sql);
						
						// "ist Ergebnis von" calibquest-taskconstellation
						sql = "CREATE TABLE IF NOT EXISTS  containscalibtask"
								+ "(id TEXT PRIMARY 			KEY     NOT NULL,"
								+ " taskconstellationid		TEXT , "
								+ " sqltime		   TIMESTAMP   DEFAULT CURRENT_TIMESTAMP   NOT NULL, "
								+ " calibrationquestionid			TEXT, "
								+ " task_position  			INT);";

						connection.query(sql);
						
						// TaskConstellation
						sql = "CREATE TABLE IF NOT EXISTS  taskconstellation"
								+ "(id TEXT PRIMARY KEY     NOT NULL,"
								+ " assignmentid TEXT NOT NULL,"
								+ " again     	   INT,"
								+ " submit         INT, "
								+ " next		   INT,"
								+ " current_pos    INT); ";
						connection.query(sql);
						
						//containsCalib
						sql =  "CREATE TABLE IF NOT EXISTS  containcalib"
								+ "(id TEXT PRIMARY 			KEY     NOT NULL,"
								+ "calibrationquestionid TEXT , "
								+ "experimentid TEXT);";
						connection.query(sql);
			
			
		} catch (Exception e) {

		}
	
	}
	
	
	
	
	
	
	
	/* (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.CalibrationQuestionRepo#createCalibrationQuestion(int)
	 */
	@Override
	public MutableCalibrationQuestion createCalibrationQuestion(String id) throws DatabaseException, IDAlreadyUsedException {
		try {
			String sql = connection.formatString("SELECT * FROM calibrationquestion WHERE id = {?};", Value.fromString(id));
			if (connection.query(sql).iterator().hasNext()) {
				throw new IDAlreadyUsedException("calibrationquestionid already taken");
			}

			sql = connection.formatString("INSERT INTO calibrationquestion (id) VALUES ({?});", Value.fromString(id));
			connection.query(sql);

		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		PersistentCalibrationQuestion ex = new PersistentCalibrationQuestion(id, this.connection);
		return ex;
	}

	/* (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.CalibrationQuestionRepo#loadCalibrationQuestion(int)
	 */
	@Override
	public MutableCalibrationQuestion loadCalibrationQuestion(String id) throws DatabaseException, IDNotFoundException {
		MutableCalibrationQuestion ret = null;
		try {
			String sql = connection.formatString("SELECT * FROM calibrationquestion WHERE id = {?};", Value.fromString(id));
			Iterable<Iterable<Value>> persistentCalibQuests = connection.query(sql);
			if (persistentCalibQuests.iterator().hasNext()) {
				ret = new PersistentCalibrationQuestion(id, this.connection);
			} else {
				throw new IDNotFoundException("requested calibrationquestion does not exist");
			}

		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ret;
	}

	/* (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.CalibrationQuestionRepo#deleteCalibrationQuestion(int)
	 */
	@Override
	public void deleteCalibrationQuestion(String id) throws DatabaseException {
		try {
			String sql =  connection.formatString("DELETE FROM calibrationquestion WHERE id = {?};", Value.fromString(id));
			connection.query(sql);
			
			sql =  connection.formatString("DELETE FROM calibrationanswer WHERE calibrationquestionid = {?};", Value.fromString(id));
			connection.query(sql);
			sql =  connection.formatString("DELETE FROM possiblecalibrationanswer WHERE calibrationquestionid = {?};", Value.fromString(id));
			connection.query(sql);
			sql =  connection.formatString("DELETE FROM containscalibtask WHERE calibrationquestionid = {?};", Value.fromString(id));
			connection.query(sql);
			sql =  connection.formatString("DELETE FROM containcalib WHERE calibrationquestionid = {?};", Value.fromString(id));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}
	@Override
	public List<MutableCalibrationQuestion> loadAllCalibrationQuestions() throws DatabaseException {
		List<MutableCalibrationQuestion> ret = new LinkedList<MutableCalibrationQuestion>();
		try {
			String sql = "SELECT id FROM calibrationquestion";
			Iterable<Iterable<Value>> calibquestids = connection.query(sql);
			Iterator<Iterable<Value>> idIterator = calibquestids.iterator();
		
			while (idIterator.hasNext()) {
				String calibquestid = idIterator.next().iterator().next().asString();
				MutableCalibrationQuestion calibquest = loadCalibrationQuestion(calibquestid);
				ret.add(calibquest);
			}
		} catch (SQLException | IDNotFoundException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ret;
	}

}

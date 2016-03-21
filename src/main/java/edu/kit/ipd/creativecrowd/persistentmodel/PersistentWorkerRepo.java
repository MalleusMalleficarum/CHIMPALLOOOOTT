package edu.kit.ipd.creativecrowd.persistentmodel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import edu.kit.ipd.chimpalot.util.GlobalApplicationConfig;
import edu.kit.ipd.creativecrowd.database.DatabaseConnection;
import edu.kit.ipd.creativecrowd.database.SQLiteDatabaseConnection;
import edu.kit.ipd.creativecrowd.database.Value;
import edu.kit.ipd.creativecrowd.mutablemodel.WorkerRepo;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableWorker;
import edu.kit.ipd.creativecrowd.operations.WorkerPaymentChecker;

/**
 * @see edu.kit.ipd.creativecrowd.mutablemodel.WorkerRepo
 * @author basti
 */
public class PersistentWorkerRepo implements WorkerRepo {
	
	public static final String ANONYM_FLAG  = "!ANONYM";

	/** The connection to the database. */
	DatabaseConnection connection;

	
	/**
	 * Instantiates a new persistent worker repository.
	 * Builds up the tables if needed.
	 *
	 * @throws DatabaseException if any of the SQL commands fails
	 */
	public PersistentWorkerRepo() throws DatabaseException {
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
			// Worker
			String sql = "CREATE TABLE IF NOT EXISTS worker"
					+ "(workerid TEXT PRIMARY KEY     NOT NULL,"
					+ " name    TEXT    DEFAULT '' NOT NULL, "
					+ " credit	INT, "
					+ " blocked INT, "
					+ " platform TEXT DEFAULT '',"
					+ " email   TEXT DEFAULT '');";
			connection.query(sql);
			
			// Rating
						sql = "CREATE TABLE IF NOT EXISTS  rating"
								+ "(id TEXT PRIMARY KEY     NOT NULL,"
								+ " sqltime		   TIMESTAMP   DEFAULT CURRENT_TIMESTAMP   NOT NULL, "
								+ " text	   TEXT, "
								+ " ratingoptionid TEXT, "
								+ " final_quality_index    REAL, "
								+ " containsevaluativeid TEXT, "
								+ " workerid TEXT, "
								+ " answerid	   TEXT	  ); ";

						connection.query(sql);

						// Answer
						sql = "CREATE TABLE IF NOT EXISTS  answer"
								+ "(id TEXT PRIMARY 			KEY     NOT NULL,"
								+ " final_quality_index  	REAL , "
								+ " sqltime		   TIMESTAMP   DEFAULT CURRENT_TIMESTAMP   NOT NULL, "
								+ " text   					TEXT, "
								+ " marked_as_rated  			INT, "
								+ " workerid TEXT, "
								+ " containscreativeid			TEXT);";

						connection.query(sql);
			
			//Calibrationanswer ROBIN: i changed calibrationquestion to calibrationquestionid
						sql = "CREATE TABLE IF NOT EXISTS  calibrationanswer"
								+ "(id TEXT PRIMARY 			KEY     NOT NULL,"
								+ "answer TEXT , "
								+ " sqltime		   TIMESTAMP   DEFAULT CURRENT_TIMESTAMP   NOT NULL, "
								+ " workerid TEXT, "
								+ " calibrationquestionid TEXT);";

						connection.query(sql);
						
						//PossibleCalibrationanswer
						sql =  "CREATE TABLE IF NOT EXISTS  possiblecalibrationanswer"
								+ "(id TEXT PRIMARY 			KEY     NOT NULL,"
								+ "calibrationquestionid TEXT , "
								+ "istrue TEXT , "
								+ "answer TEXT );";
						
			
			
			//Calibrationquestion
						sql = "CREATE TABLE IF NOT EXISTS  calibrationquestion"
								+ "(id TEXT PRIMARY 			KEY     NOT NULL,"
								+ "question TEXT );";

						connection.query(sql);
						
			
			
		} catch (Exception e) {

		}
	
	}
	
	
	
	
	
	
	
	/* (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.WorkerRepo#createWorker(int)
	 */
	@Override
	public MutableWorker createWorker(String id) throws DatabaseException {
		try {
			String sql = connection.formatString("SELECT * FROM worker WHERE workerid = {?};", Value.fromString(id));
			if (connection.query(sql).iterator().hasNext()) {
				throw new DatabaseException("workerid already taken");
			}

			sql = connection.formatString("INSERT INTO worker (workerid, credit, email, blocked) VALUES ({?}, 0, '', 0);", Value.fromString(id));
			connection.query(sql);

		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		PersistentWorker ex = new PersistentWorker( this.connection, id);
		return ex;
	}

	/* (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.WorkerRepo#loadWorker(int)
	 */
	@Override
	public MutableWorker loadWorker(String id) throws DatabaseException {
		MutableWorker ret = null;
		try {
			String sql = connection.formatString("SELECT * FROM worker WHERE workerid = {?};", Value.fromString(id));
			Iterable<Iterable<Value>> persistentWorkers = connection.query(sql);
			if (persistentWorkers.iterator().hasNext()) {
				ret = new PersistentWorker(this.connection, id);
			} else {
				throw new DatabaseException("requested Worker does not exist");
			}

		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ret;
	}

	/* (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.WorkerRepo#anonymizeWorker(int)
	 * ROBIN: shouldn't the email and name be replaced, the id is arbitrary after all
	 * Unused credit is removed after anonymization
	 */
	@Override
	public void anonymizeWorker(String  id) throws DatabaseException {
		try {
			WorkerPaymentChecker wp = new WorkerPaymentChecker();
			if(wp.isPayable(id)) {
				wp.payWorker(id);
				}
			String sql =  connection.formatString("UPDATE worker SET name = {?} WHERE workerid = {?};", Value.fromString(ANONYM_FLAG), Value.fromString(id));
			connection.query(sql);
			sql =  connection.formatString("UPDATE worker SET email = {?} WHERE workerid = {?};", Value.fromString(ANONYM_FLAG), Value.fromString(id));
			connection.query(sql);
			sql =  connection.formatString("UPDATE worker SET credit = 0 WHERE workerid = {?};", Value.fromString(id));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}







	@Override
	public List<MutableWorker> loadAllWorkers() throws DatabaseException {
		List<MutableWorker> ret = new LinkedList<MutableWorker>();
		try {
			String sql = "SELECT workerid FROM worker";
			Iterable<Iterable<Value>> workerids = connection.query(sql);
			Iterator<Iterable<Value>> idIterator = workerids.iterator();
		
			while (idIterator.hasNext()) {
				String workerid = idIterator.next().iterator().next().asString();
				MutableWorker worker = loadWorker(workerid);
				ret.add(worker);
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ret;
	}
	
	/**
	 * returns worker
	 * if name, email pair doesnt exists a new worker will get created
	 * @param name of the worker
	 * @param email of the worker
	 * @return worker
	 * @throws DatabaseException
	 */
	public MutableWorker findWorker(String name, String email) throws DatabaseException {
		try {
			String sql = connection.formatString("SELECT workerid FROM worker WHERE name = {?} AND email = {?};", Value.fromString(name), Value.fromString(email));
			if (connection.query(sql).iterator().hasNext()) {
				return new PersistentWorker( this.connection, connection.query(sql).iterator().next().iterator().next().asString());
			}
			String id = connection.generateID("worker");
			List<Value> args = new ArrayList<Value>();
			args.add(Value.fromString(id));
			args.add(Value.fromString(name));
			args.add(Value.fromString(email));
			sql = connection.formatString("INSERT INTO worker (workerid, name, email) VALUES ({?},{?},{?});", args);
			connection.query(sql);
			return new PersistentWorker( this.connection, id);

		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

}

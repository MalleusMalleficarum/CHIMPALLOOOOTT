package edu.kit.ipd.creativecrowd.persistentmodel;

import java.sql.SQLException;
import java.util.Iterator;

import edu.kit.ipd.creativecrowd.database.DatabaseConnection;
import edu.kit.ipd.creativecrowd.database.SQLiteDatabaseConnection;
import edu.kit.ipd.creativecrowd.database.Value;
import edu.kit.ipd.creativecrowd.mutablemodel.ExperimentRepo;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.util.GlobalApplicationConfig;

/**
 * @see edu.kit.ipd.creativecrowd.mutablemodel.ExperimentRepo
 * @author Philipp + Alexis
 */
public class PersistentExperimentRepo implements ExperimentRepo {

	/** The connection to the database. */
	DatabaseConnection connection;

	/**
	 * Instantiates a new persistent experiment repository.
	 * Builds up the tables if needed.
	 *
	 * @throws DatabaseException if any of the SQL commands fails
	 */
	public PersistentExperimentRepo() throws DatabaseException {
		connection = SQLiteDatabaseConnection.getInstance();

		// set Up connection
		try {
			connection.setUpDatabaseConnection(GlobalApplicationConfig
					.getDBPath());
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}

		/*-{?}|Test Repo-Review|Philipp|c0|{?}*/

		// build the tables etc.
		try {
			// Experiment
			String sql = "CREATE TABLE IF NOT EXISTS experiment"
					+ "(id TEXT PRIMARY KEY     NOT NULL,"
					+ " description    TEXT    DEFAULT '', "
					+ " budget		   INT, "
					+ " preview_click_count    INT, "
					+ " hit_mturkid    TEXT, "
					+ " finished       INT, "
					+ " softfinished   INT, "
					+ " answer_payment    INT, "
					+ " rating_payment    INT , "
					+ " basic_payment    INT, "
					+ " statsid        TEXT, "
					+ " ratingviewclass TEXT, "
					+ " sqltime		   TIMESTAMP   DEFAULT CURRENT_TIMESTAMP   NOT NULL, "
					+ " bonus_payment    INT, "
					+ " tags           INT, "
					+ " qualifications    INT, "
					+ " hit_title      INT, "
					+ " max_answers_per_assignment    INT, "
					+ " max_ratings_per_assignment    INT, "
					+ " hit_description    INT);";
			connection.query(sql);

			// Qualification
			sql = "CREATE TABLE IF NOT EXISTS  qualification"
					+ "(id TEXT PRIMARY KEY     NOT NULL,"
					+ " text	   TEXT  , "
					+ " experimentid   TEXT     NOT NULL); ";
			connection.query(sql);

			// Tag
			sql = "CREATE TABLE IF NOT EXISTS  tag"
					+ "(id TEXT PRIMARY KEY     NOT NULL,"
					+ " text	   TEXT   , "
					+ " experimentid   TEXT     NOT NULL); ";
			connection.query(sql);

			// CreativeTask
			sql = "CREATE TABLE IF NOT EXISTS  creativetask"
					+ "(id TEXT PRIMARY KEY     NOT NULL,"
					+ " description	   TEXT  , "
					+ " ratingtask_description	   TEXT  , "
					+ " picture_source_url     TEXT, "
					+ " picture_url	   TEXT	 , "
					+ " experimentid   TEXT     NOT NULL); ";
			connection.query(sql);

			// StrategyParam
			sql = "CREATE TABLE IF NOT EXISTS  strategyparam"
					+ "(id TEXT PRIMARY KEY     NOT NULL,"
					+ " key	  	 TEXT , "
					+ " value    TEXT, "
					+ " experimentid TEXT	   NOT NULL);";
			connection.query(sql);

			// Assignment
			sql = "CREATE TABLE IF NOT EXISTS  assignment"
					+ "(id TEXT PRIMARY KEY     NOT NULL,"
					+ " worker_mturkid TEXT , "
					+ " is_paid	   	   INT  , "
					+ " mturkid        TEXT, "
					+ " is_submitted	   INT, "
					+ " sqltime		   TIMESTAMP   DEFAULT CURRENT_TIMESTAMP   NOT NULL, "
					+ " experimentid   TEXT     NOT NULL); ";
			connection.query(sql);

			// RatingTask
			sql = "CREATE TABLE IF NOT EXISTS  ratingtask"
					+ "(id TEXT PRIMARY KEY     NOT NULL,"
					+ " experimentid   TEXT NOT NULL,"
					+ " description       TEXT );";

			connection.query(sql);

			// Stats
			sql = "CREATE TABLE IF NOT EXISTS  stats"
					+ "(id TEXT PRIMARY KEY     NOT NULL,"
					+ " sqltime		   TIMESTAMP   DEFAULT CURRENT_TIMESTAMP   NOT NULL, "
					+ "num_cancellers	INT,"
					+ "preview_clicks_count	INT DEFAULT 0,"
					+ "accepted_hits_count	INT    DEFAULT 0, "
					+ "rating_count			INT, "
					+ "submission_count		INT );";

			connection.query(sql);

			// PaymentOutcome
			sql = "CREATE TABLE IF NOT EXISTS  paymentoutcome"
					+ "(id TEXT PRIMARY KEY     NOT NULL,"
					+ " assignmentid TEXT	 NOT NULL,"
					+ " receives_base_payment    INT , "
					+ " bonus_amount   INT	); ";
			connection.query(sql);

			// RatingOption
			sql = "CREATE TABLE IF NOT EXISTS  ratingoption"
					+ "(id TEXT PRIMARY KEY     NOT NULL,"
					+ " value	   	   REAL, "
					+ " text    TEXT , "
					+ " experimentid   TEXT	   NOT NULL);";
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

			// Rating
			sql = "CREATE TABLE IF NOT EXISTS  rating"
					+ "(id TEXT PRIMARY KEY     NOT NULL,"
					+ " sqltime		   TIMESTAMP   DEFAULT CURRENT_TIMESTAMP   NOT NULL, "
					+ " text	   TEXT, "
					+ " ratingoptionid TEXT, "
					+ " final_quality_index    REAL, "
					+ " containsevaluativeid TEXT, "
					+ " answerid	   TEXT	  ); ";

			connection.query(sql);

			// Answer
			sql = "CREATE TABLE IF NOT EXISTS  answer"
					+ "(id TEXT PRIMARY 			KEY     NOT NULL,"
					+ " final_quality_index  	REAL , "
					+ " sqltime		   TIMESTAMP   DEFAULT CURRENT_TIMESTAMP   NOT NULL, "
					+ " text   					TEXT, "
					+ " marked_as_rated  			INT, "
					+ " containscreativeid			TEXT);";

			connection.query(sql);

			// "ist Ergebnis von" creativetask-taskconstellation-answer
			sql = "CREATE TABLE IF NOT EXISTS  containscreative"
					+ "(id TEXT PRIMARY 			KEY     NOT NULL,"
					+ " taskconstellationid		TEXT , "
					+ " sqltime		   TIMESTAMP   DEFAULT CURRENT_TIMESTAMP   NOT NULL, "
					+ " creativetaskid			TEXT, "
					+ " task_position  			INT);";

			connection.query(sql);

			// "ist Ergebnis von" ratingtask-rating-taskconstellation
			sql = "CREATE TABLE IF NOT EXISTS  containsevaluative"
					+ "(id TEXT PRIMARY 			KEY     NOT NULL,"
					+ " taskconstellationid		TEXT , "
					+ " sqltime		   TIMESTAMP   DEFAULT CURRENT_TIMESTAMP   NOT NULL, "
					+ " ratingtaskid			TEXT, "
					+ " task_position  			INT);";

			connection.query(sql);

			// relation between m RatingTasks and n Answers
			sql = "CREATE TABLE IF NOT EXISTS  findratingsfor"
					+ "(id TEXT PRIMARY 			KEY     NOT NULL,"
					+ " ratingtaskid		TEXT , "
					+ " sqltime		   TIMESTAMP   DEFAULT CURRENT_TIMESTAMP   NOT NULL, "
					+ " answerid			TEXT, "
					+ " answer_position  			INT);";

			connection.query(sql);

		} catch (Exception e) {

		}
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.ExperimentRepo#createExperiment(java.lang.String)
	 */
	@Override
	public MutableExperiment createExperiment(String name) throws DatabaseException {
		try {
			String sql = connection.formatString("SELECT * FROM experiment WHERE id = {?};", Value.fromString(name));
			if (connection.query(sql).iterator().hasNext()) {
				throw new DatabaseException("experimentid already taken");
			}

			sql = connection.formatString("INSERT INTO experiment (id) VALUES ({?});", Value.fromString(name));
			connection.query(sql);

			String statsid = connection.generateID("stats");
			try {
				sql = connection.formatString("INSERT INTO stats (id) VALUES ({?});", Value.fromString(statsid));
				connection.query(sql);
				sql = connection.formatString("UPDATE experiment SET statsid = {?} WHERE id = {?};", Value.fromString(statsid), Value.fromString(name));
				connection.query(sql);
			} catch (SQLException e) {
				throw new DatabaseException(e.getMessage());
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		PersistentExperiment ex = new PersistentExperiment(name, this.connection);
		return ex;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.ExperimentRepo#loadExperiment(java.lang.String)
	 */
	@Override
	public MutableExperiment loadExperiment(String name) throws DatabaseException {

		MutableExperiment ret = null;
		try {
			String sql = connection.formatString("SELECT * FROM experiment WHERE id = {?};", Value.fromString(name));
			Iterable<Iterable<Value>> persistentExperiments = connection.query(sql);
			if (persistentExperiments.iterator().hasNext()) {
				ret = new PersistentExperiment(name, this.connection);
			} else {
				throw new DatabaseException("requested Experiment does not exist");
			}

		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ret;
	}

	@Override
	public void deleteExperiment(String name) throws DatabaseException {
		try {
			// delete Stats
			String sql = connection.formatString("SELECT statsid FROM experiment WHERE id = {?};", Value.fromString(name));
			String tmpId = connection.query(sql).iterator().next().iterator().next().asString();
			sql = connection.formatString("DELETE FROM stats WHERE id = {?};", Value.fromString(tmpId));
			connection.query(sql);
			// delete RatingOptions
			sql = connection.formatString("DELETE FROM ratingoption WHERE experimentid = {?};", Value.fromString(name));
			connection.query(sql);
			// delete Tags
			sql = connection.formatString("DELETE FROM tag WHERE experimentid = {?};", Value.fromString(name));
			connection.query(sql);
			// delete Qualifications
			sql = connection.formatString("DELETE FROM qualification WHERE experimentid = {?};", Value.fromString(name));
			connection.query(sql);
			// delete Strategy
			sql = connection.formatString("DELETE FROM strategyparam WHERE experimentid = {?};", Value.fromString(name));
			connection.query(sql);

			/*-{?}|Anika|Philipp|c2|{?}*/
			// delete the creative tasks
			sql = connection.formatString("DELETE FROM creativetask WHERE experimentid = {?};", Value.fromString(name));
			connection.query(sql);
			// delete the rating tasks
			sql = connection.formatString("DELETE FROM ratingtask WHERE experimentid = {?};", Value.fromString(name));
			connection.query(sql);

			// for each Assignment and its TC
			sql = connection.formatString("SELECT id FROM assignment WHERE experimentid = {?};", Value.fromString(name));
			Iterable<Iterable<Value>> itrbl = connection.query(sql);
			for (Iterable<Value> ass : itrbl) {
				String assId = ass.iterator().next().asString();
				// get the TC to get the Creative+RatingTasks
				String sql2 = connection.formatString("SELECT id FROM taskconstellation WHERE assignmentid = {?};", Value.fromString(assId));
				String tcId = connection.query(sql2).iterator().next().iterator().next().asString();
				// for each containscreative
				sql2 = connection.formatString("SELECT id,creativetaskid FROM containscreative WHERE taskconstellationid = {?};", Value.fromString(tcId));
				Iterable<Iterable<Value>> it2 = connection.query(sql2);
				for (Iterable<Value> cc : it2) {
					Iterator<Value> cccolumn = cc.iterator();
					String ccid = cccolumn.next().asString();
					String ctid = cccolumn.next().asString();
					// delete Answers pointing at this cc
					String sql3 = connection.formatString("DELETE FROM answer WHERE containscreativeid = {?};", Value.fromString(ccid));
					connection.query(sql3);
					// delete the Creative Task (yes there's only one. Don't care)
					sql3 = connection.formatString("DELETE FROM creativetask WHERE id = {?};", Value.fromString(ctid));
					connection.query(sql3);
					// now delete the containscreative
					sql3 = connection.formatString("DELETE FROM containscreative WHERE id = {?};", Value.fromString(ccid));
					connection.query(sql3);

				}
				// for each containsevaluative
				sql2 = connection.formatString("SELECT id,ratingtaskid FROM containsevaluative WHERE taskconstellationid = {?};", Value.fromString(tcId));
				Iterable<Iterable<Value>> it3 = connection.query(sql2);
				for (Iterable<Value> ce : it3) {
					Iterator<Value> cecolumn = ce.iterator();
					String ceid = cecolumn.next().asString();
					String rtid = cecolumn.next().asString();
					// delete Ratings pointing at this ce
					String sql3 = connection.formatString("DELETE FROM rating WHERE containsevaluativeid = {?};", Value.fromString(ceid));
					connection.query(sql3);
					// delete the RatingTask
					sql3 = connection.formatString("DELETE FROM ratingtask WHERE id = {?};", Value.fromString(rtid));
					connection.query(sql3);
					// delete the findratingsfor-entry
					sql3 = connection.formatString("DELETE FROM findratingsfor WHERE ratingtaskid = {?};", Value.fromString(rtid));
					connection.query(sql3);
					// now delete the containsevaluative
					sql3 = connection.formatString("DELETE FROM containsevaluative WHERE id = {?};", Value.fromString(ceid));
					connection.query(sql3);
				}
				// now delete the taskconstellation
				sql2 = connection.formatString("DELETE FROM taskconstellation WHERE id = {?};", Value.fromString(tcId));
				connection.query(sql2);
				// delete the paymentoutcome
				sql2 = connection.formatString("DELETE FROM paymentoutcome WHERE assignmentid = {?};", Value.fromString(assId));
				connection.query(sql2);
				// now delete the assignment
				sql2 = connection.formatString("DELETE FROM assignment WHERE id = {?};", Value.fromString(assId));
				connection.query(sql2);

			}

			// now, finally, delete the experiment
			sql = connection.formatString("DELETE FROM experiment WHERE id = {?};", Value.fromString(name));
			connection.query(sql);
		} catch (SQLException ex) {
			throw new DatabaseException(ex.getMessage());
		}
	}

}

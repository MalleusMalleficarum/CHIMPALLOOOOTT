package edu.kit.ipd.creativecrowd.persistentmodel;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;

import edu.kit.ipd.chimpalot.util.GlobalApplicationConfig;
import edu.kit.ipd.chimpalot.util.Logger;
import edu.kit.ipd.creativecrowd.database.DatabaseConnection;
import edu.kit.ipd.creativecrowd.database.SQLiteDatabaseConnection;
import edu.kit.ipd.creativecrowd.database.Value;
import edu.kit.ipd.creativecrowd.mutablemodel.ExperimentRepo;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.readablemodel.ConfigModel;

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
					+ " preview_click_count    INT, "
					+ " hit_mturkid      TEXT, "
					+ " finished         INT, "
					+ " softfinished     INT, "
					+ " statsid          TEXT, "
					+ " sqltime		     TIMESTAMP   DEFAULT CURRENT_TIMESTAMP   NOT NULL, "
					+ " config		     TEXT, "
					+ " parentexperiment TEXT, "
					+ " amendment        TEXT, "	
					+ " bonus_payment    INT);";
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

			// Assignment
			sql = "CREATE TABLE IF NOT EXISTS  assignment"
					+ "(id TEXT PRIMARY KEY     NOT NULL,"
					+ " worker_mturkid TEXT , "
					+ " is_paid	   	   INT  , "
					+ " mturkid        TEXT, "
					+ " is_submitted	   INT, "
					+ " sqltime		   TIMESTAMP   DEFAULT CURRENT_TIMESTAMP   NOT NULL, " //This was already there, but I'm not sure if it is actually used or not. Using a new row for submissiontime...
					+ " submissiontime TIMESTAMP, "
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
					+ " marked_as_invalid INT, "
					+ " workerid TEXT, "
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
			
			// "ist Ergebnis von" Controlquest-taskconstellation
						sql = "CREATE TABLE IF NOT EXISTS  containscontrol"
								+ "(id TEXT PRIMARY 			KEY     NOT NULL,"
								+ " taskconstellationid		TEXT , "
								+ " sqltime		   TIMESTAMP   DEFAULT CURRENT_TIMESTAMP   NOT NULL, "
								+ " controlquestionid			TEXT, "
								+ " task_position  			INT);";

						connection.query(sql);
						
						// "ist Ergebnis von" calibquest-taskconstellation
						// Overliquid ist jetzt anders ersetzt
//						sql = "CREATE TABLE IF NOT EXISTS  containscalibtask"
//								+ "(id TEXT PRIMARY 			KEY     NOT NULL,"
//								+ " taskconstellationid		TEXT , "
//								+ " sqltime		   TIMESTAMP   DEFAULT CURRENT_TIMESTAMP   NOT NULL, "
//								+ " calibrationquestionid			TEXT, "
//								+ " task_position  			INT);";
//
//						connection.query(sql);

			// relation between m RatingTasks and n Answers
			sql = "CREATE TABLE IF NOT EXISTS  findratingsfor"
					+ "(id TEXT PRIMARY 			KEY     NOT NULL,"
					+ " ratingtaskid		TEXT , "
					+ " sqltime		   TIMESTAMP   DEFAULT CURRENT_TIMESTAMP   NOT NULL, "
					+ " answerid			TEXT, "
					+ " answer_position  			INT);";

			connection.query(sql);
			//TODO
			// Controlquestion
						sql = "CREATE TABLE IF NOT EXISTS  controlquestion"
								+ "(id TEXT PRIMARY 			KEY     NOT NULL,"
								+ " question  TEXT, "
								+ " experimentid			TEXT);";

						connection.query(sql);

						//Controlanswer
						sql = "CREATE TABLE IF NOT EXISTS  controlanswer"
								+ "(id TEXT PRIMARY 			KEY     NOT NULL,"
								+ "iscorrect TEXT , "
								+ " sqltime		   TIMESTAMP   DEFAULT CURRENT_TIMESTAMP   NOT NULL, "
								+ " workerid TEXT, "
								+ " controlquestionid TEXT);";

						connection.query(sql);

						//PossibleControlanswer
						sql =  "CREATE TABLE IF NOT EXISTS  possiblecontrolanswer"
								+ "(id TEXT PRIMARY 			KEY     NOT NULL,"
								+ "controlquestionid TEXT , "
								+ "istrue TEXT , "
								+ "answer TEXT );";
						
						connection.query(sql);
						
						//Calibrationanswer
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
						
						connection.query(sql);
						
						//containsCalib
						sql =  "CREATE TABLE IF NOT EXISTS  containcalib"
								+ "(id TEXT PRIMARY 			KEY     NOT NULL,"
								+ "calibrationquestionid TEXT , "
								+ "experimentid TEXT );";
						
						connection.query(sql);
						
			//Calibrationquestion
						sql = "CREATE TABLE IF NOT EXISTS  calibrationquestion"
								+ "(id TEXT PRIMARY 			KEY     NOT NULL,"
								+ "question TEXT );";
						
						connection.query(sql);
						
						//RatingOptions
						sql = "CREATE TABLE IF NOT EXISTS  ratingoption"
								+ "(id TEXT PRIMARY KEY NOT NULL,"
								+ " text TEXT, "
								+ " value REAL, "
								+ " experimentid TEXT NOT NULL);";
						connection.query(sql);
						
						

		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		
		new PersistentCalibrationQuestionRepo(); //TODO Dirty fix. We should centralise repo building at some point
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.ExperimentRepo#createExperiment(java.lang.String)
	 */
	@Override
	public MutableExperiment createExperiment(String name, ConfigModel model) throws DatabaseException, IDAlreadyUsedException {
		if(model == null || name == null) {
			throw new DatabaseException("Tried to create an experiment without configmodel");
		}
		try {
			String sql = connection.formatString("SELECT * FROM experiment WHERE id = {?};", Value.fromString(name));
			if (connection.query(sql).iterator().hasNext()) {
				throw new IDAlreadyUsedException("experimentid already taken");
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
		ex.setConfig(model);
		ex.setRatingOptions(model.getRatingOptions());
		System.out.println(name + " created!");
		return ex;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.ExperimentRepo#loadExperiment(java.lang.String)
	 */
	@Override
	public MutableExperiment loadExperiment(String name) throws DatabaseException, IDNotFoundException {

		MutableExperiment ret = null;
		try {
			Logger.debug("************" +name);
			String sql = connection.formatString("SELECT * FROM experiment WHERE id = {?};", Value.fromString(name));
			Iterable<Iterable<Value>> persistentExperiments = connection.query(sql);
			if (persistentExperiments.iterator().hasNext()) {
				ret = new PersistentExperiment(name, this.connection);
			} else {
				throw new IDNotFoundException("requested Experiment does not exist");
			}

		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage() + "WELT");
		}
		return ret;
	}

	@Override
	public void deleteExperiment(String name) throws DatabaseException {
		try {
			//Delete the associated ConfigModel
			PersistentExperiment exp = new PersistentExperiment(name, this.connection);
			PersistentConfigModelRepo configRepo = new PersistentConfigModelRepo();
			Logger.debug("*************** " +exp.getConfig().getID());
			configRepo.deleteConfigModel(exp.getConfig().getID());

			// delete Stats
			String sql = connection.formatString("SELECT statsid FROM experiment WHERE id = {?};", Value.fromString(name));
			String tmpId = connection.query(sql).iterator().next().iterator().next().asString();
			sql = connection.formatString("DELETE FROM stats WHERE id = {?};", Value.fromString(tmpId));
			connection.query(sql);
			
			
			// delete RatingOptions done in config.delete
//			sql = connection.formatString("DELETE FROM ratingoption WHERE experimentid = {?};", Value.fromString(name));
//			connection.query(sql);

			/*-{?}|Anika|Philipp|c2|{?}*/
			// delete the creative tasks
			sql = connection.formatString("DELETE FROM creativetask WHERE experimentid = {?};", Value.fromString(name));
			connection.query(sql);
			// delete the rating tasks
			sql = connection.formatString("DELETE FROM ratingtask WHERE experimentid = {?};", Value.fromString(name));
			connection.query(sql);

			//delete from controlquests
			sql = connection.formatString("SELECT  id FROM controlquestion WHERE experimentid = {?};", Value.fromString(name));
			Iterable<Iterable<Value>> itrbl = connection.query(sql);
			for (Iterable<Value> ass : itrbl) {
				String assId = ass.iterator().next().asString();
				exp.removeControlQuestion(assId);
			}
			sql =  connection.formatString("DELETE FROM controlquestion WHERE experimentid = {?};", Value.fromString(name));
			connection.query(sql);

			//delete from containcalib
			sql =  connection.formatString("DELETE FROM containcalib WHERE experimentid = {?};", Value.fromString(name));
			connection.query(sql);
			
			//delete from containcalibtask
			//overliquid sollte im configmodel gelöscht werden
//			sql =  connection.formatString("DELETE FROM containscalibtask WHERE experimentid = {?};", Value.fromString(name));
//			connection.query(sql);

			// for each Assignment and its TC
			sql = connection.formatString("SELECT id FROM assignment WHERE experimentid = {?};", Value.fromString(name));
			itrbl = connection.query(sql);
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
				// now delete the ratingoption
				sql = connection.formatString("DELETE FROM ratingoption WHERE experimentid = {?}", Value.fromString(name));
				connection.query(sql);

			}

			// now, finally, delete the experiment
			sql = connection.formatString("DELETE FROM experiment WHERE id = {?};", Value.fromString(name));
			connection.query(sql);
		} catch (SQLException ex) {
			throw new DatabaseException(ex.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.ExperimentRepo#loadAllExperiments()
	 */
	@Override
	public List<MutableExperiment> loadAllExperiments() throws DatabaseException {
		List<MutableExperiment> ret = new LinkedList<MutableExperiment>();
		try {
			String sql = "SELECT id FROM experiment";
			Iterable<Iterable<Value>> experimentNames = connection.query(sql);
			Iterator<Iterable<Value>> namesIterator = experimentNames.iterator();

			while (namesIterator.hasNext()) {
				String expName = namesIterator.next().iterator().next().asString();
				MutableExperiment exp = loadExperiment(expName);
				ret.add(exp);
			}
		} catch (SQLException | IDNotFoundException e) {
			throw new DatabaseException(e.getMessage());
		}
		//System.out.println(ret);
		return ret;
	}

}

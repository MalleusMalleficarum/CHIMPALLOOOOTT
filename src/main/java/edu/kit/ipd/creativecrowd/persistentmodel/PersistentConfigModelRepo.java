package edu.kit.ipd.creativecrowd.persistentmodel;

import java.sql.SQLException;

import edu.kit.ipd.creativecrowd.mutablemodel.ConfigModelRepo;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import edu.kit.ipd.creativecrowd.readablemodel.CalibrationQuestion;
import edu.kit.ipd.creativecrowd.readablemodel.ConfigModel;
import edu.kit.ipd.creativecrowd.readablemodel.ControlQuestion;
import edu.kit.ipd.creativecrowd.readablemodel.TypeOfTask;
import edu.kit.ipd.creativecrowd.readablemodel.Worker;
import edu.kit.ipd.chimpalot.util.GlobalApplicationConfig;
import edu.kit.ipd.chimpalot.util.Logger;
import edu.kit.ipd.creativecrowd.crowdplatform.PlatformIdentity;
import edu.kit.ipd.creativecrowd.database.DatabaseConnection;
import edu.kit.ipd.creativecrowd.database.SQLiteDatabaseConnection;
import edu.kit.ipd.creativecrowd.database.Value;

/**
 * @see edu.kit.ipd.creativecrowd.mutablemodel.ConfigModelRepo
 * @author Pascal Gabriel & Thomas Friedel
 *
 */
public class PersistentConfigModelRepo implements ConfigModelRepo {
	
	/**
	 * Signals that a ConfigModel is used by an experiment.
	 */
	public static String IDPREFIX = "CONFIGFOREXP_";
	
	/** The connection to the database */
	DatabaseConnection connection;
	
	/**
	 * Instantiates a new persistent configmodel repository.
	 * Builds up the tables if needed.
	 *
	 * @throws DatabaseException if any of the SQL commands fails
	 */
	public PersistentConfigModelRepo() throws DatabaseException {
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
			//ConfigModel
			String sql = "CREATE TABLE IF NOT EXISTS configmodel"
					+ "(id TEXT PRIMARY KEY NOT NULL,"
					+ " experimentid TEXT,"
					+ " taskquestion TEXT,"
					+ " ratingtaskquestion TEXT,"
					+ " taskdescription TEXT,"
					+ " tasktitle TEXT,"
					+ " pictureurl TEXT,"
					+ " sourceurl TEXT,"
					+ " maxcreativetask INT,"
					+ " maxratingtask INT,"
					+ " budget INT,"				//evtl auch REAL, kommt auf einheit (euro oder cent an)
					+ " sendcreativetomt INT,"	//boolean als 0 bzw 1 repräsentiert
					+ " sendcreativetopb INT,"
					+ " sendratingtomt INT,"
					+ " sendratingtopb INT,"
					+ " basicpaymentmt INT,"	//in cent
					+ " basicpaymentpb INT,"
					+ " paymentpertaskcrmt INT,"
					+ " paymentpertaskcrpb INT,"
					+ " paymentpertaskramt INT,"
					+ " paymentpertaskrapb INT,"
					+ " experimenttype TEXT,"
					+ " totaltaskcountthreshold INT,"
					+ " averageratingthreshold REAL,"
					+ " evaluationtype TEXT);";
			connection.query(sql);

			// ConfigModelTag (oder tasktags TEXT mit kommata getrennt)
			sql = "CREATE TABLE IF NOT EXISTS  configmodeltag"
					+ "(id TEXT PRIMARY KEY     NOT NULL,"
					+ " text	   TEXT , "
					+ " configmodelid   TEXT     NOT NULL); ";
			connection.query(sql);

			// ConfigModelStrategy
			sql = "CREATE TABLE IF NOT EXISTS  configmodelstrategy"
					+ "(id TEXT PRIMARY KEY     NOT NULL,"
					+ " key	  	 TEXT , "
					+ " value    TEXT, "
					+ " configmodelid TEXT	   NOT NULL);";
			connection.query(sql);
			
			// ConfigModelCalibQuestions (saves the ids of the associated calibquestions)
			sql = "CREATE TABLE IF NOT EXISTS  configmodelcalibquestions"
					+ "(id TEXT     NOT NULL,"
					+ " configmodelid TEXT	   NOT NULL,"
					+ " PRIMARY KEY (id, configmodelid));";
			connection.query(sql);
			
			// ConfigModelControlQuestions (saves thecontrolquestions of each configmodel)
			sql = "CREATE TABLE IF NOT EXISTS  configmodelcontrolquestions"
					+ "(id TEXT PRIMARY KEY     NOT NULL,"
					+ " configmodelid TEXT	   NOT NULL);";
			connection.query(sql);
			
			//Qualifications for Mturk
			sql = "CREATE TABLE IF NOT EXISTS configmodelqualificationmt"
					+ "(id TEXT PRIMARY KEY NOT NULL,"
					+ "qualification TEXT,"
					+ "configmodelid TEXT NOT NULL);";
			connection.query(sql);
			
			//Qualifications for PyBossa
			sql = "CREATE TABLE IF NOT EXISTS configmodelqualificationpb"
					+ "(id TEXT PRIMARY KEY NOT NULL,"
					+ "qualification TEXT,"
					+ "configmodelid TEXT NOT NULL);";
			connection.query(sql);
			
			//ConfigModelRatingOptions
			sql = "CREATE TABLE IF NOT EXISTS  ratingoptionconfigmodel"
					+ "(id TEXT PRIMARY KEY NOT NULL,"
					+ " text TEXT, "
					+ " value REAL, "
					+ " configmodelid TEXT NOT NULL);";
			connection.query(sql);
			
			//For an experiment blocked workers
			sql = "CREATE TABLE IF NOT EXISTS configmodelblockedworkers"
					+ "(configid TEXT NOT NULL,"
					+ "workerid TEXT NOT NULL,"
					+ "PRIMARY KEY (configid, workerid));";
			connection.query(sql);
				
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.ConfigModel#createConfigModel(java.lang.String)
	 */
	@Override
	public ConfigModel createConfigModel(ConfigModel conf, String name, String expid) throws DatabaseException, IDAlreadyUsedException {
		try {
			Logger.debug("NAME: " + name);
			String sql = connection.formatString("SELECT * FROM configmodel WHERE id = {?};", Value.fromString(name));
			if (connection.query(sql).iterator().hasNext()) {
				
				throw new IDAlreadyUsedException("configmodelid already taken" + "WELTHERRSCHER");
			}

			sql = connection.formatString("INSERT INTO configmodel (id) VALUES ({?});", Value.fromString(name));
			connection.query(sql);

		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		PersistentConfigModel perConf = new PersistentConfigModel(name, this.connection);
		perConf.setExperimentID(expid);
		perConf.setTaskQuestion(conf.getTaskQuestion());
		perConf.setRatingTaskQuestion(conf.getRatingTaskQuestion());
		perConf.setTaskDescription(conf.getTaskDescription());
		perConf.setTaskTitle(conf.getTaskTitle());
		if (conf.getTaskTags() != null) {
			for (String tag : conf.getTaskTags()) {
				perConf.addTaskTag(tag);
			}
		}
		perConf.setPictureURL(conf.getPictureURL());
		perConf.setTaskSourceURL(conf.getTaskSourceURL());
		if (conf.getCalibQuestions() != null) {
			for (CalibrationQuestion calib : conf.getCalibQuestions()) {
				perConf.addCalibQuestion(calib);
			}
		}
		if (conf.getControlQuestions() != null) {
			for (ControlQuestion control : conf.getControlQuestions()) {
				perConf.addControlQuestion(control);
			}
		}
		
		perConf.setMaxCreativeTask(conf.getMaxCreativeTask());
		perConf.setMaxRatingTask(conf.getMaxRatingTask());
		perConf.setBudget(conf.getBudget());
		//TODO use enums efficiently
		perConf.setSendCreativeTo(PlatformIdentity.MTurk, conf.getSendCreativeTo(PlatformIdentity.MTurk));
		perConf.setSendCreativeTo(PlatformIdentity.PyBossa, conf.getSendCreativeTo(PlatformIdentity.PyBossa));
		perConf.setSendRatingTo(PlatformIdentity.MTurk, conf.getSendRatingTo(PlatformIdentity.MTurk));
		perConf.setSendRatingTo(PlatformIdentity.PyBossa, conf.getSendRatingTo(PlatformIdentity.PyBossa));
		perConf.setBasicPayment(PlatformIdentity.MTurk, conf.getBasicPayment(PlatformIdentity.MTurk));
		perConf.setBasicPayment(PlatformIdentity.PyBossa, conf.getBasicPayment(PlatformIdentity.PyBossa));
		perConf.setPaymentPerTask(PlatformIdentity.MTurk, TypeOfTask.Creative,
				conf.getPaymentPerTask(PlatformIdentity.MTurk, TypeOfTask.Creative));
		perConf.setPaymentPerTask(PlatformIdentity.MTurk, TypeOfTask.Rating,
				conf.getPaymentPerTask(PlatformIdentity.MTurk, TypeOfTask.Rating));
		perConf.setPaymentPerTask(PlatformIdentity.PyBossa, TypeOfTask.Creative,
				conf.getPaymentPerTask(PlatformIdentity.PyBossa, TypeOfTask.Creative));
		perConf.setPaymentPerTask(PlatformIdentity.PyBossa, TypeOfTask.Rating,
				conf.getPaymentPerTask(PlatformIdentity.PyBossa, TypeOfTask.Rating));
		perConf.setQualifications(conf.getQualifications(PlatformIdentity.MTurk), PlatformIdentity.MTurk);
		perConf.setQualifications(conf.getQualifications(PlatformIdentity.PyBossa), PlatformIdentity.PyBossa);
		perConf.setEvaluationType(conf.getEvaluationType());
		perConf.setStrategy(conf.getStrategy());
		perConf.setExperimentType(conf.getExperimentType().name());
		if (conf.getBlockedWorkers() != null) {
			for (Worker blocked : conf.getBlockedWorkers()) {
				perConf.addBlockedWorker(blocked);
			}
		}
		if (conf.getRatingOptions() != null) {
			perConf.setRatingOptions(conf.getRatingOptions());
		}
		return perConf;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.ConfigModel#loadConfigModel(java.lang.String)
	 */
	@Override
	public ConfigModel loadConfigModel(String name) throws DatabaseException, IDNotFoundException {
		ConfigModel ret = null;
		try {
			String sql = connection.formatString("SELECT * FROM configmodel WHERE id = {?};", Value.fromString(name));
			Iterable<Iterable<Value>> persistentConfigModels = connection.query(sql);
			if (persistentConfigModels.iterator().hasNext()) {
				ret = new PersistentConfigModel(name, this.connection);
			} else {
				throw new IDNotFoundException("requested configmodel does not exist");
			}

		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.ConfigModel#deleteConfigModel(java.lang.String)
	 */
	@Override
	public void deleteConfigModel(String name) throws DatabaseException {
		try {
			// delete Tags
			String sql = connection.formatString("DELETE FROM configmodeltag WHERE configmodelid = {?};", Value.fromString(name));
			connection.query(sql);
			// delete Strategy
			sql = connection.formatString("DELETE FROM configmodelstrategy WHERE configmodelid = {?};", Value.fromString(name));
			connection.query(sql);
			// delete association to CalibQuestions
			sql = connection.formatString("DELETE FROM configmodelcalibquestions WHERE configmodelid = {?};", Value.fromString(name));
			connection.query(sql);
			// delete associated ControlQuestions
			sql = connection.formatString("DELETE FROM configmodelcontrolquestions WHERE configmodelid = {?};", Value.fromString(name));
			connection.query(sql);
			// delete the ConfigModel
			sql = connection.formatString("DELETE FROM configmodel WHERE id = {?};", Value.fromString(name));
			connection.query(sql);
			//delete references in confiigmodelqualificationmt
			sql = connection.formatString("DELETE FROM configmodelqualificationmt WHERE configmodelid = {?};", Value.fromString(name));
			connection.query(sql);
			sql = connection.formatString("DELETE FROM configmodelqualificationpb WHERE configmodelid = {?};", Value.fromString(name));
			connection.query(sql);
			sql = connection.formatString("DELETE FROM ratingoptionconfigmodel WHERE configmodelid = {?}", Value.fromString(name));
			connection.query(sql);
			sql = connection.formatString("DELETE FROM configmodelblockedworkers WHERE configid = {?}",
					Value.fromString(name));
			connection.query(sql);
			
		} catch (SQLException ex) {
			throw new DatabaseException(ex.getMessage());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.ConfigModelRepo#loadAllConfigModels()
	 */
	@Override
	public List<ConfigModel> loadAllConfigModels() throws DatabaseException {
		List<ConfigModel> ret = new LinkedList<ConfigModel>();
		try {
			String sql = "SELECT id FROM configmodel";
			Iterable<Iterable<Value>> configIds = connection.query(sql);
			Iterator<Iterable<Value>> namesIterator = configIds.iterator();
		
			while (namesIterator.hasNext()) {
				String confID = namesIterator.next().iterator().next().asString();
				ConfigModel conf = loadConfigModel(confID);
				ret.add(conf);
			}
		} catch (SQLException | IDNotFoundException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ret;
	}

}

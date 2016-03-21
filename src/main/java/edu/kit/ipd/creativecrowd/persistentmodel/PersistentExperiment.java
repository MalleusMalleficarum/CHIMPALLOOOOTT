package edu.kit.ipd.creativecrowd.persistentmodel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import edu.kit.ipd.creativecrowd.crowdplatform.PlatformIdentity;
import edu.kit.ipd.creativecrowd.crowdplatform.WorkerId;
import edu.kit.ipd.creativecrowd.database.DatabaseConnection;
import edu.kit.ipd.creativecrowd.database.Value;
import edu.kit.ipd.chimpalot.util.Logger;
import edu.kit.ipd.creativecrowd.crowdplatform.AssignmentId;
import edu.kit.ipd.creativecrowd.mutablemodel.ConfigModelRepo;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAssignment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableCalibrationQuestion;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableControlQuestion;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableCreativeTask;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRatingTask;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableStats;
import edu.kit.ipd.creativecrowd.readablemodel.ConfigModel;
import edu.kit.ipd.creativecrowd.readablemodel.ExperimentType;
import edu.kit.ipd.creativecrowd.readablemodel.RatingOption;
import edu.kit.ipd.creativecrowd.readablemodel.TypeOfTask;
import edu.kit.ipd.creativecrowd.readablemodel.Worker;

/**
 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment
 * @see edu.kit.ipd.creativecrowd.readablemodel.Experiment
 * @author Philipp & Alexis
 */
class PersistentExperiment implements MutableExperiment {

	/** The unique name of an experiment. */
	private String name;

	/** The connection to the database. */
	private DatabaseConnection connection;

	/**
	 * Instantiates a new persistent experiment.
	 * This does NOT change anything at all in the Database.
	 * So if you want to put a new shiny experiment in your Database, then this constructor DOES NOT HELP YOU AT ALL.
	 *
	 * @param id, a unique id of a persistent experiment
	 * @param connection, a connection to the database
	 */
	PersistentExperiment(String id, DatabaseConnection connection) {
		this.connection = connection;
		this.name = id;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment#getStats()
	 */
	@Override
	public MutableStats getStats() throws DatabaseException {
		MutableStats stats;
		try {
			String sql = connection.formatString("SELECT statsid FROM experiment WHERE id = {?};", Value.fromString(name));
			stats = new PersistentStats(connection.query(sql).iterator().next().iterator().next().asString(), connection);
		} catch (SQLException e) {
			throw new Error(e.getMessage());
		} catch (NoSuchElementException ex) {
			throw new DatabaseException(ex.getMessage() + "  no existing stats for this experiment");
		}
		return stats;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment#addAssignment()
	 */
	@Override
	public MutableAssignment addAssignment() throws DatabaseException {
		String assid = connection.generateID("assignment");
		try {
			String sql = connection.formatString("INSERT INTO assignment (id,experimentid,is_paid) VALUES ({?},{?},0);", Value.fromString(assid), Value.fromString(name));
			connection.query(sql);
			// create empty paymentoutcome
			sql = connection.formatString("INSERT INTO paymentoutcome (id,assignmentid,receives_base_payment,bonus_amount) VALUES ({?}, {?}, 0,0);",
					Value.fromString(connection.generateID("paymentoutcome")), Value.fromString(assid));
			connection.query(sql);

			// create empty taskconstellation
			String taskconstellationid = connection.generateID("taskconstellation");
			sql = connection.formatString("INSERT INTO taskconstellation (id,assignmentid) VALUES ({?},{?});", Value.fromString(taskconstellationid), Value.fromString(assid));
			connection.query(sql);

		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage() + " an error occured creating the assignment");
		}
		PersistentAssignment assignment = new PersistentAssignment(assid, connection);
		return assignment;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment#getMutableAssignments()
	 */
	@Override
	public Iterable<MutableAssignment> getAssignments() throws DatabaseException {
		List<MutableAssignment> assignments = new ArrayList<MutableAssignment>();
		try {
			String sql = connection.formatString("SELECT id FROM assignment WHERE experimentid = {?};", Value.fromString(name));
			for (Iterable<Value> row : connection.query(sql)) {
				assignments.add(new PersistentAssignment(row.iterator().next().asString(), this.connection));
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return assignments;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment#getMutableAssignment(java.lang.String)
	 */
	@Override
	public MutableAssignment getAssignment(String not_mturkassid) throws DatabaseException {
		MutableAssignment ret = null;
		try {
			String sql = connection.formatString("SELECT id FROM assignment WHERE id = {?};", Value.fromString(not_mturkassid));
			if (!connection.query(sql).iterator().hasNext()) {
				throw new DatabaseException(" Assignment with this id doesn't exist");
			}
			else {
				ret = new PersistentAssignment(not_mturkassid, connection);
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ret;
	}

	@Override
	public MutableAssignment getAssignmentWithMturkId(AssignmentId mturkassid) throws DatabaseException {
		if(mturkassid == null) {
			throw new DatabaseException("Sorry, but an ID should be specified");
		}
		MutableAssignment ret = null;
		try {
			String sql = connection.formatString("SELECT id FROM assignment WHERE experimentid = {?} AND mturkid = {?}", Value.fromString(name), Value.fromString(mturkassid.getId()));
			Iterable<Iterable<Value>> rows = connection.query(sql);
			String internassid;
			if (rows.iterator().hasNext()) {
				internassid = rows.iterator().next().iterator().next().asString();
				ret = new PersistentAssignment(internassid, connection);
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment#getCreativeTask()
	 */
	@Override
	public MutableCreativeTask getCreativeTask() throws DatabaseException {
		MutableCreativeTask ret = null;
		try {
			String sql = connection.formatString("SELECT id FROM creativetask WHERE experimentid = {?};", Value.fromString(name));
			Iterator<Iterable<Value>> it = connection.query(sql).iterator();
			if (!it.hasNext()) {
				throw new DatabaseException(" Creative Task has not been set yet");
			}
			else {
				ret = new PersistentCreativeTask(it.next().iterator().next().asString(), connection);
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment#addRatingTask()
	 */
	@Override
	public MutableRatingTask addRatingTask() throws DatabaseException {
		String ratingtaskid = connection.generateID("ratingtask");
		try {
			String sql = connection.formatString("INSERT INTO ratingtask (id,experimentid) VALUES ({?},{?});", Value.fromString(ratingtaskid), Value.fromString(name));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage() + " An error occured creating the RatingTask");
		}
		PersistentRatingTask ratingTask = new PersistentRatingTask(ratingtaskid, connection);
		return ratingTask;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment#getRatingTasks()
	 */
	@Override
	public Iterable<? extends MutableRatingTask> getRatingTasks() throws DatabaseException {
		//TODO only used by WorIntController, not sure if necessary. If not delete. ~Thomas
		
		// TODO Vereinheitlichen der return Werte, ob wir extra Parameter haben wollen oder nicht
		// TODO ExceptionHandling verbessern
		/*-?|Test Repo-Review|MainUser|c0|?*/
		List<MutableRatingTask> ratingTasks = new ArrayList<MutableRatingTask>();
		try {
			String sql = connection.formatString("SELECT id FROM ratingtask WHERE experimentid = {?};", Value.fromString(name));
			for (Iterable<Value> row : connection.query(sql)) {
				ratingTasks.add(new PersistentRatingTask(row.iterator().next().asString(), this.connection));
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ratingTasks;
	}



	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment#markAsFinished()
	 */
	@Override
	public void markAsFinished() throws DatabaseException {
		try {
			String sql = connection.formatString("UPDATE experiment SET finished = 1 WHERE id = {?};", Value.fromString(name));
			connection.query(sql);
		} catch (SQLException e) {
			// there is no reason to throw an exception
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment#addCreativeTask()
	 */
	@Override
	public MutableCreativeTask addCreativeTask() throws DatabaseException {
		String creativetaskid = connection.generateID("creativetask");
		try {
			String sql = connection.formatString("INSERT INTO creativetask (id,experimentid) VALUES ({?},{?});", Value.fromString(creativetaskid), Value.fromString(name));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		MutableCreativeTask creativetask = new PersistentCreativeTask(creativetaskid, connection);
		return creativetask;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment#markExperimentAsSoftFinished()
	 */
	@Override
	public void markExperimentAsSoftFinished() throws DatabaseException { //TODO not used. DELETE?
		try {
			String sql = connection.formatString("UPDATE experiment SET softfinished = 1 WHERE id = {?};", Value.fromString(name));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.AbstractExperiment#getID()
	 */
	@Override
	public String getID() {
		return this.name;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.name;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		return this.name.equals(obj.toString()); //WTF?

	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.AbstractExperiment#getDescription()
	 */
	@Override
	public String getDescription() throws DatabaseException {
		return this.getConfig().getTaskDescription();
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.AbstractExperiment#getTags()
	 */
	@Override
	public Iterable<String> getTags() throws DatabaseException {
		List<String> ret = new ArrayList<String>();
		for (String tag : this.getConfig().getTaskTags()) {
			ret.add(tag);
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.AbstractExperiment#getQualifications()
	 */
	@Override
	public Iterable<String> getQualifications(PlatformIdentity platform) throws DatabaseException {
		return this.getConfig().getQualifications(platform);
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.AbstractExperiment#getStrategyParams()
	 */
	@Override
	public Map<String, String> getStrategyParams() throws DatabaseException {
		return this.getConfig().getStrategy();
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.AbstractExperiment#getBudgetCents()
	 */
	@Override
	public int getBudgetCents() throws DatabaseException {
		return this.getConfig().getBudget();
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.AbstractExperiment#getBasicPaymentHITCents()
	 */
	@Override
	public int getBasicPaymentHITCents(PlatformIdentity platform) throws DatabaseException {
		return this.getConfig().getBasicPayment(platform);
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.AbstractExperiment#getBasicPaymentAnswerCents()
	 */
	@Override
	public int getBasicPaymentAnswerCents(PlatformIdentity platform) throws DatabaseException {
		return this.getConfig().getPaymentPerTask(platform, TypeOfTask.Creative);
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.AbstractExperiment#getBasicPaymentRatingCents()
	 */
	@Override
	public int getBasicPaymentRatingCents(PlatformIdentity platform) throws DatabaseException {
		return this.getConfig().getPaymentPerTask(platform, TypeOfTask.Rating);
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.AbstractExperiment#getHITTitle()
	 */
	@Override
	public String getHITTitle() throws DatabaseException {
		return this.getConfig().getTaskTitle();
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.AbstractExperiment#getHITDescription()
	 */
	@Override
	public String getHITDescription() throws DatabaseException {
		return this.getConfig().getTaskDescription();
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment#getHITID()
	 */
	@Override
	public String getHitID() throws DatabaseException {
		String result = null;
		try {
			String sql = connection.formatString("SELECT hit_mturkid FROM experiment WHERE id = {?};", Value.fromString(name));
			Iterable<Iterable<Value>> hitDescription = connection.query(sql);
			result = hitDescription.iterator().next().iterator().next().asString();
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment#setHitID(java.lang.String)
	 */
	@Override
	public void setHitID(String id) throws DatabaseException {
		try {
			String sql = connection.formatString("UPDATE experiment SET hit_mturkid = {?} WHERE id = {?};", Value.fromString(id), Value.fromString(name));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}
	 
	@Override
	public int getMaxNumberOfAnswersPerAssignment() throws DatabaseException {
		return this.getConfig().getMaxCreativeTask();
	}

	@Override
	public int getMaxNumberOfRatingsPerAssignment() throws DatabaseException {
		return this.getConfig().getMaxRatingTask();
	}

	@Override
	public boolean isFinished() throws DatabaseException {
		boolean result = false;
		try {
			String sql = connection.formatString("SELECT finished FROM experiment WHERE id = {?};", Value.fromString(name));
			Iterable<Iterable<Value>> maxRatingsPerAssignment = connection.query(sql);
			result = (maxRatingsPerAssignment.iterator().next().iterator().next().asInt() == 1);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return result;
	}

	@Override
	public String getRatingTaskViewClass() throws DatabaseException {
		return this.getConfig().getEvaluationType();
	}
	
	@Override
	public Iterable<? extends MutableControlQuestion> getControlQuestions() throws DatabaseException {
		List<MutableControlQuestion> cqs = new ArrayList<MutableControlQuestion>();
		try {
			String sql = connection.formatString("SELECT  id FROM controlquestion WHERE experimentid = {?};", Value.fromString(name));
			for (Iterable<Value> rows : connection.query(sql)) {
				cqs.add( new PersistentControlQuestion( this.connection,rows.iterator().next().asString()));
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return cqs;
	}
	
	@Override
	public MutableControlQuestion addControlQuestion() throws DatabaseException {
		String controlquestionid = connection.generateID("controlquestion");
		try {
			String sql = connection.formatString("INSERT INTO controlquestion (id,experimentid) VALUES ({?},{?});", Value.fromString(controlquestionid), Value.fromString(name));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage() + " An error occured creating the ControlQuestion");
		}
		return new PersistentControlQuestion(this.connection, controlquestionid);
	}

	@Override
	public Iterable<? extends MutableCalibrationQuestion> getCalibrationQuestions() throws DatabaseException {
		List<MutableCalibrationQuestion> cqs = new ArrayList<MutableCalibrationQuestion>();
		
		try {
			String sql = connection.formatString("SELECT id FROM configmodelcalibquestions WHERE configmodelid = {?}", Value.fromString(this.getConfig().getID()));
			for (Iterable<Value> rows : connection.query(sql)) {
				cqs.add(new PersistentCalibrationQuestion( rows.iterator().next().asString(),this.connection));
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return cqs;
	}



	@Override
	public MutableCalibrationQuestion addCalibrationQuestion() throws DatabaseException {
		String calibquestid  = connection.generateID("calibrationquestion");
		String containcalibid = connection.generateID("containcalib");
		try {
			String sql = "";
			List<Value> args = new ArrayList<Value>();
			args.add(Value.fromString(containcalibid));
			args.add(Value.fromString(name));
			args.add(Value.fromString(calibquestid));
			sql = connection.formatString("INSERT INTO containcalib (id,experimentid, calibrationquestionid) VALUES ({?},{?},{?});", args);
			connection.query(sql);
			sql = connection.formatString("INSERT INTO containcalib (id,experimentid) VALUES ({?},{?});", Value.fromString(calibquestid), Value.fromString(name));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage() + " An error occured creating the CalibrationQuestion");
		}
		return new PersistentCalibrationQuestion(calibquestid, this.connection);
	}


	@Override
	public ConfigModel getConfig() throws DatabaseException {
		ConfigModel result;
		try {
			String sql = connection.formatString("SELECT config FROM experiment WHERE id = {?};", Value.fromString(name));
			result = new PersistentConfigModel(connection.query(sql).iterator().next().iterator().next().asString(), connection);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return result;
	}
	
	@Override
	public void setConfig(ConfigModel config) throws DatabaseException {
		ConfigModelRepo configrepo = new PersistentConfigModelRepo();

		ConfigModel temp = configrepo.createConfigModel(config, PersistentConfigModelRepo.IDPREFIX + name, name);

		PersistentConfigModel perConf = new PersistentConfigModel(temp.getID(), connection);
		perConf.setExperimentID(name);
		
		try {
			String sql = connection.formatString("UPDATE experiment SET config = {?} WHERE id = {?};",
					Value.fromString(perConf.getID()), Value.fromString(name));
			connection.query(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DatabaseException(e.getMessage());
		}
	}

	@Override
	public void removeControlQuestion(String quest) throws DatabaseException {
		String sql = "";
		try {
				sql = connection.formatString("DELETE FROM controlanswer WHERE controlquestionid ={?};", Value.fromString(quest));
				connection.query(sql);
				sql = connection.formatString("DELETE FROM possiblecontrolanswer WHERE controlquestionid ={?};", Value.fromString(quest));
				connection.query(sql);
				 sql = connection.formatString("DELETE FROM controlquestion WHERE id ={?}  AND experimentid ={?};", Value.fromString(quest), Value.fromString(name));
			connection.query(sql);
			sql = connection.formatString("DELETE FROM containscontrol WHERE controlquestionid ={?};", Value.fromString(quest));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	@Override
	public void removeCalibrationQuestion(String quest) throws DatabaseException { //TODO not used. DELETE?
		try {
			String sql = connection.formatString("DELETE FROM containcalib WHERE calibrationquestionid ={?}  AND experimentid ={?};", Value.fromString(quest), Value.fromString(name));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	@Override
	public ExperimentType getType() throws DatabaseException {
		return this.getConfig().getExperimentType();
	}
	
	/**
	 * Sets the rating options.
	 * @param ratingOptions list of rating options
	 * @return {@code true} if successful, {@code false} if input was malformed
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public boolean setRatingOptions(Map<String, Float> ratingOptions) throws DatabaseException {
		if (ratingOptions == null) {
			return false;
		}
		try {
			for (Map.Entry<String, Float> entry : ratingOptions.entrySet()) {
				
				String optionId = connection.generateID("ratingoption");
				List<Value> args = new ArrayList<Value>();
				args.add(Value.fromString(optionId));
				args.add(Value.fromString(entry.getKey()));
				args.add(Value.fromFloat(entry.getValue()));
				args.add(Value.fromString(this.name));
				String sql = connection.formatString("INSERT INTO ratingoption (id,text,value,experimentid) VALUES ({?},{?},{?},{?});", args);
				Logger.debug("SQL: " + sql);
				connection.query(sql);
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.ConfigModel#getRatingOptions()
	 */
	@Override
	public Iterable<RatingOption> getRatingOptions() throws DatabaseException {
		List<RatingOption> result = new LinkedList<RatingOption>();
		try {
			String sql = connection.formatString("SELECT id FROM ratingoption WHERE experimentid = {?};",
					Value.fromString(name)); 
			Iterable<Iterable<Value>> ratingOptionIds = connection.query(sql);
			Iterator<Iterable<Value>> ratingOptionIterator = ratingOptionIds.iterator();
			Logger.debug(ratingOptionIds.toString());
			Logger.debug(ratingOptionIterator.toString());
		
			while (ratingOptionIterator.hasNext()) {
				String ratingOptionId = ratingOptionIterator.next().iterator().next().asString();
				Logger.debug("THE REQUEST ID " + ratingOptionId);
				RatingOption ratingOption = new PersistentRatingOption(ratingOptionId, this.connection);
				result.add(ratingOption);
			}
			
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return result;
	}


	@Override
	public Iterable<WorkerId> getBlockedWorkers() throws DatabaseException {
		ArrayList<WorkerId> result = new ArrayList<WorkerId>();
		for (Worker wrk : this.getConfig().getBlockedWorkers()) {
			result.add(new WorkerId(wrk.getID()));
		}
		return result;
	}
}
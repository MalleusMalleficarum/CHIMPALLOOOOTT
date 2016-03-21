package edu.kit.ipd.creativecrowd.persistentmodel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import edu.kit.ipd.creativecrowd.database.DatabaseConnection;
import edu.kit.ipd.creativecrowd.database.Value;
import edu.kit.ipd.creativecrowd.mturk.AssignmentId;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAssignment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableCreativeTask;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRatingOption;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRatingTask;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableStats;

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
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment#addRatingOption()
	 */
	@Override
	public MutableRatingOption addRatingOption() throws DatabaseException {
		String ratingoptionid = connection.generateID("ratingoption");
		try {
			String sql = connection.formatString("INSERT INTO ratingoption (id,experimentid) VALUES ({?},{?});", Value.fromString(ratingoptionid), Value.fromString(name));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage() + " An error occured creating the RatingOption");
		}
		return new PersistentRatingOption(ratingoptionid, this.connection);
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment#getRatingOptions()
	 */
	@Override
	public Iterable<MutableRatingOption> getRatingOptions() throws DatabaseException {
		List<MutableRatingOption> ratingOptions = new ArrayList<MutableRatingOption>();
		try {
			String sql = connection.formatString("SELECT id FROM ratingoption WHERE experimentid = {?};", Value.fromString(name));
			for (Iterable<Value> row : connection.query(sql)) {
				ratingOptions.add(new PersistentRatingOption(row.iterator().next().asString(), this.connection));
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ratingOptions;
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
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment#setDescription(java.lang.String)
	 */
	@Override
	public void setDescription(String description) throws DatabaseException {
		try {
			String sql = connection.formatString("UPDATE experiment SET description = {?} WHERE id = {?};", Value.fromString(description), Value.fromString(name));
			connection.query(sql);
		} catch (SQLException e) {
			// there is no reason to throw an exception
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment#setBudget(int)
	 */
	@Override
	public void setBudget(int cents) throws DatabaseException {
		try {
			String sql = connection.formatString("UPDATE experiment SET budget = {?} WHERE id = {?};", Value.fromInt(cents), Value.fromString(name));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment#setBasicPayment(int)
	 */
	@Override
	public void setBasicPaymentHIT(int cents) throws DatabaseException {
		try {
			String sql = connection.formatString("UPDATE experiment SET basic_payment = {?} WHERE id = {?};", Value.fromInt(cents), Value.fromString(name));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment#setCreativeTaskPayment(int)
	 */
	@Override
	public void setBasicPaymentAnswer(int cents) throws DatabaseException {
		try {
			String sql = connection.formatString("UPDATE experiment SET answer_payment = {?} WHERE id = {?};", Value.fromInt(cents), Value.fromString(name));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment#setRatingTaskPayment(int)
	 */
	@Override
	public void setBasicPaymentRating(int cents) throws DatabaseException {
		try {
			String sql = connection.formatString("UPDATE experiment SET rating_payment = {?} WHERE id = {?};", Value.fromInt(cents), Value.fromString(name));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
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
	public void markExperimentAsSoftFinished() throws DatabaseException {
		try {
			String sql = connection.formatString("UPDATE experiment SET softfinished = 1 WHERE id = {?};", Value.fromString(name));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment#setBonusPayment(int)
	 */
	@Override
	public void setBonusPayment(int cents) throws DatabaseException {
		try {
			String sql = connection.formatString("UPDATE experiment SET bonus_payment = {?} WHERE id = {?};", Value.fromInt(cents), Value.fromString(name));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment#setStrategyParams(java.util.Map)
	 */
	@Override
	public void setStrategyParams(Map<String, String> params) throws DatabaseException {

		// for each param
		for (Map.Entry<String, String> entry : params.entrySet()) {

			// create id and prepare formatString- Iterable
			String strategyparamid = connection.generateID("strategyparam");
			List<Value> sqlArgs = new ArrayList<Value>();
			sqlArgs.add(Value.fromString(strategyparamid));
			sqlArgs.add(Value.fromString(entry.getKey()));
			sqlArgs.add(Value.fromString(entry.getValue()));
			sqlArgs.add(Value.fromString(name));

			// insert into database
			try {
				String sql = connection.formatString("INSERT INTO strategyparam (id,key,value,experimentid) VALUES ({?},{?},{?},{?});", sqlArgs);
				connection.query(sql);
			} catch (SQLException e) {
				throw new DatabaseException(e.getMessage());
			}
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
		return this.name.equals(obj.toString());

	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.AbstractExperiment#getDescription()
	 */
	@Override
	public String getDescription() throws DatabaseException {
		String result = null;
		try {
			String sql = connection.formatString("SELECT description FROM experiment WHERE id = {?};", Value.fromString(name));
			Iterable<Iterable<Value>> descriptions = connection.query(sql);
			result = descriptions.iterator().next().iterator().next().asString();
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.AbstractExperiment#getTags()
	 */
	@Override
	public Iterable<String> getTags() throws DatabaseException {
		List<String> ret = new ArrayList<String>();
		try {
			String sql = connection.formatString("SELECT text FROM tag WHERE experimentid = {?};", Value.fromString(name));
			for (Iterable<Value> tag : connection.query(sql)) {
				ret.add(tag.iterator().next().asString());
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.AbstractExperiment#getQualifications()
	 */
	@Override
	public Iterable<String> getQualifications() throws DatabaseException {
		List<String> ret = new ArrayList<String>();
		try {
			String sql = connection.formatString("SELECT text FROM qualification WHERE experimentid = {?};", Value.fromString(name));
			for (Iterable<Value> qualitext : connection.query(sql)) {
				ret.add(qualitext.iterator().next().asString());
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.AbstractExperiment#getStrategyParams()
	 */
	@Override
	public Map<String, String> getStrategyParams() throws DatabaseException {
		Map<String, String> ret = new HashMap<String, String>();
		try {
			String sql = connection.formatString("SELECT key,value FROM strategyparam WHERE experimentid = {?};", Value.fromString(name));
			for (Iterable<Value> strategyparam : connection.query(sql)) {
				Iterator<Value> it = strategyparam.iterator();
				String key = it.next().asString();
				String val = it.next().asString();
				ret.put(key, val);
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.AbstractExperiment#getBudgetCents()
	 */
	@Override
	public int getBudgetCents() throws DatabaseException {
		int result = 0;
		try {
			String sql = connection.formatString("SELECT budget FROM experiment WHERE id = {?};", Value.fromString(name));
			Iterable<Iterable<Value>> budgets = connection.query(sql);
			result = budgets.iterator().next().iterator().next().asInt();
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.AbstractExperiment#getBasicPaymentHITCents()
	 */
	@Override
	public int getBasicPaymentHITCents() throws DatabaseException {
		int result = 0;
		try {
			String sql = connection.formatString("SELECT basic_payment FROM experiment WHERE id = {?};", Value.fromString(name));
			Iterable<Iterable<Value>> basicPayments = connection.query(sql);
			result = basicPayments.iterator().next().iterator().next().asInt();
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.AbstractExperiment#getBasicPaymentAnswerCents()
	 */
	@Override
	public int getBasicPaymentAnswerCents() throws DatabaseException {
		int result = 0;
		try {
			String sql = connection.formatString("SELECT answer_payment FROM experiment WHERE id = {?};", Value.fromString(name));
			Iterable<Iterable<Value>> answerPayments = connection.query(sql);
			result = answerPayments.iterator().next().iterator().next().asInt();
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.AbstractExperiment#getBasicPaymentRatingCents()
	 */
	@Override
	public int getBasicPaymentRatingCents() throws DatabaseException {
		int result = 0;
		try {
			String sql = connection.formatString("SELECT rating_payment FROM experiment WHERE id = {?};", Value.fromString(name));
			Iterable<Iterable<Value>> ratingPayments = connection.query(sql);
			result = ratingPayments.iterator().next().iterator().next().asInt();
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.AbstractExperiment#getBonusPaymentCents()
	 */
	@Override
	public int getBonusPaymentCents() throws DatabaseException {
		int result = 0;
		try {
			String sql = connection.formatString("SELECT bonus_payment FROM experiment WHERE id = {?};", Value.fromString(name));
			Iterable<Iterable<Value>> bonusPayments = connection.query(sql);
			result = bonusPayments.iterator().next().iterator().next().asInt();
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.AbstractExperiment#getHITTitle()
	 */
	@Override
	public String getHITTitle() throws DatabaseException {
		String result = null;
		try {
			String sql = connection.formatString("SELECT hit_title FROM experiment WHERE id = {?};", Value.fromString(name));
			Iterable<Iterable<Value>> hitTitles = connection.query(sql);
			result = hitTitles.iterator().next().iterator().next().asString();
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.AbstractExperiment#getHITDescription()
	 */
	@Override
	public String getHITDescription() throws DatabaseException {
		String result = null;
		try {
			String sql = connection.formatString("SELECT hit_description FROM experiment WHERE id = {?};", Value.fromString(name));
			Iterable<Iterable<Value>> hitDescriptions = connection.query(sql);
			result = hitDescriptions.iterator().next().iterator().next().asString();
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment#setHITTitle(java.lang.String)
	 */
	@Override
	public void setHITTitle(String title) throws DatabaseException {
		try {
			String sql = connection.formatString("UPDATE experiment SET hit_title = {?} WHERE id = {?};", Value.fromString(title), Value.fromString(name));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment#setHITDescription(java.lang.String)
	 */
	@Override
	public void setHITDescription(String description) throws DatabaseException {
		try {
			String sql = connection.formatString("UPDATE experiment SET hit_description = {?} WHERE id = {?};", Value.fromString(description), Value.fromString(name));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment#setTags(java.lang.Iterable)
	 */
	@Override
	public void setTags(Iterable<String> tags) throws DatabaseException {
		try {
			String sql = null;
			for (String tag : tags) {
				String tagId = connection.generateID("tag");
				List<Value> args = new ArrayList<Value>();
				args.add(Value.fromString(tagId));
				args.add(Value.fromString(tag));
				args.add(Value.fromString(this.name));
				sql = connection.formatString("INSERT INTO tag (id,text,experimentid) VALUES ({?},{?},{?});", args);
				connection.query(sql);
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}

	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment#setQualifications(java.lang.Iterable)
	 */
	@Override
	public void setQualifications(Iterable<String> qualifications) throws DatabaseException {
		try {
			String sql = null;
			for (String qualif : qualifications) {
				String quid = connection.generateID("qualification");
				List<Value> args = new ArrayList<Value>();
				args.add(Value.fromString(quid));
				args.add(Value.fromString(qualif));
				args.add(Value.fromString(this.name));
				sql = connection.formatString("INSERT INTO qualification (id,text,experimentid) VALUES ({?},{?},{?});", args);
				connection.query(sql);
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}

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

	@Override
	public int getMaxNumberOfAnswersPerAssignment() throws DatabaseException {
		int result = 0;
		try {
			String sql = connection.formatString("SELECT max_answers_per_assignment FROM experiment WHERE id = {?};", Value.fromString(name));
			Iterable<Iterable<Value>> maxAnswersPerAssignment = connection.query(sql);
			result = maxAnswersPerAssignment.iterator().next().iterator().next().asInt();
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return result;
	}

	@Override
	public void setMaxNumberOfRatingsPerAssignment(int maxRatings) throws DatabaseException {
		try {
			String sql = connection.formatString("UPDATE experiment SET max_ratings_per_assignment = {?} WHERE id = {?};", Value.fromInt(maxRatings), Value.fromString(name));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	@Override
	public void setMaxNumberOfAnswersPerAssignment(int maxAnswers) throws DatabaseException {
		try {
			String sql = connection.formatString("UPDATE experiment SET max_answers_per_assignment = {?} WHERE id = {?};", Value.fromInt(maxAnswers), Value.fromString(name));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	@Override
	public int getMaxNumberOfRatingsPerAssignment() throws DatabaseException {
		int result = 0;
		try {
			String sql = connection.formatString("SELECT max_ratings_per_assignment FROM experiment WHERE id = {?};", Value.fromString(name));
			Iterable<Iterable<Value>> maxRatingsPerAssignment = connection.query(sql);
			result = maxRatingsPerAssignment.iterator().next().iterator().next().asInt();
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return result;
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
		String result = null;
		try {
			String sql = connection.formatString("SELECT ratingviewclass FROM experiment WHERE id = {?};", Value.fromString(name));
			Iterable<Iterable<Value>> viewclass = connection.query(sql);
			result = viewclass.iterator().next().iterator().next().asString();
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return result;
	}

	@Override
	public void setRatingTaskViewClass(String ratingTaskViewClass) throws DatabaseException {
		try {
			String sql = connection.formatString("UPDATE experiment SET ratingviewclass = {?} WHERE id = {?};", Value.fromString(ratingTaskViewClass), Value.fromString(name));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

}
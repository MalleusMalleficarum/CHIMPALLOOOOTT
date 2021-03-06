package edu.kit.ipd.creativecrowd.persistentmodel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import edu.kit.ipd.chimpalot.util.Logger;
import edu.kit.ipd.creativecrowd.database.DatabaseConnection;
import edu.kit.ipd.creativecrowd.database.Value;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableCalibrationAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableCalibrationQuestion;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableControlAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableControlQuestion;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableCreativeTask;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRating;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRatingTask;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableTaskConstellation;
import edu.kit.ipd.creativecrowd.readablemodel.Task;

/**
 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableTaskConstellation
 * @see edu.kit.ipd.creativecrowd.readablemodel.TaskConstellation
 * @author Philipp + Alexis
 */
class PersistentTaskConstellation implements MutableTaskConstellation {

	/** The unique id of a persistent TaskConstellation. */
	private String id;

	/** The connection to the database. */
	private DatabaseConnection connection;

	/**
	 * Instantiates a new persistent task constellation.
	 *
	 * @param id, a unique id of a persistent task constellation
	 * @param connection, a connection to the database
	 */
	PersistentTaskConstellation(String id, DatabaseConnection connection) {
		this.connection = connection;
		this.id = id;
	}

	/*-{?}|Test Repo-Review|Philipp|c1|{?}*/
	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.TaskConstellation#getTasks()
	 */
	@Override
	public Iterable<Task> getTasks() throws DatabaseException {
		List<Task> ret = new ArrayList<Task>();
		try {
			String sql = connection.formatString("SELECT creativetaskid FROM containscreative WHERE taskconstellationid = {?};", Value.fromString(this.id));
			for (Iterable<Value> row : connection.query(sql)) {
				ret.add(new PersistentCreativeTask(row.iterator().next().asString(), connection));
			}
			sql = connection.formatString("SELECT ratingtaskid FROM containsevaluative WHERE taskconstellationid = {?};", Value.fromString(this.id));
			for (Iterable<Value> row : connection.query(sql)) {
				ret.add(new PersistentRatingTask(row.iterator().next().asString(), connection));
			}
			sql = connection.formatString("SELECT controlquestionid FROM containscontrol WHERE taskconstellationid = {?};", Value.fromString(this.id));
			for (Iterable<Value> row : connection.query(sql)) {
				ret.add(new PersistentControlQuestion(connection, row.iterator().next().asString()));
			}
			sql = connection.formatString("SELECT calibrationquestionid FROM containscalibtask WHERE taskconstellationid = {?};", Value.fromString(this.id));
			for (Iterable<Value> row : connection.query(sql)) {
				ret.add(new PersistentCalibrationQuestion(row.iterator().next().asString(), connection));
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ret;

	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.TaskConstellation#nextExists()
	 */
	@Override
	public boolean nextButtonExists() throws DatabaseException {
		boolean ret = false;
		try {
			String sql = connection.formatString("SELECT next FROM taskconstellation WHERE id = {?};", Value.fromString(this.id));
			if (connection.query(sql).iterator().next().iterator().next().asInt() == 1) {
				ret = true;
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());

		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.TaskConstellation#againExists()
	 */
	@Override
	public boolean againButtonExists() throws DatabaseException {
		boolean ret = false;
		try {
			String sql = connection.formatString("SELECT again FROM taskconstellation WHERE id = {?};", Value.fromString(this.id));
			if (connection.query(sql).iterator().next().iterator().next().asInt() == 1) {
				ret = true;
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());

		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.TaskConstellation#getCurrentTask()
	 */
	@Override
	public Task getCurrentTask() throws DatabaseException {
		String taskid = null;
		Task ret = null;;
		try {
			int current;
			String sql = connection
					//get taskconst id
					.formatString(
							"SELECT current_pos FROM taskconstellation WHERE id = {?};",
							Value.fromString(this.id));
			current = connection.query(sql).iterator().next().iterator().next().asInt();

			sql = connection
					.formatString(
							"SELECT ratingtaskid FROM containsevaluative WHERE task_position = {?} AND taskconstellationid = {?};",
							Value.fromInt(current), Value.fromString(id));
			if (!connection.query(sql).iterator().hasNext()) {
				sql = connection
						.formatString(
								"SELECT creativetaskid FROM containscreative WHERE task_position = {?} AND taskconstellationid = {?};",
								Value.fromInt(current), Value.fromString(this.id));
				if (!connection.query(sql).iterator().hasNext()) {
					
					if (!connection.query(sql).iterator().hasNext()) {
						sql = connection
								.formatString(
										"SELECT controlquestionid FROM containscontrol WHERE task_position = {?} AND taskconstellationid = {?};",
										Value.fromInt(current), Value.fromString(this.id));
						if (!connection.query(sql).iterator().hasNext()) {
							sql = connection
									.formatString(
											"SELECT calibrationquestionid FROM containscalibtask WHERE task_position = {?} AND taskconstellationid = {?};",
											Value.fromInt(current), Value.fromString(this.id));
							if (!connection.query(sql).iterator().hasNext()) {
								throw new DatabaseException("task with task_position " + current + " does not exist in task constellation " + this.toString());
							}
						}
						else {
							taskid = connection.query(sql).iterator().next().iterator().next().asString();
							ret = new PersistentCalibrationQuestion(taskid, connection);
						}
					}
					else{
						taskid = connection.query(sql).iterator().next().iterator().next().asString();
						ret = new PersistentControlQuestion(connection, taskid);
					}
				}
				else {
					taskid = connection.query(sql).iterator().next().iterator().next().asString();
					ret = new PersistentCreativeTask(taskid, connection);
				}
			}
			else {
				taskid = connection.query(sql).iterator().next().iterator().next().asString();
				ret = new PersistentRatingTask(taskid, connection);
			}

		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());

		}
		
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableTaskConstellation#setNext(boolean)
	 */
	@Override
	public void setNextButton(boolean next) throws DatabaseException {
		try {
			int intnext = next ? 1 : 0;
			String sql = connection.formatString(
					"UPDATE taskconstellation SET next = {?} WHERE id = {?};",
					Value.fromInt(intnext),
					Value.fromString(this.id));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableTaskConstellation#setAgain(boolean)
	 */
	@Override
	public void setAgainButton(boolean again) throws DatabaseException {
		try {
			int intAgain = again ? 1 : 0;
			String sql = connection.formatString("UPDATE taskconstellation SET again = {?} WHERE id = {?};", Value.fromInt(intAgain), Value.fromString(this.id));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableTaskConstellation#setCurrentTask(int)
	 */
	@Override
	public void setCurrentTask(int taskPosition) throws DatabaseException {
		try {
			String sql = connection.formatString(
					"UPDATE taskconstellation SET current_pos = {?} WHERE id = {?};",
					Value.fromInt(taskPosition),
					Value.fromString(this.id));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}

	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableTaskConstellation#getMutableCreativeTask(int)
	 */
	@Override
	public MutableCreativeTask getMutableCreativeTask(int taskPosition) throws DatabaseException {
		String taskid = null;
		try {
			String sql = connection.formatString("SELECT creativetaskid FROM containscreative WHERE task_position = {?} AND taskconstellationid = {?}; ", Value.fromInt(taskPosition),
					Value.fromString(this.id));
			taskid = connection.query(sql).iterator().next().iterator().next().asString();

		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return new PersistentCreativeTask(taskid, connection);
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableTaskConstellation#addRatingTask(edu.kit.ipd.creativecrowd.mutablemodel.MutableRatingTask)
	 */
	@Override
	public void addRatingTask(MutableRatingTask ratingTask) throws DatabaseException {
		try {
			int currentLength = this.getTaskCount();
			List<Value> args = new ArrayList<Value>();
			args.add((Value.fromString(connection.generateID("containsevaluative"))));
			args.add(Value.fromString(id));
			args.add(Value.fromString(ratingTask.getID()));
			args.add((Value.fromInt(currentLength)));
			String sql = connection.formatString("INSERT INTO containsevaluative (id,taskconstellationid,ratingtaskid,task_position) VALUES ({?},{?},{?},{?}); ", args);
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableTaskConstellation#getMutableRatingTask(int)
	 */
	@Override
	public MutableRatingTask getMutableRatingTask(int taskPosition) throws DatabaseException {
		String taskid = null;
		MutableRatingTask ret = null;
		try {
			String sql = connection.formatString("SELECT ratingtaskid FROM containsevaluative WHERE task_position = {?} AND taskconstellationid = {?}; ", Value.fromInt(taskPosition),
					Value.fromString(this.id));
			if (connection.query(sql).iterator().hasNext()) {
				taskid = connection.query(sql).iterator().next().iterator().next().asString();
				ret = new PersistentRatingTask(taskid, connection);
			}

		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.TaskConstellation#getID()
	 */
	@Override
	public String getID() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		return this.id.equals(obj.toString());

	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableTaskConstellation#getLength()
	 */
	@Override
	public int getTaskCount() throws DatabaseException {
		int ret = 0;
		try {
			String sql = connection.formatString("SELECT id FROM containscreative WHERE taskconstellationid = {?}; ", Value.fromString(this.id));
			for (@SuppressWarnings("unused") Iterable<Value> row : connection.query(sql)) {
				ret++;
			}
			sql = connection.formatString("SELECT id FROM containsevaluative WHERE taskconstellationid = {?}; ", Value.fromString(this.id));
			for (@SuppressWarnings("unused") Iterable<Value> row : connection.query(sql)) {
				ret++;
			}
			sql = connection.formatString("SELECT id FROM containscontrol WHERE taskconstellationid = {?}; ", Value.fromString(this.id));
			for (@SuppressWarnings("unused") Iterable<Value> row : connection.query(sql)) {
				ret++;
			}
			sql = connection.formatString("SELECT id FROM containscalibtask WHERE taskconstellationid = {?}; ", Value.fromString(this.id));
			for (@SuppressWarnings("unused") Iterable<Value> row : connection.query(sql)) {
				ret++;
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableTaskConstellation#addCreativeTask()
	 */
	@Override
	public void addCreativeTask(MutableCreativeTask ct) throws DatabaseException {
		try {
			int currentLength = this.getTaskCount();
			List<Value> args = new ArrayList<Value>();
			args.add((Value.fromString(connection.generateID("containscreative"))));
			args.add(Value.fromString(id));
			MutableCreativeTask creativeTask = ct;
			args.add(Value.fromString(creativeTask.getID()));
			args.add((Value.fromInt(currentLength)));
			String sql = connection.formatString("INSERT INTO containscreative (id,taskconstellationid,creativetaskid,task_position) VALUES ({?},{?},{?},{?}); ", args);
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	@Override
	public PersistentAssignment getAssignment() throws DatabaseException {
		PersistentAssignment ret;
		try {
			String sql = connection.formatString("SELECT assignmentid FROM taskconstellation WHERE id = {?};", Value.fromString(this.id));
			ret = new PersistentAssignment(connection.query(sql).iterator().next().iterator().next().asString(), this.connection);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ret;
	}

	@Override
	public boolean submitButtonExists() throws DatabaseException {
		boolean ret = false;
		try {
			String sql = connection.formatString("SELECT submit FROM taskconstellation WHERE id = {?};", Value.fromString(this.id));
			if (connection.query(sql).iterator().next().iterator().next().asInt() == 1) {
				ret = true;
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());

		}
		return ret;
	}

	@Override
	public void setSubmitButton(boolean submit) throws DatabaseException {
		try {
			int intSubmit = submit ? 1 : 0;
			String sql = connection.formatString("UPDATE taskconstellation SET submit = {?} WHERE id = {?};", Value.fromInt(intSubmit), Value.fromString(this.id));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	@Override
	public MutableAnswer answerCreativeTaskAt(int index)
			throws DatabaseException {
		MutableAnswer ret = null;
		try {
			int i = 0;
			for (Task t : getTasks()) {
				if (i == index) {
					List<Value> args = new ArrayList<Value>();
					String sql = connection.formatString("SELECT id FROM containscreative WHERE creativetaskid = {?} AND taskconstellationid = {?};", Value.fromString(t.getID()),
							Value.fromString(this.id));
					String answerid = connection.generateID("answer");
					args.add(Value.fromString(answerid));
					args.add(Value.fromString(connection.query(sql).iterator().next().iterator().next().asString()));
					
					args.add(Value.fromString(getCurrentWorkerID()));
					
					sql = connection.formatString("INSERT INTO answer (id,containscreativeid,workerid) VALUES ({?}, {?},{?});", args);
					connection.query(sql);
					ret = new PersistentAnswer(answerid, connection);
				}
				i++;
			}
		} catch (NoSuchElementException e) {
			throw new DatabaseException(e.getMessage() + ", \n probably the Task at the given index is not a CreativeTask");
		} catch (SQLException ex) {
			throw new DatabaseException(ex.getMessage() + " an Error occured whilst trying to create an Answer" );
		}
		return ret;

	}

	@Override
	public MutableRating addRatingToRatingTaskAt(int index)
			throws DatabaseException {
		MutableRating ret = null;
		try {
			int i = 0;
			for (Task t : getTasks()) {
				if (i == index) {
					List<Value> args = new ArrayList<Value>();
					String sql = connection.formatString("SELECT id FROM containsevaluative WHERE ratingtaskid = {?} AND taskconstellationid = {?};", Value.fromString(t.getID()),
							Value.fromString(this.id));
					String ratingid = connection.generateID("rating");
					args.add(Value.fromString(ratingid));
					args.add(Value.fromString(connection.query(sql).iterator().next().iterator().next().asString()));
					args.add(Value.fromString(getCurrentWorkerID()));
					sql = connection.formatString("INSERT INTO rating (id,containsevaluativeid,workerid) VALUES ({?}, {?},{?});",args);
					connection.query(sql);
					ret = new PersistentRating(ratingid, connection);
				}
				i++;
			}
		} catch (NoSuchElementException e) {
			throw new DatabaseException(e.getMessage() + ", \n probably the Task at the given index is not a RatingTask");
		} catch (SQLException ex) {
			throw new DatabaseException(ex.getMessage() + " an Error occured whilst trying to create an Answer");
		}
		return ret;

	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.Assignment#getAnswers()
	 */
	@Override
	public Iterable<MutableAnswer> getAnswers() throws DatabaseException {
		List<MutableAnswer> ret = new ArrayList<MutableAnswer>();
		try {
			// look up in containscreative
			String sql = connection.formatString("SELECT id FROM containscreative WHERE taskconstellationid = {?};", Value.fromString(this.id));
			// for each containscreative add answers containing this containscreativeid to ret
			for (Iterable<Value> containscreativeRow : connection.query(sql)) {
				String containscreativeid = containscreativeRow.iterator().next().asString();
				sql = connection.formatString("SELECT id FROM answer WHERE containscreativeid = {?};", Value.fromString(containscreativeid));
				// add each answer to ret
				for (Iterable<Value> answerRow : connection.query(sql)) {
					ret.add(new PersistentAnswer(answerRow.iterator().next().asString(), connection));

				}

			}

		} catch (NoSuchElementException ex) {
			ret.clear();

		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}

		return ret;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.Assignment#getRatings()
	 */
	public Iterable<MutableRating> getRatings() throws DatabaseException {
		List<MutableRating> ret = new ArrayList<MutableRating>();
		try {
			// look up in containsevaluative
			String sql = connection.formatString("SELECT id FROM containsevaluative WHERE taskconstellationid = {?};", Value.fromString(this.id));
			// for each containsevaluative add ratings containing this containsevaluativeid to ret
			for (Iterable<Value> containsevaluativeRow : connection.query(sql)) {
				String containsevaluativeid = containsevaluativeRow.iterator().next().asString();
				sql = connection.formatString("SELECT id FROM rating WHERE containsevaluativeid = {?};", Value.fromString(containsevaluativeid));
				// add each rating to ret
				for (Iterable<Value> ratingRow : connection.query(sql)) {
					ret.add(new PersistentRating(ratingRow.iterator().next().asString(), connection));

				}

			}

		} catch (NoSuchElementException ex) {
			throw new DatabaseException(ex.getMessage());

		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}

		return ret;
	}

	@Override
	public int getCurrentTaskIndex() throws DatabaseException {
		int ret = 0;
		try {
			String sql = connection
					.formatString(
							"SELECT current_pos FROM taskconstellation WHERE id = {?};",
							Value.fromString(this.id));
			ret = connection.query(sql).iterator().next().iterator().next().asInt();

		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());

		}
		return ret;
	}

	@Override
	public void addControlQuestion(MutableControlQuestion coq) throws DatabaseException {
		try {
			int currentLength = this.getTaskCount();
			List<Value> args = new ArrayList<Value>();
			args.add((Value.fromString(coq.getID())));
			args.add(Value.fromString(id));
			MutableControlQuestion cq = coq;
			args.add(Value.fromString(cq.getID()));
			args.add((Value.fromInt(currentLength)));
			String sql = connection.formatString("INSERT INTO containscontrol (id,taskconstellationid,controlquestionid,task_position) VALUES ({?},{?},{?},{?}); ", args);
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	@Override
	public MutableControlQuestion getMutableControlQuestion(int taskPosition) throws DatabaseException {
		String taskid = null;
		try {
			String sql = connection.formatString("SELECT controlquestionid FROM containscontrol WHERE task_position = {?} AND taskconstellationid = {?}; ", Value.fromInt(taskPosition),
					Value.fromString(this.id));
			taskid = connection.query(sql).iterator().next().iterator().next().asString();

		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return new PersistentControlQuestion(connection, taskid);
	}

	@Override
	public void addCalibrationQuestion(MutableCalibrationQuestion coq) throws DatabaseException {
		try {
			int currentLength = this.getTaskCount();
			List<Value> args = new ArrayList<Value>();
			args.add((Value.fromString(coq.getID())));
			args.add(Value.fromString(id));
			MutableCalibrationQuestion cq = coq;
			args.add(Value.fromString(cq.getID()));
			args.add((Value.fromInt(currentLength)));
			String sql = connection.formatString("INSERT INTO containscalibtask (id,taskconstellationid,calibrationquestionid,task_position) VALUES ({?},{?},{?},{?}); ", args);
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	@Override
	public MutableCalibrationQuestion getMutableCalibrationQuestion(int taskPosition) throws DatabaseException {
		String taskid = null;
		try {
			String sql = connection.formatString("SELECT calibrationquestionid FROM containscalibtask WHERE task_position = {?} AND taskconstellationid = {?}; ", Value.fromInt(taskPosition),
					Value.fromString(this.id));
			taskid = connection.query(sql).iterator().next().iterator().next().asString();

		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return new PersistentCalibrationQuestion(taskid, connection);
	}


	@Override
	public MutableControlAnswer answerControlQuestionAt(int index) throws DatabaseException {
		MutableControlAnswer ret = null;
		try {
			int i = 0;
			for (Task t : getTasks()) {
				if (i == index) {
					List<Value> args = new ArrayList<Value>();
					String answerid = connection.generateID("controlanswer");
					args.add(Value.fromString(answerid));
					String sql = connection.formatString("SELECT id FROM containscontrol WHERE controlquestionid = {?} AND taskconstellationid = {?};", Value.fromString(t.getID()),
							Value.fromString(this.id));
					args.add(Value.fromString(connection.query(sql).iterator().next().iterator().next().asString()));

					args.add(Value.fromString(getCurrentWorkerID()));
					sql = connection.formatString("INSERT INTO controlanswer (id,controlquestionid,workerid) VALUES ({?}, {?},{?});",args);
					connection.query(sql);
					ret = new PersistentControlAnswer(connection, answerid);
				}
				i++;
			}
		} catch (NoSuchElementException e) {
			throw new DatabaseException(e.getMessage() + ", \n probably the Task at the given index is not a Controlquestion");
		} catch (SQLException ex) {
			throw new DatabaseException(ex.getMessage() + " an Error occured whilst trying to create an Controlanswer");
		}
		return ret;

	}

	@Override
	public MutableCalibrationAnswer answerCalibrationQuestionAt(int index) throws DatabaseException {
		MutableCalibrationAnswer ret = null;
		try {
			int i = 0;
			
			for (Task t : getTasks()) {
				if (i == index) {
					List<Value> args = new ArrayList<Value>();
					String answerid = connection.generateID("calibrationanswer");
					args.add(Value.fromString(answerid));
					String sql = connection.formatString("SELECT id FROM containscalibtask WHERE calibrationquestionid = {?} AND taskconstellationid = {?};", Value.fromString(t.getID()),
							Value.fromString(this.id));
					args.add(Value.fromString(connection.query(sql).iterator().next().iterator().next().asString()));

					args.add(Value.fromString(getCurrentWorkerID()));
					Logger.debug("==================" + args.toString());
					sql = connection.formatString("INSERT INTO calibrationanswer (id,calibrationquestionid,workerid) VALUES ({?}, {?},{?});",args);
					connection.query(sql);
					ret = new PersistentCalibrationAnswer(connection, answerid);
				}
				i++;
			}
		} catch (NoSuchElementException e) {
			throw new DatabaseException(e.getMessage() + ", \n probably the Task at the given index is not a Calibrationanswer");
		} catch (SQLException ex) {
			throw new DatabaseException(ex.getMessage() + " an Error occured whilst trying to create an Calibrationanswer");
		}
		return ret;
	}
	
	private String getCurrentWorkerID() throws DatabaseException{
		try {
			String sql = connection.formatString("SELECT worker_mturkid FROM assignment, taskconstellation WHERE assignment.id = {?};",
					Value.fromString(this.getAssignment().getID()));			
			String result = connection.query(sql).iterator().next().iterator().next().asString();
			Logger.debug("==============" +result);
			
			return result;
		} catch (SQLException ex) {
			throw new DatabaseException(ex.getMessage() + " an Error occured whilst trying to get the workerid");
		}
	}

	@Override
	public MutableCalibrationAnswer answerCalibrationQuestionAt(String calibrationquestid) throws DatabaseException {
		MutableCalibrationAnswer ret = null;
		try {
			for (Task t : getTasks()) {
				if (t.getID().equals(calibrationquestid)) {
					List<Value> args = new ArrayList<Value>();
					String answerid = connection.generateID("calibrationanswer");
					args.add(Value.fromString(answerid));
					String sql = connection.formatString("SELECT id FROM containscalibtask WHERE calibrationquestionid = {?} AND taskconstellationid = {?};", Value.fromString(t.getID()),
							Value.fromString(this.id));
					args.add(Value.fromString(connection.query(sql).iterator().next().iterator().next().asString()));
					args.add(Value.fromString(getCurrentWorkerID()));
					sql = connection.formatString("INSERT INTO calibrationanswer (id,calibrationquestionid,workerid) VALUES ({?}, {?},{?});",args);
					connection.query(sql);
					ret = new PersistentCalibrationAnswer(connection, answerid);
				}
			}
		} catch (NoSuchElementException e) {
			throw new DatabaseException(e.getMessage() + ", \n probably the Task at the given index is not a Calibrationanswer");
		} catch (SQLException ex) {
			throw new DatabaseException(ex.getMessage() + " an Error occured whilst trying to create an Calibrationanswer");
		}
		return ret;
	}

	@Override
	public MutableAnswer answerCreativeTaskAt(String creativeTaskId) throws DatabaseException {
		MutableAnswer ret = null;
		try {
			for (Task t : getTasks()) {
				if (t.getID().equals(creativeTaskId)) {
					List<Value> args = new ArrayList<Value>();
					String sql = connection.formatString("SELECT id FROM containscreative WHERE creativetaskid = {?} AND taskconstellationid = {?};", Value.fromString(t.getID()),
							Value.fromString(this.id));
					String answerid = connection.generateID("answer");
					args.add(Value.fromString(answerid));
					args.add(Value.fromString(connection.query(sql).iterator().next().iterator().next().asString()));
					args.add(Value.fromString(getCurrentWorkerID()));
					sql = connection.formatString("INSERT INTO answer (id,containscreativeid,workerid) VALUES ({?}, {?},{?});",args);
					connection.query(sql);
					ret = new PersistentAnswer(answerid, connection);
				}
			}
		} catch (NoSuchElementException e) {
			throw new DatabaseException(e.getMessage() + ", \n probably the Task at the given index is not a CreativeTask");
		} catch (SQLException ex) {
			throw new DatabaseException(ex.getMessage() + " an Error occured whilst trying to create an Answer");
		}
		return ret;
	}
	
	@Override
	public MutableRating addRatingToRatingTaskAt(String ratingTaskId)
			throws DatabaseException {
		MutableRating ret = null;
		try {
			for (Task t : getTasks()) {
				if (t.getID().equals(ratingTaskId)) {
					List<Value> args = new ArrayList<Value>();
					String sql = connection.formatString("SELECT id FROM containsevaluative WHERE ratingtaskid = {?} AND taskconstellationid = {?};", Value.fromString(t.getID()),
							Value.fromString(this.id));
					String ratingid = connection.generateID("rating");
					args.add(Value.fromString(ratingid));
					args.add(Value.fromString(connection.query(sql).iterator().next().iterator().next().asString()));
					args.add(Value.fromString(getCurrentWorkerID()));
					sql = connection.formatString("INSERT INTO rating (id,containsevaluativeid,workerid) VALUES ({?}, {?},{?});",args);
					connection.query(sql);
					ret = new PersistentRating(ratingid, connection);
				}
			}
		} catch (NoSuchElementException e) {
			throw new DatabaseException(e.getMessage() + ", \n probably the Task at the given index is not a RatingTask");
		} catch (SQLException ex) {
			throw new DatabaseException(ex.getMessage() + " an Error occured whilst trying to create an Answer");
		}
		return ret;

	}
}

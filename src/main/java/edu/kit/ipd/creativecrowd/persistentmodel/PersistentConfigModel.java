package edu.kit.ipd.creativecrowd.persistentmodel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
import java.net.URL;
import java.net.MalformedURLException;

import edu.kit.ipd.chimpalot.util.Logger;
import edu.kit.ipd.creativecrowd.readablemodel.CalibrationQuestion;
import edu.kit.ipd.creativecrowd.readablemodel.ConfigModel;
import edu.kit.ipd.creativecrowd.readablemodel.ControlQuestion;
import edu.kit.ipd.creativecrowd.readablemodel.ExperimentType;
import edu.kit.ipd.creativecrowd.readablemodel.PossibleControlAnswer;
import edu.kit.ipd.creativecrowd.crowdplatform.PlatformIdentity;
import edu.kit.ipd.creativecrowd.readablemodel.TypeOfTask;
import edu.kit.ipd.creativecrowd.readablemodel.Worker;
import edu.kit.ipd.creativecrowd.database.DatabaseConnection;
import edu.kit.ipd.creativecrowd.database.Value;
import edu.kit.ipd.creativecrowd.mutablemodel.MutablePossibleControlAnswer;

/**
 * Provides methods to set information of a config model.
 * @see edu.kit.ipd.creativecrowd.readablemodel.ConfigModel
 * 
 * @author Pascal Gabriel, Thomas Friedel
 *
 */
public class PersistentConfigModel implements ConfigModel {
	/** The unique name of a ConfigModel */
	private String name;
	/** The connection to the database */
	private DatabaseConnection connection;

	/**
	 * Initializes a PersistentConfigModel.
	 * This does NOT change anything at all in the Database.
	 * So if you want to put a new shiny Config in your Database, then this constructor DOES NOT HELP YOU AT ALL.
	 * Use {@link PersistentConfigModelRepo#createConfigModel(ConfigModel, String, String)} for that. General rule of thumb:
	 * If a constructor requires a connection, then you probably don't want to call it directly, unless you've got a connection
	 * as an attribut already. If you want a quick ConfigModel to play with, look at {@link edu.kit.ipd.chimpalot.jsonclasses.ConfigModelJson}, that might help you.
	 * 
	 * @param id the id of the configmodel
	 * @param connection the database connection
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 * @author Thomas Friedel
	 */
	public PersistentConfigModel(String id, DatabaseConnection connection) throws DatabaseException {
		this.name = id;
		this.connection = connection;
	}
	
	/**
	 * Sets the experiment id of the config model.
	 * @param expid the experiment id
	 * @return {@code true} if successful, {@code false} if input was malformed
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	protected boolean setExperimentID(String expid) throws DatabaseException {
		if (expid == null) {
			return false;
		}
		try {
			String sql = connection.formatString("UPDATE configmodel SET experimentid = {?} WHERE id = {?};", Value.fromString(expid), Value.fromString(name));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return true; 
	}
	
	/**
	 * Sets the question of the task.
	 * @param question the question
	 * @return {@code true} if successful, {@code false} if input was malformed
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	protected boolean setTaskQuestion(String question) throws DatabaseException{
		if (question == null) {
			return false;
		}
		try {
			String sql = connection.formatString("UPDATE configmodel SET taskquestion = {?} WHERE id = {?};", Value.fromString(question), Value.fromString(name));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return true; 	
	}
	
	/**
	 * Sets the rating task question.
	 * @param question the question
	 * @return {@code true} if successful, {@code false} if input was malformed
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	protected boolean setRatingTaskQuestion(String question) throws DatabaseException {
		if (question == null) {
			return false;
		}
		try {
			String sql = connection.formatString("UPDATE configmodel SET ratingtaskquestion = {?} WHERE id = {?};",
					Value.fromString(question), Value.fromString(name));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return true;
	}

	/**
	 * Sets the description of the task.
	 * @param description the description
	 * @return {@code true} if successful, {@code false} if input was malformed
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	protected boolean setTaskDescription(String description) throws DatabaseException{
		if (description == null) {
			return false;
		}
		try {
			String sql = connection.formatString("UPDATE configmodel SET taskdescription = {?} WHERE id = {?};", Value.fromString(description), Value.fromString(name));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return true;
	}

	/**
	 * Sets the title of the task.
	 * @param title the title
	 * @return {@code true} if successful, {@code false} if input was malformed
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	protected boolean setTaskTitle(String title) throws DatabaseException{
		if (title == null) {
			return false;
		}
		try {
			String sql = connection.formatString("UPDATE configmodel SET tasktitle = {?} WHERE id = {?};", Value.fromString(title), Value.fromString(name));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return true;	
	}

	/**
	 * Adds a tag for the task.
	 * @param tag the task
	 * @return {@code true} if successful, {@code false} if input was malformed
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	protected boolean addTaskTag(String tag) throws DatabaseException{
		if (tag == null) {
			return false;
		}
		try {
			String sql = null;
			String tagId = connection.generateID("configmodeltag");
			List<Value> args = new ArrayList<Value>();
			args.add(Value.fromString(tagId));
			args.add(Value.fromString(tag));
			args.add(Value.fromString(this.name));
			sql = connection.formatString("INSERT INTO configmodeltag (id,text,configmodelid) VALUES ({?},{?},{?});", args);
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return true;
	}

	/**
	 * Sets the URL of the picture.
	 * @param pictureURL the URL
	 * @return {@code true} if successful, {@code false} if input was malformed
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	protected boolean setPictureURL(String pictureURL) throws DatabaseException{
		if (pictureURL == null) {
			return false;
		}
		//testing for basic form of a URL
		if (!pictureURL.equals("")) {
			try {
				new URL(pictureURL);
			} catch (MalformedURLException e) {
				return false;
			}
		}
		try {
			String sql = connection.formatString("UPDATE configmodel SET pictureurl = {?} WHERE id = {?};", Value.fromString(pictureURL), Value.fromString(name));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return true;
	}

	/**
	 * Sets the source URL of the task.
	 * @param sourceURL the URL
	 * @return {@code true} if successful, {@code false} if input was malformed
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	protected boolean setTaskSourceURL(String sourceURL) throws DatabaseException{
		if (sourceURL == null) {
			return false;
		}
		//testing for basic form of a URL
		if (!sourceURL.equals("")) {
			try {
				new URL(sourceURL);
			} catch (MalformedURLException e) {
				return false;
			}
		}
		try {
			String sql = connection.formatString("UPDATE configmodel SET sourceurl = {?} WHERE id = {?};", Value.fromString(sourceURL), Value.fromString(name));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return true;
	}

	/**
	 * Adds a calibration question.
	 * @param question the calibration question
	 * @return {@code true} if successful, {@code false} if input was malformed
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	protected boolean addCalibQuestion(CalibrationQuestion question) throws DatabaseException{
		if (question == null) {
			return false;
		}
		try {
			String sql = null;
			List<Value> args = new ArrayList<Value>();
			args.add(Value.fromString(question.getID()));
			args.add(Value.fromString(this.name));
			sql = connection.formatString("INSERT INTO configmodelcalibquestions (id,configmodelid) VALUES ({?},{?});", args);
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return true;
	}

	/**
	 * Adds a control question.
	 * @param question the control question
	 * @return {@code true} if successful, {@code false} if input was malformed
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	protected boolean addControlQuestion(ControlQuestion question) throws DatabaseException{
		if (question == null) {
			return false;
		}
		try {
			String sql = null;
			String controlquestionid = connection.generateID("controlquestion");
			
			
			//Create Controlquestion
			sql = connection.formatString("INSERT INTO controlquestion (id, question) VALUES ({?}, {?})",
					Value.fromString(controlquestionid), Value.fromString(question.getQuestion() == null? "" : question.getQuestion()));
			connection.query(sql);
			//TODO answers are not added correctly, testing ControlQuestionJson first
			PersistentControlQuestion perCont = new PersistentControlQuestion(connection, controlquestionid);
			for (PossibleControlAnswer ans : question.getPossibleAnswers()) {
				MutablePossibleControlAnswer mutAns = perCont.addPossibleAnswer();
				mutAns.setText(ans.getText() == null? "" : ans.getText());
				mutAns.setIsTrue(ans.getIsTrue());
			}
			
			List<Value> args = new ArrayList<Value>();
			args.add(Value.fromString(controlquestionid));
			args.add(Value.fromString(this.name));			
			sql = connection.formatString("INSERT INTO configmodelcontrolquestions (id,configmodelid) VALUES ({?},{?});", args);
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return true;
	}
	
	/**
	 * Sets the max count of creative tasks.
	 * @param max the max count
	 * @return {@code true} if successful, {@code false} if input was malformed
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	protected boolean setMaxCreativeTask(int max) throws DatabaseException{
		if (max < 0) {
			return false;
		}
		try {
			String sql = connection.formatString("UPDATE configmodel SET maxcreativetask = {?} WHERE id = {?};", Value.fromInt(max), Value.fromString(name));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return true;	
	}

	/**
	 * Sets the max count of rating tasks.
	 * @param max the max count
	 * @return {@code true} if successful, {@code false} if input was malformed
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	protected boolean setMaxRatingTask(int max) throws DatabaseException{
		if (max < 0) {
			return false;
		}
		try {
			String sql = connection.formatString("UPDATE configmodel SET maxratingtask = {?} WHERE id = {?};", Value.fromInt(max), Value.fromString(name));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return true;
	}

	/**
	 * Sets the budget.
	 * @param budget the budget
	 * @return {@code true} if successful, {@code false} if input was malformed
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	protected boolean setBudget(int budget) throws DatabaseException{
		if (budget < 0) {
			return false;
		}
		try {
			String sql = connection.formatString("UPDATE configmodel SET budget = {?} WHERE id = {?};", Value.fromInt(budget), Value.fromString(name));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return true;
	}

	/**
	 * Sets if a creative task is send to the chosen platform.
	 * @param platform the chosen platform
	 * @param value {@code true} if send to, {@code false} if not
	 * @return {@code true} if successful, {@code false} if input was malformed
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	protected boolean setSendCreativeTo(PlatformIdentity platform, boolean value) throws DatabaseException{
		if (platform == PlatformIdentity.Unspecified || platform == null) {
			return false;
		}
		int boolAsInt;
		if (value == true) {
			boolAsInt = 1;
		} else {
			boolAsInt = 0;
		}
		try {
			if (platform == PlatformIdentity.MTurk) {
				String sql = connection.formatString("UPDATE configmodel SET sendcreativetomt = {?} WHERE id = {?};", Value.fromInt(boolAsInt), Value.fromString(name));
				connection.query(sql);
				return true;
			} else {
				String sql = connection.formatString("UPDATE configmodel SET sendcreativetopb = {?} WHERE id = {?};", Value.fromInt(boolAsInt), Value.fromString(name));
				connection.query(sql);
				return true;
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/**
	 * Sets if a rating task is send to the chosen platform.
	 * @param platform the chosen platform
	 * @param value {@code true} if send to, {@code false} if not
	 * @return {@code true} if successful, {@code false} if input was malformed
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	protected boolean setSendRatingTo(PlatformIdentity platform, boolean value) throws DatabaseException{
		if (platform == PlatformIdentity.Unspecified || platform == null) {
			return false;
		}
		int boolAsInt;
		if (value == true) {
			boolAsInt = 1;
		} else {
			boolAsInt = 0;
		}
		try {
			if (platform == PlatformIdentity.MTurk) {
				String sql = connection.formatString("UPDATE configmodel SET sendratingtomt = {?} WHERE id = {?};", Value.fromInt(boolAsInt), Value.fromString(name));
				connection.query(sql);
				return true;
			} else {
				String sql = connection.formatString("UPDATE configmodel SET sendratingtopb = {?} WHERE id = {?};", Value.fromInt(boolAsInt), Value.fromString(name));
				connection.query(sql);
				return true;
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/**
	 * Sets the basic payment for the chosen platform.
	 * @param platform the chosen platform
	 * @param payment the basic payment
	 * @return {@code true} if successful, {@code false} if input was malformed
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	protected boolean setBasicPayment(PlatformIdentity platform, int payment) throws DatabaseException{
		if (platform == PlatformIdentity.Unspecified || platform == null || payment < 0) {
			return false;
		}
		String sql = null;
		try {
			if (platform == PlatformIdentity.MTurk) {
				sql = connection.formatString("UPDATE configmodel SET basicpaymentmt = {?} WHERE id = {?};", Value.fromInt(payment), Value.fromString(name));
			} else if (platform == PlatformIdentity.PyBossa) {
				sql = connection.formatString("UPDATE configmodel SET basicpaymentpb = {?} WHERE id = {?};", Value.fromInt(payment), Value.fromString(name));
			}
			connection.query(sql);
			return true;
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/**
	 * Sets the payment per task for the chosen platform.
	 * @param platform the chosen platform
	 * @param payment the payment per task
	 * @return {@code true} if successful, {@code false} if input was malformed
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	protected boolean setPaymentPerTask(PlatformIdentity platform, TypeOfTask type, int payment) throws DatabaseException{
		if (platform == PlatformIdentity.Unspecified || platform == null || type == null || payment < 0) {
			return false;
		}
		String sql = null;
		try {
			if (platform == PlatformIdentity.MTurk) {
				if (type == TypeOfTask.Creative) {
					sql = connection.formatString("UPDATE configmodel SET paymentpertaskcrmt = {?} WHERE id = {?};", Value.fromInt(payment), Value.fromString(name));
				} else if(type == TypeOfTask.Rating){
					sql = connection.formatString("UPDATE configmodel SET paymentpertaskramt = {?} WHERE id = {?};", Value.fromInt(payment), Value.fromString(name));
				} else {
					Logger.error("platform " + String.valueOf(platform) + "does not have the requested TaskType "+ String.valueOf(type));
					return false;
				}
			} else if (platform == PlatformIdentity.PyBossa) {
				if (type == TypeOfTask.Creative) {
					sql = connection.formatString("UPDATE configmodel SET paymentpertaskcrpb = {?} WHERE id = {?};", Value.fromInt(payment), Value.fromString(name));
				} else if(type == TypeOfTask.Rating) {
					sql = connection.formatString("UPDATE configmodel SET paymentpertaskrapb = {?} WHERE id = {?};", Value.fromInt(payment), Value.fromString(name));
				} else {
					Logger.error("platform " + String.valueOf(platform) + "does not have the requested TaskType "+ String.valueOf(type));
					return false;
				}
			} else {
				Logger.error("CONFIG MODEL (setPaymentPerTask): The requested platform is not implemented in the tables");
				return false;
			}
			connection.query(sql);
			return true;
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/**
	 * Sets the evaluation type.
	 * @param type the evaluation type
	 * @return {@code true} if successful, {@code false} if input was malformed
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	protected boolean setEvaluationType(String type) throws DatabaseException{
		if (type == null) {
			return false;
		}
		try {
			String sql = connection.formatString("UPDATE configmodel SET evaluationtype = {?} WHERE id = {?};", Value.fromString(type), Value.fromString(name));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return true;
	}
	
	/**
	 * Sets the rating options.
	 * @param ratingOptions list of rating options
	 * @return {@code true} if successful, {@code false} if input was malformed
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	protected boolean setRatingOptions(Map<String, Float> ratingOptions) throws DatabaseException {
		if (ratingOptions == null) {
			return false;
		}
		try {
			for (Map.Entry<String, Float> entry : ratingOptions.entrySet()) {
				
				String optionId = connection.generateID("ratingoptionconfigmodel");
				List<Value> args = new ArrayList<Value>();
				args.add(Value.fromString(optionId));
				args.add(Value.fromString(entry.getKey()));
				args.add(Value.fromFloat(entry.getValue()));
				args.add(Value.fromString(this.name));
				String sql = connection.formatString("INSERT INTO ratingoptionconfigmodel"
						+ " (id,text,value,configmodelid) VALUES ({?},{?},{?},{?});", args);
				Logger.debug("SQL: " + sql);
				connection.query(sql);
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return true;
	}
	
	/**
	 * Sets the strategies.
	 * @param strategy Map of strategies.
	 * @return {@code true} if successful, {@code false} if input was malformed
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	protected boolean setStrategy(Map<String, String> strategy) throws DatabaseException{
		if (strategy == null) {
			return false;
		}
		// for each param
		for (Map.Entry<String, String> entry : strategy.entrySet()) {

			// create id and prepare formatString- Iterable
			String strategyparamid = connection.generateID("configmodelstrategy");
			List<Value> sqlArgs = new ArrayList<Value>();
			if(entry.getKey() == null || entry.getValue() == null) {
				Logger.error("Some entry in the map is null");
				break;
			}
			sqlArgs.add(Value.fromString(strategyparamid));
			sqlArgs.add(Value.fromString(entry.getKey()));
			sqlArgs.add(Value.fromString(entry.getValue()));
			sqlArgs.add(Value.fromString(name));

			// insert into database
			try {
				String sql = connection.formatString("INSERT INTO configmodelstrategy (id,key,value,configmodelid) VALUES ({?},{?},{?},{?});", sqlArgs);
				connection.query(sql);
			} catch (SQLException e) {
				throw new DatabaseException(e.getMessage());
			}
		}
		return true;
	}
	
	/**
	 * Sets the qualifications for a chosen platform. pybossa qualifications are stored backwards.
	 * @param qualifications the qualifications
	 * @param platform the chosen platform
	 * @return {@code true} if successful, {@code false} if input was malformed
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	protected boolean setQualifications(Iterable<String> qualifications, PlatformIdentity platform) throws DatabaseException{
		if (platform == PlatformIdentity.Unspecified || platform == null || qualifications == null) {
			return false;
		}
		Iterator<String> qualiIterator = qualifications.iterator();
		while(qualiIterator.hasNext()) {
			String sql = null;
			List<Value> sqlArgs = new ArrayList<Value>();
			String actualValue = qualiIterator.next();

			try {
				if (platform == PlatformIdentity.MTurk) {
					String qualificationid = connection.generateID("configmodelqualificationmt");

					sqlArgs.add(Value.fromString(qualificationid));
					sqlArgs.add(Value.fromString(actualValue));
					sqlArgs.add(Value.fromString(name));

					sql = connection.formatString("INSERT INTO configmodelqualificationmt (id,qualification,configmodelid) VALUES ({?},{?},{?});", sqlArgs);

				} else if (platform == PlatformIdentity.PyBossa) {
					String current = actualValue;
					current = backwardsString(current);
					String qualificationid = connection.generateID("configmodelqualificationpb");
					sqlArgs.add(0, Value.fromString(qualificationid));
					sqlArgs.add(Value.fromString(current));
					sqlArgs.add(Value.fromString(name));
					Logger.debug(sqlArgs.toString());
					sql = connection.formatString("INSERT INTO configmodelqualificationpb (id,qualification,configmodelid) VALUES ({?},{?},{?});", sqlArgs);
				} else {
					return false;
				}
				connection.query(sql);
			} catch (SQLException e) {
				throw new DatabaseException(e.getMessage());
			}
		}
		return true;
	}
	
	/**
	 * Sets the threshold a pybossa worker needs to surpass in his average Rating.
	 * @param threshold the threshold
	 * @return {@code true} if successful, {@code false} if input was malformed
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	protected boolean setAverageRatingThreshold(float threshold) throws DatabaseException{
		if (threshold < 0) {
			return false;
		}
		try {
			String sql = connection.formatString("UPDATE configmodel SET averageratingthreshold = {?} WHERE id = {?};", Value.fromFloat(threshold), Value.fromString(name));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return true;
	}
	
	/**
	 * Sets the threshold a pybossa worker needs to surpass in his total count of tasks.
	 * @param threshold the threshold
	 * @return {@code true} if successful, {@code false} if input was malformed
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	protected boolean setTotalTaskCountThreshold(int threshold) throws DatabaseException{
		if (threshold < 0) {
			return false;
		}
		try {
			String sql = connection.formatString("UPDATE configmodel SET totaltaskcountthreshold = {?} WHERE id = {?};", Value.fromInt(threshold), Value.fromString(name));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return true;
	}
	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.ConfigModel#getID()
	 */
	@Override
	public String getID() throws DatabaseException {
		return this.name;
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.ConfigModel#getExperimentID()
	 */
	@Override
	public String getExperimentID() throws DatabaseException {
		String result;
		try {
			String sql = connection.formatString("SELECT experimentid FROM configmodel WHERE id = {?};", Value.fromString(name));
			Iterable<Iterable<Value>> values = connection.query(sql);
			result = values.iterator().next().iterator().next().asString();
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.ConfigModel#getTaskQuestion()
	 */
	@Override
	public String getTaskQuestion() throws DatabaseException{
		String result;
		try {
			String sql = connection.formatString("SELECT taskquestion FROM configmodel WHERE id = {?};", Value.fromString(name));
			Iterable<Iterable<Value>> values = connection.query(sql);
			result = values.iterator().next().iterator().next().asString();
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.ConfigModel#getTaskDescription()
	 */
	@Override
	public String getTaskDescription() throws DatabaseException {
		String result;
		try {
			String sql = connection.formatString("SELECT taskdescription FROM configmodel WHERE id = {?};", Value.fromString(name));
			Iterable<Iterable<Value>> values = connection.query(sql);
			result = values.iterator().next().iterator().next().asString();
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.ConfigModel#getTaskTitle()
	 */
	@Override
	public String getTaskTitle() throws DatabaseException {
		String result;
		try {
			String sql = connection.formatString("SELECT tasktitle FROM configmodel WHERE id = {?};", Value.fromString(name));
			Iterable<Iterable<Value>> values = connection.query(sql);
			result = values.iterator().next().iterator().next().asString();
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.ConfigModel#getTaskTags()
	 */
	@Override
	public String[] getTaskTags() throws DatabaseException {
		String[] result;
		LinkedList<String> ret = new LinkedList<String>();
		try {
			String sql = connection.formatString("SELECT text FROM configmodeltag WHERE configmodelid = {?};", Value.fromString(name));
			Iterable<Iterable<Value>> values = connection.query(sql);
			Iterator<Iterable<Value>> tagIterator = values.iterator();
			while (tagIterator.hasNext()) {
				String tagId = tagIterator.next().iterator().next().asString();
				ret.add(tagId);
			}
			result = ret.toArray(new String[0]);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.ConfigModel#getPictureURL()
	 */
	@Override
	public String getPictureURL() throws DatabaseException {
		String result;
		try {
			String sql = connection.formatString("SELECT pictureurl FROM configmodel WHERE id = {?};", Value.fromString(name));
			Iterable<Iterable<Value>> values = connection.query(sql);
			result = values.iterator().next().iterator().next().asString();
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.ConfigModel#getTaskSourceURL()
	 */
	@Override
	public String getTaskSourceURL() throws DatabaseException {
		String result;
		try {
			String sql = connection.formatString("SELECT sourceurl FROM configmodel WHERE id = {?};", Value.fromString(name));
			Iterable<Iterable<Value>> values = connection.query(sql);
			result = values.iterator().next().iterator().next().asString();
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.ConfigModel#getCalibQuestions()
	 */
	@Override
	public CalibrationQuestion[] getCalibQuestions() throws DatabaseException {
		List<CalibrationQuestion> ret = new LinkedList<CalibrationQuestion>();
		try {
			String sql = connection.formatString("SELECT id FROM configmodelcalibquestions WHERE configmodelid = {?};", Value.fromString(name));
			Iterable<Iterable<Value>> calibQIds = connection.query(sql);
			Iterator<Iterable<Value>> namesIterator = calibQIds.iterator();
		
			while (namesIterator.hasNext()) {
				String calibQId = namesIterator.next().iterator().next().asString();
				CalibrationQuestion calibQ = new PersistentCalibrationQuestion(calibQId, this.connection);
				ret.add(calibQ);
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ret.toArray(new CalibrationQuestion[0]);
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.ConfigModel#getControlQuestions()
	 */
	@Override
	public ControlQuestion[] getControlQuestions() throws DatabaseException {
		List<ControlQuestion> ret = new LinkedList<ControlQuestion>();
		try {
			String sql = connection.formatString("SELECT id FROM configmodelcontrolquestions WHERE configmodelid = {?};", Value.fromString(name));
			Iterable<Iterable<Value>> controlQIds = connection.query(sql);
			Iterator<Iterable<Value>> namesIterator = controlQIds.iterator();
		
			while (namesIterator.hasNext()) {
				String controlQId = namesIterator.next().iterator().next().asString();
				ControlQuestion controlQ = new PersistentControlQuestion(this.connection, controlQId);
				ret.add(controlQ);
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ret.toArray(new ControlQuestion[0]);
	}
    
	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.ConfigModel#getMaxCreativeTask()
	 */
	@Override
	public int getMaxCreativeTask() throws DatabaseException {
		int result;
		try {
			String sql = connection.formatString("SELECT maxcreativetask FROM configmodel WHERE id = {?};", Value.fromString(name));
			Iterable<Iterable<Value>> values = connection.query(sql);
			result = values.iterator().next().iterator().next().asInt();
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.ConfigModel#getMaxRatingTask()
	 */
	@Override
	public int getMaxRatingTask() throws DatabaseException {
		int result;
		try {
			String sql = connection.formatString("SELECT maxratingtask FROM configmodel WHERE id = {?};", Value.fromString(name));
			Iterable<Iterable<Value>> values = connection.query(sql);
			result = values.iterator().next().iterator().next().asInt();
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.ConfigModel#getBudget()
	 */
	@Override
	public int getBudget() throws DatabaseException {
		int result;
		try {
			String sql = connection.formatString("SELECT budget FROM configmodel WHERE id = {?};", Value.fromString(name));
			Iterable<Iterable<Value>> values = connection.query(sql);
			result = values.iterator().next().iterator().next().asInt();
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.ConfigModel#getSendCreativeTo(PlatformIdentity)
	 */
	@Override
	public boolean getSendCreativeTo(PlatformIdentity platform) throws DatabaseException {
		String sql = "";
		int resultInt;
		try {
			if (platform == PlatformIdentity.MTurk) {
				sql = connection.formatString("SELECT sendcreativetomt FROM configmodel WHERE id = {?};", Value.fromString(name));
			} else if (platform == PlatformIdentity.PyBossa){
				sql = connection.formatString("SELECT sendcreativetopb FROM configmodel WHERE id = {?};", Value.fromString(name));
			} else {
				return false;
			}
			Iterable<Iterable<Value>> values = connection.query(sql);
			resultInt = values.iterator().next().iterator().next().asInt();
			if (resultInt == 0) {
				return false;
			} else if (resultInt == 1) {
				return true;
			} else {
				throw new SQLException();
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.ConfigModel#getSendRatingTo(PlatformIdentity)
	 */
	@Override
	public boolean getSendRatingTo(PlatformIdentity platform) throws DatabaseException {
		String sql = "";
		int resultInt;
		try {
			if (platform == PlatformIdentity.MTurk) {
				sql = connection.formatString("SELECT sendratingtomt FROM configmodel WHERE id = {?};", Value.fromString(name));
			} else if (platform == PlatformIdentity.PyBossa){
				sql = connection.formatString("SELECT sendratingtopb FROM configmodel WHERE id = {?};", Value.fromString(name));
			} else {
				return false;
			}
			Iterable<Iterable<Value>> values = connection.query(sql);
			resultInt = values.iterator().next().iterator().next().asInt();
			if (resultInt == 0) {
				return false;
			} else if (resultInt == 1) {
				return true;
			} else {
				throw new SQLException();
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.ConfigModel#getBasicPayment(PlatformIdentity)
	 */
	@Override
	public int getBasicPayment(PlatformIdentity platform) throws DatabaseException {
		if(platform == null) {
			Logger.error("CONFIGMODEL (getBasicPayment): Tried to use null as identity");
			return 0;
		}
		String sql = "";
		int result = 0;
		try {
			if (platform == PlatformIdentity.MTurk) {
				sql = connection.formatString("SELECT basicpaymentmt FROM configmodel WHERE id = {?};", Value.fromString(name));
			} else if (platform == PlatformIdentity.PyBossa){
				sql = connection.formatString("SELECT basicpaymentpb FROM configmodel WHERE id = {?};", Value.fromString(name));
			} else {
				Logger.error("CONFIGMODEL (getBasicPayment): Tried to access the basic payment of a platform without table");
				return 0;
			}
			Iterable<Iterable<Value>> values = connection.query(sql);
			result = values.iterator().next().iterator().next().asInt();
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.ConfigModel#getPaymentPerTask(PlatformIdentity, TypeOfTask)
	 */
	@Override
	public int getPaymentPerTask(PlatformIdentity platform, TypeOfTask type) throws DatabaseException {
		String sql = "";
		int result = 0;
		try {
			if (platform == PlatformIdentity.MTurk) {
				if (type == TypeOfTask.Creative) {
					sql = connection.formatString("SELECT paymentpertaskcrmt FROM configmodel WHERE id = {?};", Value.fromString(name));
				} else {
					sql = connection.formatString("SELECT paymentpertaskramt FROM configmodel WHERE id = {?};", Value.fromString(name));
				}
			} else if (platform == PlatformIdentity.PyBossa){
				if (type == TypeOfTask.Creative) {
					sql = connection.formatString("SELECT paymentpertaskcrpb FROM configmodel WHERE id = {?};", Value.fromString(name));
				} else {
					sql = connection.formatString("SELECT paymentpertaskrapb FROM configmodel WHERE id = {?};", Value.fromString(name));
				}
			}
			Iterable<Iterable<Value>> values = connection.query(sql);
			result = values.iterator().next().iterator().next().asInt();
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.ConfigModel#getEvaluationType()
	 */
	@Override
	public String getEvaluationType() throws DatabaseException {
		String result;
		try {
			String sql = connection.formatString("SELECT evaluationtype FROM configmodel WHERE id = {?};", Value.fromString(name));
			Iterable<Iterable<Value>> values = connection.query(sql);
			result = values.iterator().next().iterator().next().asString();
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.ConfigModel#getStrategy()
	 */
	@Override
	public Map<String, String> getStrategy() throws DatabaseException {
		Map<String, String> result = new HashMap<String, String>();
		try {
			String sql = connection.formatString("SELECT key,value FROM configmodelstrategy WHERE configmodelid = {?};", Value.fromString(name));
			Iterable<Iterable<Value>> values = connection.query(sql);
			Iterator<Iterable<Value>> strategyIterator = values.iterator();
			while (strategyIterator.hasNext()) {
				Iterable<Value> currentStrategy = strategyIterator.next();
				Iterator<Value> currentIterator = currentStrategy.iterator();
				String strategyKey = currentIterator.next().asString();
				String strategyValue = currentIterator.next().asString();
				result.put(strategyKey, strategyValue);
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.ConfigModel#getRatingTaskQuestion()
	 */
	@Override
	public String getRatingTaskQuestion() throws DatabaseException {
		String result;
		try {
			String sql = connection.formatString("SELECT ratingtaskquestion FROM configmodel WHERE id = {?};", Value.fromString(name));
			Iterable<Iterable<Value>> values = connection.query(sql);
			result = values.iterator().next().iterator().next().asString();
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.ConfigModel#getQualifications(PlatformIdentity)
	 */
	@Override
	public Iterable<String> getQualifications(PlatformIdentity platform) throws DatabaseException {
		LinkedList<String> result = new LinkedList<String>();
		try {
			String sql;
			if (platform == PlatformIdentity.MTurk) {
				sql = connection.formatString("SELECT qualification FROM configmodelqualificationmt"
						+ " WHERE configmodelid = {?};", Value.fromString(name));
				Logger.debug("NAME: " + name);
			} else if (platform == PlatformIdentity.PyBossa) {
				sql = connection.formatString("SELECT qualification FROM configmodelqualificationpb"
						+ " WHERE configmodelid = {?};", Value.fromString(name));
			} else {
				throw new DatabaseException("Undefined platformidentity.");
			}
			Logger.debug("SQL: " + sql);
			Iterator<Iterable<Value>> qualIterator = connection.query(sql).iterator();
			while (qualIterator.hasNext()){
				Iterable<Value> qualification = qualIterator.next();
				if(platform == PlatformIdentity.PyBossa) {
					result.add(backwardsString(qualification.iterator().next().asString()));
				} else if (platform == PlatformIdentity.MTurk) {
					result.add(qualification.iterator().next().asString());
				}
				
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.ConfigModel#getRatingOptions()
	 */
	@Override
	public Map<String, Float> getRatingOptions() throws DatabaseException {
		Map<String, Float> result = new HashMap<String, Float>();
		try {
			String sql = connection.formatString("SELECT text, value FROM ratingoptionconfigmodel WHERE configmodelid = {?};",
					Value.fromString(name)); 
			Iterable<Iterable<Value>> values = connection.query(sql);
			Iterator<Iterable<Value>> iterator = values.iterator();
			while (iterator.hasNext()) {
				Iterable<Value> currentOption = iterator.next();
				Iterator<Value> currentIterator = currentOption.iterator();
				String text = currentIterator.next().asString();
				float value = currentIterator.next().asFloat();
				result.put(text, value);
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.ConfigModel#getAverageRatingThreshold()
	 */
	@Override
	public float getAverageRatingThreshold() throws DatabaseException {
		float result;
		try {
			String sql = connection.formatString("SELECT averageratingthreshold FROM configmodel WHERE id = {?};", Value.fromString(name));
			Iterable<Iterable<Value>> values = connection.query(sql);
			result = values.iterator().next().iterator().next().asFloat();
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.ConfigModel#getTotalTaskCountThreshold()
	 */
	@Override
	public int getTotalTaskCountThreshold() throws DatabaseException {
		int result;
		try {
			String sql = connection.formatString("SELECT totaltaskcountthreshold FROM configmodel WHERE id = {?};", Value.fromString(name));
			Iterable<Iterable<Value>> values = connection.query(sql);
			result = values.iterator().next().iterator().next().asInt();
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return result;
	}

	@Override
	public ExperimentType getExperimentType() throws DatabaseException {
		String result;
		try {
			String sql = connection.formatString("SELECT experimenttype FROM configmodel WHERE id = {?};", Value.fromString(name));
			Iterable<Iterable<Value>> values = connection.query(sql);
			result = values.iterator().next().iterator().next().asString();
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		if (result == null || result.isEmpty()) {
			return null;
		}
		return ExperimentType.valueOf(result);
	}
	
	/**
	 * Sets the experiment type.
	 * @see edu.kit.ipd.creativecrowd.readablemodel.ExperimentType
	 * 
	 * @param type the type
	 * @return {@code true}, if the assignment was successful and valid, {@code false} if not
	 * @throws DatabaseException if the SQL request fails
	 */
	protected boolean setExperimentType(String type) throws DatabaseException {
		try {
			ExperimentType.valueOf(type);
		} catch (IllegalArgumentException  | NullPointerException e) {
			//Non valid ExperimentType
			return false;
		}
		try {
			String sql = connection.formatString("UPDATE configmodel SET experimenttype = {?} WHERE id = {?};",
					Value.fromString(type), Value.fromString(name));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return true;
	}
	
	@Override
	public Iterable<Worker> getBlockedWorkers() throws DatabaseException {
		List<Worker> result = new LinkedList<Worker>();
		try {
			String sql = connection.formatString("SELECT workerid FROM configmodelblockedworkers WHERE configid = {?};", Value.fromString(name));
			Iterable<Iterable<Value>> values = connection.query(sql);
			Iterator<Iterable<Value>> iterator = values.iterator();
			
			while (iterator.hasNext()) {
				result.add(new PersistentWorker(connection, iterator.next().iterator().next().asString()));
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return result;
	}
	
	/**
	 * Adds a worker to the list of for this experiment blocked workers.
	 * 
	 * @param worker the worker to be blocked
	 * @return {@code true}, if the addition was successful and valid, {@code false} if not
	 * @throws DatabaseException if the SQL request fails
	 */
	protected boolean addBlockedWorker(Worker worker) throws DatabaseException {
		if (worker == null) {
			return false;
		}
		for (Worker alreadyBlocked : this.getBlockedWorkers()) {
			if (alreadyBlocked.equals(worker)) {
				return false;
			}
		}
		try {
			
			String sql = connection.formatString("INSERT INTO configmodelblockedworkers (workerid, configid) VALUES ({?}, {?});",
					Value.fromString(worker.getID()), Value.fromString(name));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return true;
	}
	
	/*
	 * Returns the String you'd get from reading input backwards.
	 */
	private String backwardsString(String input) {
		if(input.length() == 1) {
			return input;
		} else {
			String ret = backwardsString(input.substring(1)) + backwardsString(input.substring(0,1));
			return ret;
		}
	}
}

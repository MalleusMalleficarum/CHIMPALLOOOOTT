/**
 * 
 */
package edu.kit.ipd.creativecrowd.persistentmodel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.kit.ipd.creativecrowd.crowdplatform.PlatformIdentity;
import edu.kit.ipd.creativecrowd.database.DatabaseConnection;
import edu.kit.ipd.creativecrowd.database.Value;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableCalibrationAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableCalibrationQuestion;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRating;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableWorker;
import edu.kit.ipd.creativecrowd.readablemodel.CalibrationAnswer;
import edu.kit.ipd.creativecrowd.readablemodel.Rating;
import edu.kit.ipd.creativecrowd.readablemodel.Worker;

/**
 *  @see edu.kit.ipd.creativecrowd.mutablemodel.MutableWorker
 * @see edu.kit.ipd.creativecrowd.readablemodel.Worker
 * @author Bastian
 *
 */
public class PersistentWorker implements MutableWorker, Worker {

	/** The connection to the database. */
	private DatabaseConnection connection;

	/** The unique id of a persistent worker. */
	private String id;

	/**
	 * Initializes a PersistentWorker
	 * This does NOT change anything at all in the Database.
	 * So if you want to put a new shiny Worker in your Database, then this constructor DOES NOT HELP YOU AT ALL.
	 * 
	 * @param id, a unique id of a persistent worker
	 * @param connection, a connection to the database
	 */
	public PersistentWorker(DatabaseConnection connection, String id) {
		super();
		this.connection = connection;
		this.id = id;
	}
	
	/* (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.Worker#getName()
	 */
	@Override
	public String getName() throws DatabaseException {
		String name = null;
		try {
			String sql = connection.formatString("SELECT name FROM worker WHERE workerid = {?};", Value.fromString(id));
			name = connection.query(sql).iterator().next().iterator().next().asString();
		} catch (SQLException e) {
			throw new Error(e.getMessage());
		} 
		return name;
	}


	/* (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.Worker#getEmail()
	 */
	@Override
	public String getEmail() throws DatabaseException {
		String email = null;
		try {
			String sql = connection.formatString("SELECT email FROM worker WHERE workerid = {?};", Value.fromString(id));
			email = connection.query(sql).iterator().next().iterator().next().asString();
		} catch (SQLException e) {
			throw new Error(e.getMessage());
		} 
		return email;
	}

	/* (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.Worker#getWorkerID()
	 */
	@Override
	public String getID() throws DatabaseException {
		return id;
	}

	/* (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.Worker#getCredit()
	 */
	@Override
	public int getCredit() throws DatabaseException {
		int credit = -1;
		try {
			String sql = connection.formatString("SELECT credit FROM worker WHERE workerid = {?};", Value.fromString(id));
			credit = connection.query(sql).iterator().next().iterator().next().asInt();
		} catch (SQLException e) {
			throw new Error(e.getMessage());
		} 
		return credit;
	}

	/* (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.Worker#getCalibrationAnswers()
	 */
	@Override
	public Iterable<? extends CalibrationAnswer> getCalibrationAnswers() throws DatabaseException {
		List<MutableCalibrationAnswer> cqs = new ArrayList<MutableCalibrationAnswer>();
		
		try {
			String sql = connection.formatString("SELECT  id FROM calibrationanswer WHERE workerid = {?};", Value.fromString(id));
			for (Iterable<Value> rows : connection.query(sql)) {
				cqs.add(new PersistentCalibrationAnswer( this.connection,rows.iterator().next().asString()));
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return cqs;
	}

	/* (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.Worker#getRatings()
	 */
	@Override
	public Iterable<? extends Rating> getRatings() throws DatabaseException {
		List<MutableRating> cqs = new ArrayList<MutableRating>();
		
		try {
			String sql = connection.formatString("SELECT  id FROM rating WHERE workerid = {?};", Value.fromString(id));
			for (Iterable<Value> rows : connection.query(sql)) {
				cqs.add(new PersistentRating( rows.iterator().next().asString(),this.connection));
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return cqs;
	}

	/* (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableWorker#addAnswer()
	 */
	@Override
	public MutableAnswer addAnswer() throws DatabaseException {
		String answerid = connection.generateID("answer");
		try {
			String sql = connection.formatString("INSERT INTO answer (id,workerid) VALUES ({?},{?});", Value.fromString(answerid), Value.fromString(id));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage() + " An error occured creating the answer");
		}
		return new PersistentAnswer(answerid, this.connection);
	}

	/* (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableWorker#getAnswers()
	 */
	@Override
	public Iterable<? extends MutableAnswer> getAnswers() throws DatabaseException {
		List<MutableAnswer> cqs = new ArrayList<MutableAnswer>();
		
		try {
			String sql = connection.formatString("SELECT  id FROM answer WHERE workerid = {?};", Value.fromString(id));
			for (Iterable<Value> rows : connection.query(sql)) {
				cqs.add(new PersistentAnswer( rows.iterator().next().asString(),this.connection));
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return cqs;
	}

	/* (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableWorker#addRating()
	 */
	@Override
	public MutableRating addRating() throws DatabaseException {
		String ratingid = connection.generateID("rating");
		try {
			String sql = connection.formatString("INSERT INTO rating (id,workerid) VALUES ({?},{?});", Value.fromString(ratingid), Value.fromString(id));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage() + " An error occured creating the rating");
		}
		return new PersistentRating(ratingid, this.connection);
	}


	@Override
	public MutableCalibrationAnswer addCalibrationAnswer() throws DatabaseException {
		String calibansid = connection.generateID("calibrationanswer");
		try {
			String sql = connection.formatString("INSERT INTO calibrationanswer (id,workerid) VALUES ({?},{?});", Value.fromString(calibansid), Value.fromString(id));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage() + " An error occured creating the Calibrationanswer");
		}
		return new PersistentCalibrationAnswer(this.connection, calibansid);
	}

	@Override
	public Iterable<? extends MutableCalibrationQuestion> getDoneCalibQuestWorker() throws DatabaseException {
		List<MutableCalibrationQuestion> ret = new ArrayList<MutableCalibrationQuestion>();
		try {
			String sql = connection.formatString("SELECT calibrationquestionid FROM calibrationanswer WHERE workerid = {?} ;", Value.fromString(this.id));
			connection.query(sql);
			for (Iterable<Value> row : connection.query(sql)) {
				ret.add(new PersistentCalibrationQuestion(row.iterator().next().asString(), connection));
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return ret;
	}
	
	@Override
	public boolean increaseCredit(int credit) throws DatabaseException {
		if (credit < 0) {
			return false;
		}
		//Here might occur a race condition
		int currentCredit = getCredit();
		int newCredit = currentCredit + credit;
		try {
			String sql = connection.formatString("UPDATE worker SET credit = {?} WHERE workerid = {?};", Value.fromFloat(newCredit), Value.fromString(id));
			connection.query(sql);
			return true;
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}
	
	@Override
	public boolean decreaseCredit(int credit) throws DatabaseException {
		//Here might occur a race condition
		int currentCredit = getCredit();
		int newCredit = currentCredit - credit;
		if (credit < 0 || newCredit < 0) {
			return false;
		}
		try {
			String sql = connection.formatString("UPDATE worker SET credit = {?} WHERE workerid = {?};", Value.fromFloat(newCredit), Value.fromString(id));
			connection.query(sql);
			return true;
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.Worker#isBlocked()
	 */
	@Override
	public boolean isBlocked() throws DatabaseException {
		try {
			String sql = connection.formatString("SELECT blocked FROM worker WHERE workerid = {?};", Value.fromString(id));
			int temp = connection.query(sql).iterator().next().iterator().next().asInt();
			return temp == 1;
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutableWorker#setBlocked(boolean)
	 */
	@Override
	public void setBlocked(boolean blocked) throws DatabaseException {
		int temp = blocked? 1 : 0;
		try {
			String sql = connection.formatString("UPDATE worker SET blocked = {?} WHERE workerid = {?};",
					Value.fromInt(temp), Value.fromString(id));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		
	}

	@Override
	public PlatformIdentity getPlatform() throws DatabaseException {
		PlatformIdentity result = PlatformIdentity.Unspecified;
		try {
			String sql = connection.formatString("SELECT platform FROM worker WHERE workerid = {?};",
					Value.fromString(id));
			String temp = connection.query(sql).iterator().next().iterator().next().asString();
			try {
				result = PlatformIdentity.valueOf(temp);
			} catch (IllegalArgumentException e) {
				//Return Unspecified then.
			}
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		return result;
	}

	@Override
	public void setName(String name) throws DatabaseException {
		try {
			String sql = connection.formatString("UPDATE worker SET name = {?} WHERE workerid = {?};", 
					Value.fromString(name), Value.fromString(this.id));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	@Override
	public void setEmail(String email) throws DatabaseException {
		try {
			String sql = connection.formatString("UPDATE worker SET email = {?} WHERE workerid = {?};", 
					Value.fromString(email), Value.fromString(this.id));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
		
	}

	@Override
	public void setPlatform(PlatformIdentity platform) throws DatabaseException {
		try {
			String sql = connection.formatString("UPDATE worker SET platform = {?} WHERE workerid = {?};", 
					Value.fromString(platform.name()), Value.fromString(this.id));
			connection.query(sql);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}
}

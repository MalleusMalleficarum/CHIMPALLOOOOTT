package edu.kit.ipd.creativecrowd.mutablemodel;


import edu.kit.ipd.creativecrowd.crowdplatform.PlatformIdentity;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.Worker;

/**
 * adds setter functionality to the Worker class.
 * 
 * @see edu.kit.ipd.creativecrowd.readablemodel.Worker
 * @author Bastian
 */
public interface MutableWorker extends Worker {


	
	/**
	 * Adds a answer given from this worker.
	 *
	 * @return the answer
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public MutableAnswer addAnswer() throws DatabaseException;
	
	/**
	 * Gets the answers given from this worker
	 *
	 * @return an iterable with the answers
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public Iterable<? extends MutableAnswer> getAnswers() throws DatabaseException;
	
	/**
	 * Adds a rating given from this worker.
	 *
	 * @return the rating
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public MutableRating addRating() throws DatabaseException;
	
	
	/**
	 * Adds a calibrationquestion.
	 *
	 * @return the calibrationquestion
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public MutableCalibrationAnswer addCalibrationAnswer() throws DatabaseException;
	
	/**
	 * Increases the amount of credit/money the worker has
	 * @param credit the difference > 0
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public boolean increaseCredit(int credit) throws DatabaseException;
	
	/**
	 * Decreases the amount of credit the worker has
	 * @param credit the difference > 0
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public boolean decreaseCredit(int credit) throws DatabaseException;
	
	/**
	 * Blocks (or unblocks) the worker.
	 * 
	 * @param blocked {@code true} if the worker should be blocked, {@code false} if not
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public void setBlocked(boolean blocked) throws DatabaseException;
	
	/**
	 * Sets the name.
	 * 
	 * @param name the new name
	 * @throws DatabaseException if the SQL request fails
	 */
	public void setName(String name) throws DatabaseException;
	
	
	/**
	 * Sets the email adress.
	 * 
	 * @param email the new email
	 * @throws DatabaseException if the SQL request fails
	 */
	public void setEmail(String email) throws DatabaseException;
	
	/**
	 * Sets the platform this worker belongs to.
	 * 
	 * @param platform the platform
	 * @throws DatabaseException if the SQL request fails
	 */
	public void setPlatform(PlatformIdentity platform) throws DatabaseException;

}

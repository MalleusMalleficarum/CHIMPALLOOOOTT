package edu.kit.ipd.creativecrowd.readablemodel;


import com.fasterxml.jackson.annotation.JsonIgnore;

import edu.kit.ipd.creativecrowd.crowdplatform.PlatformIdentity;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableCalibrationQuestion;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;

/**
 * provides read-only information about a worker. 
 *
 * @author Bastian
 */
public interface Worker {
	
	
	
	/**
	 * gets the name of the worker 
	 * 
	 * @return name of the worker
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public String getName() throws DatabaseException;
	
	/**
	 * gets the emailadress of the worker 
	 * 
	 * @return email of the worker
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public String getEmail() throws DatabaseException;
	
	/**
	 * gets the ID of the worker 
	 * 
	 * @return ID of the worker
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public String getID() throws DatabaseException;
	
	/**
	 * gets the credit of the worker 
	 * 
	 * @return credit of the worker
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public  int getCredit() throws DatabaseException;
	
	/**
	 * gets all CalibrationAnswers wich were answered by this worker
	 *
	 * @return all CalibrationAnswers belonging to this worker as Iterable
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	@JsonIgnore
	public Iterable<? extends CalibrationAnswer> getCalibrationAnswers() throws DatabaseException;
	
	/**
	 * gets all wich were answered by this worker
	 *
	 * @return all Answers belonging to this worker as Iterable
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	@JsonIgnore
	public Iterable<? extends Answer> getAnswers() throws DatabaseException;
	
	/**
	 * gets all Ratings which were done from this worker
	 *
	 * @return all Ratings belonging to this worker as Iterable
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	@JsonIgnore
	public Iterable<? extends Rating> getRatings() throws DatabaseException;
	
	/**
	 * gets all calibquest wich the worker has already answered
	 * @return answered calibquest
	 * @throws DatabaseException
	 */
	@JsonIgnore
	public Iterable<? extends MutableCalibrationQuestion> getDoneCalibQuestWorker() throws DatabaseException;
	
	/**
	 * Returns {@code true} if the worker is globally blocked.
	 * 
	 * @return {@code true} if the worker is blocked, {@code false} if not
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public boolean isBlocked() throws DatabaseException;
	
	/**
	 * @return the platform this worker belongs to.
	 * @throws DatabaseException if the SQL request fails
	 */
	public PlatformIdentity getPlatform() throws DatabaseException;

}

	


package edu.kit.ipd.creativecrowd.mutablemodel;

import java.util.List;

import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;

/**
 * contains and manages multiple worker. Represents a persistent storage for worker. Using this repo one can create, load and delete(anonymizing) worker.
 * The class builds up the Database-Structure (Tables)
 *
 * @author Bastian
 */
public interface WorkerRepo {
	
	/**
	 *  Creates the worker
	 *  
	 * @param id of the worker
	 * @return the mutable worker 
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public MutableWorker createWorker(String id) throws DatabaseException;
	
	/**
	 * Load worker
	 * 
	 * @param id of the worker
	 * @return the mutable worker
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public MutableWorker loadWorker(String id) throws DatabaseException;

	/**
	 *  Makes a worker anonym
	 * @param id of the worker
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public void anonymizeWorker(String id) throws DatabaseException;
	
	/**
	 * Load a list of all worker.
	 * 
	 * @return List of worker, empty if there are none
	 * @throws DatabaseException if the SQL request fails
	 */
	public List<MutableWorker> loadAllWorkers() throws DatabaseException;
}

package edu.kit.ipd.creativecrowd.mutablemodel;

import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;

/**
 * contains and manages multiple experiments. Represents a persistent storage for experiments. Using this repo one can create, load and delete Experiments.
 * The class builds up the Database-Structure (Tables)
 *
 * @author simon & Philipp
 */
public interface ExperimentRepo {

	/**
	 * Creates the experiment.
	 *
	 * @param name the name
	 * @return the mutable experiment
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public MutableExperiment createExperiment(String name) throws DatabaseException;

	/**
	 * Load experiment.
	 *
	 * @param name the name
	 * @return the mutable experiment
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public MutableExperiment loadExperiment(String name) throws DatabaseException;

	/**
	 * Delete experiment.
	 *
	 * @param name the name
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public void deleteExperiment(String name) throws DatabaseException;
}

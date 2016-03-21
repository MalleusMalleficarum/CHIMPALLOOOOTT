package edu.kit.ipd.creativecrowd.mutablemodel;

import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.persistentmodel.IDAlreadyUsedException;
import edu.kit.ipd.creativecrowd.persistentmodel.IDNotFoundException;
import edu.kit.ipd.creativecrowd.readablemodel.ConfigModel;

import java.util.List;

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
	 * @param the configmodel for the experiment
	 * @return the mutable experiment
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 * @throws IDAlreadyUsedException if the name is already used by another experiment
	 */
	public MutableExperiment createExperiment(String name, ConfigModel model) throws DatabaseException, IDAlreadyUsedException;

	/**
	 * Load experiment.
	 *
	 * @param name the name
	 * @return the mutable experiment
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 * @throws IDNotFoundException if an experiment with that name does not exist
	 */
	public MutableExperiment loadExperiment(String name) throws DatabaseException, IDNotFoundException;

	/**
	 * Delete experiment.
	 *
	 * @param name the name
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public void deleteExperiment(String name) throws DatabaseException;
	
	/**
	 * Load a list of all experiments.
	 * 
	 * @return List of experiments, empty if there are none
	 * @throws DatabaseException if the SQL request fails
	 * @author Pascal
	 */
	public List<MutableExperiment> loadAllExperiments() throws DatabaseException;
}

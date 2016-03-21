package edu.kit.ipd.creativecrowd.mutablemodel;

import java.util.List;

import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.persistentmodel.IDAlreadyUsedException;
import edu.kit.ipd.creativecrowd.persistentmodel.IDNotFoundException;
import edu.kit.ipd.creativecrowd.readablemodel.ConfigModel;

/**
 * Contains and manages multiple ConfigModels, represents a persistent storage for ConfigModels. Using this repo one can create, load, and delete ConfigModels.
 * The class builds up the database structure.
 * @author Pascal Gabriel
 *
 */
public interface ConfigModelRepo {
	/**
	 * Creates the ConfigModel.
	 *
	 * @param name the name
	 * @return the mutable experiment
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 * @throws IDAlreadyUsedException if a ConfigModel with that ID already exists
	 */
	public ConfigModel createConfigModel(ConfigModel conf, String name, String expid) throws DatabaseException, IDAlreadyUsedException;

	/**
	 * Load ConfigModel.
	 *
	 * @param name the name
	 * @return the mutable experiment
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 * @throws IDNotFoundException if a ConfigModel with that id does not exist
	 */
	public ConfigModel loadConfigModel(String name) throws DatabaseException, IDNotFoundException;

	/**
	 * Delete ConfigModel.
	 *
	 * @param name the name
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public void deleteConfigModel(String name) throws DatabaseException;
	
	/**
	 * Loads all ConfigModels in the repo.
	 * 
	 * @return a list of all ConfigModels
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 * @author Thomas Friedel
	 */
	public List<ConfigModel> loadAllConfigModels() throws DatabaseException;
}

package edu.kit.ipd.creativecrowd.mutablemodel;

import com.fasterxml.jackson.annotation.JsonIgnore;

import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.Stats;

/**
 * adds the functionality to change stats.
 * 
 * @see edu.kit.ipd.creativecrowd.readablemodel.Stats
 * @author simon
 */
public interface MutableStats extends Stats {

	/**
	 * Adds the preview click.
	 *
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public void addPreviewClick() throws DatabaseException;

	/**
	 * Gets the experiment of the stats.
	 * 
	 * @return the experiment of the stats
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	@JsonIgnore
	public MutableExperiment getExperiment() throws DatabaseException;
}

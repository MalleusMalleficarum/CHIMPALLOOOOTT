package edu.kit.ipd.chimpalot.jsonclasses;

import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.Experiment;
import edu.kit.ipd.creativecrowd.readablemodel.Stats;

/**
 * A javabean to create json-Files from. Provides all data the frontend needs from an Experiment.
 * 
 * @author Thomas Friedel
 *
 */
public class ExperimentDetails {
	
	private String iD;
	private boolean finished;
	private String config;
	private Stats stats;
	
	/**
	 * Initializes an empty bean.
	 */
	public ExperimentDetails() {
		iD = null;
		finished = false;
		config = null;
		stats = null;
	}
	
	/**
	 * Creates details from an experiment
	 * @param exp the experiment
	 * @throws DatabaseException if the SQL request fails
	 */
	public ExperimentDetails(Experiment exp) throws DatabaseException {
		this.iD = exp.getID();
		this.finished = exp.isFinished();
		this.config = exp.getConfig().getID();
		this.stats = exp.getStats();		
	}
	
	/**
	 * @return the id of the experiment
	 */
	public String getID() {
		return this.iD;
	}
	
	/**
	 * @return if the experiment has finished or not
	 */
	public boolean isFinished() {
		return this.finished;
	}
	
	/**
	 * @return the id of the associated config
	 */
	public String getConfig() {
		return this.config;
	}
	
	/**
	 * @return some stats about the experiment
	 */
	public Stats getStats() {
		return this.stats;
	}
}

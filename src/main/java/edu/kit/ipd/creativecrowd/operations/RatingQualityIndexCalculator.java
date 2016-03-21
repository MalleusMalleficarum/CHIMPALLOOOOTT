package edu.kit.ipd.creativecrowd.operations;

import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;

/**
 * strategyinterface for calculating the qualityindex of an Rating
 * 
 * @author Anika + Jonas
 */
public interface RatingQualityIndexCalculator extends AbstractStrategy {

	/**
	 * calculates the rating quality index of all ratings in this experiment
	 * 
	 * @param ex experiment containing ratings
	 * @throws DatabaseException if the experiment couldn´t be loaded succesfully
	 */
	void run(MutableExperiment ex) throws DatabaseException;

}

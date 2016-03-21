package edu.kit.ipd.creativecrowd.operations;

import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;

/**
 * Strategyinterface for classes calculating the quality index for an answer
 * 
 * @author Anika & Jonas
 */
public interface AnswerQualityIndexCalculator extends AbstractStrategy {
	/**
	 * calculates the actual qualityIndex for all answers in an experiment
	 * 
	 * @param ex experiment containing the answers which qualityIndex is called
	 * @throws DatabaseException
	 */
	void run(MutableExperiment ex) throws DatabaseException;

}

package edu.kit.ipd.creativecrowd.operations;

import edu.kit.ipd.creativecrowd.mutablemodel.MutableAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;

/**
 * strategy interface for deciding whether a answer has enough ratings to make a saying about her quality
 * 
 * @author Anika + Jonas
 */
public interface RatingSufficiencyDecider extends AbstractStrategy {
	boolean hasEnoughRatings(MutableAnswer a, MutableExperiment ex) throws DatabaseException;
}

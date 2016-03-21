package edu.kit.ipd.creativecrowd.operations.strategies;

import edu.kit.ipd.creativecrowd.mutablemodel.MutableAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRating;
import edu.kit.ipd.creativecrowd.operations.RatingSufficiencyDecider;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;

/**
 * decides that an answer is rating sufficiently if it has the specified number of ratings
 * 
 * @author Anika
 *         StrategyParams
 *         -- n: number of ratings which are needed for a RatingSufficiency
 */
public class FixedRatingsPerAnswerDecider extends StrategyWithParams implements RatingSufficiencyDecider {

	@Override
	public boolean hasEnoughRatings(MutableAnswer a, MutableExperiment ex) throws DatabaseException {
		int n = 0;
		for (@SuppressWarnings("unused")
		MutableRating r : a.getRatings()) {
			n++;
		}

		return n >= getIntParam("rsd.n", 5);
	}

}

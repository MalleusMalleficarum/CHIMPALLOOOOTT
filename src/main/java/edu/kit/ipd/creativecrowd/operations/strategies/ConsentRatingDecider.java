package edu.kit.ipd.creativecrowd.operations.strategies;

import java.util.ArrayList;
import java.util.List;

import edu.kit.ipd.creativecrowd.mutablemodel.MutableAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRating;
import edu.kit.ipd.creativecrowd.operations.RatingSufficiencyDecider;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;

/**
 * tests the consence of the ratings given answer. if they found a consent the answer is sufficiently rated
 * 
 * @author Anika
 *         StrategyParams
 *         quorum: necessary number of ratings to have a valid decision
 *         allowed_stdev: standard deviation
 */
public class ConsentRatingDecider extends StrategyWithParams implements RatingSufficiencyDecider {

	@Override
	public boolean hasEnoughRatings(MutableAnswer a, MutableExperiment ex) throws DatabaseException {
		// TODO statistics pls
		List<Float> vals = new ArrayList<Float>();
		for (MutableRating r : a.getRatings()) {
			vals.add(r.getSelectedRatingOption().getValue());
		}

		// check quorum
		if (vals.size() < getIntParam("rsd.quorum", 3)) {
			return false;
		}

		float sum = 0;
		for (float x : vals) {
			sum += x;
		}
		float avg = sum / vals.size();

		// c.f. http://en.wikipedia.org/wiki/Standard_deviation#Basic_examples
		float diffsum = 0;
		for (float x : vals) {
			diffsum += (x - avg) * (x - avg);
		}
		float stddev = (float) Math.sqrt(diffsum);

		// bail if the standard deviation is too large, i.e. the consent hasn't been found yet
		if (stddev > getFloatParam("rsd.allowed_stddev", 1.0f)) {
			return false;
		}

		return true;
	}

}

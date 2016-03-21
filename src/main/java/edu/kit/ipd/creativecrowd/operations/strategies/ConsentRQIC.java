package edu.kit.ipd.creativecrowd.operations.strategies;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.HashSet;

import edu.kit.ipd.creativecrowd.mutablemodel.MutableAssignment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRating;
import edu.kit.ipd.creativecrowd.operations.RatingQualityIndexCalculator;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.Answer;
import edu.kit.ipd.creativecrowd.readablemodel.Rating;
import edu.kit.ipd.creativecrowd.readablemodel.RatingOption;

/**
 * Estimates the quality of a rating by checking whether the rating is in accord
 * with the consent determined by the entirety of all ratings for the given answer.
 * If the consent was selected or no consent is present, the rating will receive
 * a quality of '1', if the consent was missed, the quality will be zero.
 *
 * @author Jonas
 */
public class ConsentRQIC extends StrategyWithParams implements RatingQualityIndexCalculator {

	@Override
	public void run(MutableExperiment ex) throws DatabaseException {
		for (MutableAssignment as : ex.getAssignments()) {
			for (MutableRating r : as.getTaskConstellation().getRatings()) {
				Answer a = r.getAnswer();
				Map<RatingOption, Integer> counts = new HashMap<RatingOption, Integer>();

				for (Rating viewedRating : a.getRatings()) {
					RatingOption viewedRO = viewedRating.getSelectedRatingOption();
					Integer n = counts.get(viewedRO);
					if (n == null) {
						n = 1;
					} else {
						n++;
					}
					counts.put(viewedRO, n);
				}

				int max = 0;
				// if no ratings are present, any choice is consenting
				Set<RatingOption> consentOptions = new HashSet<RatingOption>();
				for (RatingOption anyRO : ex.getRatingOptions()) {
					consentOptions.add(anyRO);
				}

				for (Entry<RatingOption, Integer> e : counts.entrySet()) {
					if (e.getValue() > max) {
						// if we found a new consent, clear the set of choices in the consent
						max = e.getValue();
						consentOptions = new HashSet<RatingOption>();
					}

					// in any case, add the thing to the current consent set if it fits the max
					if (e.getValue() == max) {
						consentOptions.add(e.getKey());
					}
				}

				float finalQualityIndex = 0;
				if (consentOptions.contains(r.getSelectedRatingOption())) {
					finalQualityIndex = 1;
				}
				r.setFinalQualityIndex(finalQualityIndex);
			}
		}

	}

}

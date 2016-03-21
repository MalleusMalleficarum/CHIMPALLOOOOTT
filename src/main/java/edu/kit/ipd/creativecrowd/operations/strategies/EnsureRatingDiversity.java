package edu.kit.ipd.creativecrowd.operations.strategies;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.IntPredicate;

import edu.kit.ipd.creativecrowd.mutablemodel.MutableAssignment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRating;
import edu.kit.ipd.creativecrowd.operations.RatingQualityIndexCalculator;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.RatingOption;

/**
 * This rating quality index calculator strategy assigns a bad quality to ratings
 * where the author of the ratings has consistently just given the same rating option for every
 * rating provided.
 * Strategy parameters:
 * - rqic.threshold -- float specifying which fraction of ratings there have to be present
 * for a single rating option to mark the workers as "biased", i.e. "0.8"
 * to disqualify all workers who choose the same option for >80% of ratings
 * - rqic.quorum -- Workers that complete less than quorum ratings are excluded from the calculation.
 * - rqic.val_good -- The quality index for unbiased workers
 * - rqic.val_bad -- The quality index for biased workers
 * 
 * @author jonas
 */
public class EnsureRatingDiversity extends StrategyWithParams implements RatingQualityIndexCalculator {

	@Override
	public void run(MutableExperiment ex) throws DatabaseException {
		for (MutableAssignment as : ex.getAssignments()) {
			Map<RatingOption, Integer> occurences = new HashMap<RatingOption, Integer>();
			boolean isBiased = false;
			boolean meetsQuorum = false;

			int n = 0;
			for (MutableRating r : as.getTaskConstellation().getRatings()) {
				n++;
				RatingOption thisOne = r.getSelectedRatingOption();
				System.out.println(thisOne.getText());
				int oldCount = occurences.getOrDefault(thisOne, 0);
				int newCount = oldCount + 1;
				occurences.put(thisOne, newCount);
			}
			meetsQuorum = n >= getIntParam("rqic.quorum", 1);

			List<Integer> counts = new LinkedList<Integer>();
			for (Entry<RatingOption, Integer> k : occurences.entrySet()) {
				counts.add(k.getValue());
			}

			int thresholdCount = (int) (n * getFloatParam("rqic.threshold", 0.8f));
			IntPredicate countOverThreshold = x -> x > thresholdCount;
			long countsOverThreshold = counts.stream().mapToInt(Integer::intValue).filter(countOverThreshold).count();
			isBiased = countsOverThreshold > 0;

			float qualityIndex = getFloatParam("rqic.val_good", 1.0f);
			if (isBiased && meetsQuorum) {
				qualityIndex = getFloatParam("rqic.val_bad", 0.0f);
			}

			for (MutableRating r : as.getTaskConstellation().getRatings()) {
				r.setFinalQualityIndex(qualityIndex);
			}
		}

	}

}

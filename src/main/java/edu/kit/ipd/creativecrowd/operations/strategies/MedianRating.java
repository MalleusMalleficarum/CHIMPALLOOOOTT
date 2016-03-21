package edu.kit.ipd.creativecrowd.operations.strategies;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import edu.kit.ipd.creativecrowd.mutablemodel.MutableAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.operations.AnswerQualityIndexCalculator;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.Rating;

/**
 * This answer quality index calculator computes, for each answer, the median of the numerical
 * rating values given for the answer, and sets the answer quality to be this median.
 * 
 * @author jonas
 */
public class MedianRating extends StrategyWithParams implements AnswerQualityIndexCalculator {

	@Override
	public void run(MutableExperiment ex) throws DatabaseException {
		for (MutableAnswer a : ex.getCreativeTask().getAnswers()) {
			List<Float> ratings = new LinkedList<Float>();
			for (Rating r : a.getRatings()) {
				ratings.add(r.getSelectedRatingOption().getValue());
			}

			Collections.sort(ratings);
			int middle_i = ratings.size() / 2;

			double median = 0;

			if (ratings.size() % 2 == 1) {
				median = ratings.get(middle_i);
			} else if (ratings.size() > 0) {
				median = (ratings.get(middle_i - 1) + ratings.get(middle_i)) / 2.0;
			}

			a.setFinalQualityIndex((float) median);
		}
	}
}

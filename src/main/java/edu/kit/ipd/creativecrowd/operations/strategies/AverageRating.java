package edu.kit.ipd.creativecrowd.operations.strategies;

import edu.kit.ipd.creativecrowd.mutablemodel.MutableAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRating;
import edu.kit.ipd.creativecrowd.operations.AnswerQualityIndexCalculator;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;

/**
 * This class calcs the quality indexes of answers of an CreativeTask
 * 
 * @author Anika
 */
public class AverageRating extends StrategyWithParams implements AnswerQualityIndexCalculator {
	/**
	 * this method calcs the weighted arithmetic mean of all ratings
	 * The weight is the ratingqualityindex of the rating
	 * the value is the floatValue of the chosen RatingOption in the Rating
	 * 
	 * @param ex the experiment whose answers need to have an qualityindex
	 * @throws DatabaseException if Ratings or Answers can´t be loaded from database
	 */
	@Override
	public void run(MutableExperiment ex) throws DatabaseException {
		for (MutableAnswer ans : ex.getCreativeTask().getAnswers()) {
			float sumOfWeights = 0;
			float numericRatings = 0;
			for (MutableRating rat : ans.getRatings()) {
				sumOfWeights += rat.getFinalQualityIndex();
				numericRatings += rat.getSelectedRatingOption().getValue() * rat.getFinalQualityIndex();
			}/*-?|Anika|Anika|c2|?*/
			float qualityIndex;
			if (sumOfWeights == 0) {
				qualityIndex = 0;
			}
			else {
				qualityIndex = numericRatings / sumOfWeights;
			}
			ans.setFinalQualityIndex(qualityIndex);
		}

	}

}

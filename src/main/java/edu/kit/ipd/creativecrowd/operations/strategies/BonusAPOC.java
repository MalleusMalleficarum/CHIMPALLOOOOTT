package edu.kit.ipd.creativecrowd.operations.strategies;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

import edu.kit.ipd.creativecrowd.mutablemodel.MutableAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAssignment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRating;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;

/**
 * This class generates the payment for a worker
 * and allows giving a specified BonusAmount to a specified amount of people.
 * *******
 * StrategyParams:
 * workers_to_receive_bonus: number of worker among whose the bonus is divided (absolute number)
 * bonus_pool: amount of cents of the bonus which is divided between the workers4
 * weigh_of_answer: weight of answers in calculating the QualityIndexOfAnAssignment
 *
 * @author Anika
 */
public class BonusAPOC extends DefaultAPOC {
	@Override
	public void run(MutableExperiment ex) throws DatabaseException {
		// we want to build a max-heap of assignments
		Comparator<? super MutableAssignment> inverseAssignmentComparator = (a1, a2) -> calculateAssignmentQualityIndex(a2).compareTo(calculateAssignmentQualityIndex(a1));
		Queue<MutableAssignment> assignmentsSortedByQuality = new PriorityQueue<MutableAssignment>(inverseAssignmentComparator);
		float workersToReceiveBonus = this.getFloatParam("apoc.workers_to_receive_bonus", 1);
		if (workersToReceiveBonus < 1.0) {
			int n = 0;
			for (@SuppressWarnings("unused") MutableAssignment a : ex.getAssignments()) {
				n++;
			}
			workersToReceiveBonus = workersToReceiveBonus * n;
		}
		float bonusPerWorker;
		if(workersToReceiveBonus < 1.0) {
			bonusPerWorker = 0;
		}
		else {
			bonusPerWorker = (int) this.getIntParam("apoc.bonus_pool", 10) / workersToReceiveBonus;
		}
		for (MutableAssignment as : ex.getAssignments()) {
			assignmentsSortedByQuality.add(as);
		}
		int position = 0;
		
		while(!assignmentsSortedByQuality.isEmpty()) {
			MutableAssignment as = assignmentsSortedByQuality.remove();
			int result = getBonusPayOutBasedOnCompletedTask(ex, as);
			/*-?|Anika|Anika|c10|?*/
			if (position <= workersToReceiveBonus - 1) {
				result += bonusPerWorker;
			}
			as.setPaymentOutcome(true, result);
			position++;
		}
	}

	/**
	 * calculates the qualityindex of an assignment weighs answers more than ratings
	 *
	 * @param a1 assignment whose qualityindex is needed
	 * @return
	 */
	private Float calculateAssignmentQualityIndex(MutableAssignment a1) {
		float answerWeight = this.getFloatParam("apoc.weight_of_answer", 3);
		float sumOfIndices = 0;
		int nrOfResults = 0;
		try {
			for (MutableAnswer ans : a1.getTaskConstellation().getAnswers()) {
				if(!ans.isInvalid()) {
				nrOfResults++;
				sumOfIndices += answerWeight * ans.getFinalQualityIndex();
				}
			}
			for (MutableRating rat : a1.getTaskConstellation().getRatings()) {
				nrOfResults++;
				sumOfIndices += rat.getFinalQualityIndex();
			}
		} catch (DatabaseException e) {
			return new Float(0);
		}/*-?|Anika|Anika|c3|?*/
		if (nrOfResults == 0) {
			return new Float(0);
		}
		else {
			return sumOfIndices / nrOfResults;
		}

	}

	@Override
	public int getBonusPool() {
		return this.getIntParam("apoc.bonus_pool", 0);
	}

}

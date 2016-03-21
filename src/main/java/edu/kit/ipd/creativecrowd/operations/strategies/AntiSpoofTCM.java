package edu.kit.ipd.creativecrowd.operations.strategies;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import edu.kit.ipd.creativecrowd.mutablemodel.MutableAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAssignment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRatingTask;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableTaskConstellation;
import edu.kit.ipd.creativecrowd.operations.NoValidTasksException;
import edu.kit.ipd.creativecrowd.operations.TaskConstellationMutator;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.Answer;
import edu.kit.ipd.creativecrowd.readablemodel.Rating;

public class AntiSpoofTCM extends BoehmscherTCM implements
		TaskConstellationMutator {
	@Override
	protected boolean generateAndAddRatingTask(MutableTaskConstellation tc,
			MutableExperiment ex, MutableAssignment as) throws NoValidTasksException,
			DatabaseException {
		// Create new empty task
		MutableRatingTask rt = ex.addRatingTask();
		tc.addRatingTask(rt);

		// Build a blacklist of answers that we never want to choose:
		// Skip answers that we already have in some RatingTask within the
		// constellation
		List<MutableAnswer> ineligibleAnswers = new LinkedList<MutableAnswer>();
		for (int pos = 0; pos < tc.getTaskCount(); pos++) {
			MutableRatingTask existing_rt = tc.getMutableRatingTask(pos);
			// Not a rating task at this position
			if (existing_rt == null) {
				continue;
			}

			for (MutableAnswer existing_to_be_rated : existing_rt
					.getAnswersToBeRated()) {
				ineligibleAnswers.add(existing_to_be_rated);
			}
		}

		// Skip answers that were authored in some CreativeTask in the
		// constellation
		for (MutableAnswer ans : as.getTaskConstellation().getAnswers()) {
			ineligibleAnswers.add(ans);
		}
		// skip answers which haven´t been submitted yet or spoof answers
		for (MutableAnswer ans : ex.getCreativeTask().getAnswers()) {
			if (!ans.isSubmitted()) {
				ineligibleAnswers.add(ans);
			}
			if(ans.isInvalid()) {
				ineligibleAnswers.add(ans);
			}
		}
		for (MutableAssignment ass: ex.getAssignments()){
			MutableTaskConstellation taskc = ass.getTaskConstellation();
			ArrayList<MutableAnswer> tcans = (ArrayList<MutableAnswer>) taskc.getAnswers();
			MutableAnswer a = tcans.get(0);
			if(a.isSufficientlyRated()){
				if(a.isInvalid()) {
					for (MutableAnswer ans: taskc.getAnswers()) {
						ineligibleAnswers.add(ans);
					}
				}
			}
			else {
				for (MutableAnswer ans: taskc.getAnswers()) {
					ineligibleAnswers.add(ans);
				}
				ineligibleAnswers.remove(a);
			}
		}
		// build a prioritized set of eligible answers
		Comparator<? super MutableAnswer> answerCountComparator = (a1, a2) -> countRatings(
				a1).compareTo(countRatings(a2));
		Queue<MutableAnswer> eligibleAnswersWithLeastRatings = new PriorityQueue<MutableAnswer>(answerCountComparator);

		// Find an answer that:
		for (MutableAnswer candidate : ex.getCreativeTask().getAnswers()) {
			// 1. doesn't have enough ratings yet
			if (candidate.isSufficientlyRated()) {
				continue;
			}

			// 2. isn't in our blacklist
			if (ineligibleAnswers.indexOf(candidate) != -1) {
				continue;
			}

			// Okay, we got it!
			eligibleAnswersWithLeastRatings.add(candidate);
		}

		// Now, add as many answers as needed to the RatingTask
		for (int i = 0; i < this.getIntParam("tcm.ratings_per_task", 3); i++) {
			if (!eligibleAnswersWithLeastRatings.isEmpty()) {
				/*-?|Test Repo-Review|Philipp|c7|?*/
				MutableAnswer a = eligibleAnswersWithLeastRatings.remove();
				rt.addAnswerToBeRated(a);
			} else {
				/*-?|Test Repo-Review|Philipp|c9|?*/
			}/*-?|Anika|Anika|c6|?*/
			int count = 0;
			for (@SuppressWarnings("unused") Answer ans : rt.getAnswersToBeRated()) {
				count++;
			}
			if (count == 0) {
				rt.addAnswerToBeRated(ineligibleAnswers.get(0));
				tc.setAgainButton(false);
				tc.setSubmitButton(true);
				tc.setNextButton(false);
				return false;
			}


		}
		return true;
	}

	private Integer countRatings(MutableAnswer a) {
		int n = 0;
		try {
			for (@SuppressWarnings("unused")
			Rating r : a.getRatings()) {
				n++;
			}
		} catch (DatabaseException e) {
			System.out.println("Database error while comparing: "
					+ e.getMessage());
			return 0;
		}
		return n;
	}

}

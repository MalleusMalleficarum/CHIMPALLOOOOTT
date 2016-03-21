package edu.kit.ipd.creativecrowd.operations.strategies;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.SortedSet;
import java.util.TreeSet;

import edu.kit.ipd.creativecrowd.mutablemodel.MutableAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAssignment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableCreativeTask;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRatingTask;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableTaskConstellation;
import edu.kit.ipd.creativecrowd.operations.NoValidTasksException;
import edu.kit.ipd.creativecrowd.operations.TaskConstellationMutator;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.Answer;
import edu.kit.ipd.creativecrowd.readablemodel.Assignment;
import edu.kit.ipd.creativecrowd.readablemodel.Button;
import edu.kit.ipd.creativecrowd.readablemodel.CreativeTask;
import edu.kit.ipd.creativecrowd.readablemodel.Rating;
import edu.kit.ipd.creativecrowd.readablemodel.RatingTask;
import edu.kit.ipd.creativecrowd.readablemodel.Task;

/**
 * This class implements the TaskConstellationMutator strategy interface. It
 * responds to user choices ("give me another task of this type",
 * "skip to the next type of task") by mutating the worker's task constellation.
 *
 * @author jonas
 */
public class FreeformTaskConstellationMutator extends StrategyWithParams
		implements TaskConstellationMutator {

	@Override
	public MutableTaskConstellation run(MutableAssignment ass,
			MutableExperiment ex, Button btn) throws NoValidTasksException,
			DatabaseException {
		MutableTaskConstellation tc = ass.getTaskConstellation();
		switch (btn) {
		case Again:
			int nrOfCreativeTasks = 0;
			int nrOfRatingTasks = 0;
			for (Task t : tc.getTasks()) {
				if (t instanceof CreativeTask) {
					nrOfCreativeTasks++;
				}
				if (t instanceof RatingTask) {
					nrOfRatingTasks++;
				}
			}
			if (nrOfCreativeTasks < ex.getMaxNumberOfAnswersPerAssignment()
					&& tc.getCurrentTask() instanceof CreativeTask) {
				tc.addCreativeTask((MutableCreativeTask) ass.getExperiment()
						.getCreativeTask());
				tc.setCurrentTask(tc.getTaskCount() - 1);
			}

			else if (nrOfRatingTasks < ex.getMaxNumberOfRatingsPerAssignment()
					&& tc.getCurrentTask() instanceof RatingTask) {
				if (!generateAndAddRatingTask(tc, ex, ass)) {
					tc.setCurrentTask(tc.getTaskCount() - 1);
					return tc;
				}
				tc.setCurrentTask(tc.getTaskCount() - 1);
			} else {
				throw new NoValidTasksException();
			}
			break;
		case Next:
			if (!generateAndAddRatingTask(tc, ex, ass)) {
				tc.setCurrentTask(tc.getTaskCount() - 1);
				return tc;
			}
			tc.setCurrentTask(tc.getTaskCount() - 1);
			break;
		case Start:
			tc.addCreativeTask((MutableCreativeTask) ass.getExperiment()
					.getCreativeTask());
			tc.setCurrentTask(0);
			break;
		case Submit:
			// no need to modify the task constellation here
			break;
		default:
			break;
		}
		// clicking next is allowed if the current task is still a creative
		// task but not if its the first assignment in this exp
		int count = 0;
		for (MutableAnswer ans : ex.getCreativeTask().getAnswers()) {
			if (ans.isSubmitted()) {
				count++;
			}
		}
		boolean nextAllowed = true;
		if (count == 0) {
			nextAllowed = false;
		} else {
			if (tc.getCurrentTask() instanceof CreativeTask) {
				nextAllowed = true;
			} else {
				nextAllowed = false;
			}
		}

		tc.setNextButton(nextAllowed);

		// repeating is allowed if there are less than allowed trasks
		int nrOfCreativeTasks = 0;
		int nrOfRatingTasks = 0;
		for (Task t : tc.getTasks()) {
			if (t instanceof CreativeTask) {
				nrOfCreativeTasks++;
			}
			if (t instanceof RatingTask) {
				nrOfRatingTasks++;
			}
		}
		if (tc.getCurrentTask() instanceof CreativeTask
				&& nrOfCreativeTasks < ex.getMaxNumberOfAnswersPerAssignment()) {
			tc.setAgainButton(true);
		} else if (tc.getCurrentTask() instanceof RatingTask
				&& nrOfRatingTasks < ex.getMaxNumberOfRatingsPerAssignment()) {
			tc.setAgainButton(true);
		} else {
			tc.setAgainButton(false);
		}

		tc.setSubmitButton(true);
		return ass.getTaskConstellation();
	}

	/**
	 * Generate and add a new rating task for the given Task constellation. This
	 * also selects the answers that the worker will have to give ratings for
	 * when performing the rating task. Also makes sure that one doesn't have to
	 * rate one's own answers.
	 *
	 * @param tc
	 *        The task constellation to add the new RatingTask to.
	 * @param ex
	 *        The experiment to draw answers from.
	 * @param as
	 *        The assignment to create the answer blacklist from.
	 * @throws NoValidTasksException
	 *         if the source experiment doesn't contain any eligible answers
	 *         to be added to the constellation.
	 * @throws DatabaseException
	 */
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

		// Skip answers that we authored in some CreativeTask in the
		// constellation
		for (MutableAnswer ans : as.getTaskConstellation().getAnswers()) {
			ineligibleAnswers.add(ans);
		}
		// skip answers which haven´t been submitted yet
		for (MutableAnswer ans : ex.getCreativeTask().getAnswers()) {
			if (!ans.isSubmitted()) {
				ineligibleAnswers.add(ans);
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
			for (Answer ans : rt.getAnswersToBeRated()) {
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

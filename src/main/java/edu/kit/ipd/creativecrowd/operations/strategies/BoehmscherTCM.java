package edu.kit.ipd.creativecrowd.operations.strategies;

import edu.kit.ipd.creativecrowd.mutablemodel.MutableAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAssignment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableTaskConstellation;
import edu.kit.ipd.creativecrowd.operations.NoValidTasksException;
import edu.kit.ipd.creativecrowd.operations.TaskConstellationMutator;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.Button;
import edu.kit.ipd.creativecrowd.readablemodel.CreativeTask;
import edu.kit.ipd.creativecrowd.readablemodel.RatingTask;
import edu.kit.ipd.creativecrowd.readablemodel.Task;

/**
 * This class provides the order of an experiment for our experiment.
 * In Phase 1 only creative tasks are given to workers
 * in phase 2 creative tasks an worker tasks are given to workers
 * in phase 3 only rating tasks are given to workers
 * 
 * @author Anika
 */
public class BoehmscherTCM extends FreeformTaskConstellationMutator implements TaskConstellationMutator {

	@Override
	public MutableTaskConstellation run(MutableAssignment ass,
			MutableExperiment ex, Button btn) throws NoValidTasksException,
			DatabaseException {
		MutableTaskConstellation tc = ass.getTaskConstellation();
		int nrOfAnswers = 0;
		for (MutableAnswer ans : ex.getCreativeTask().getAnswers()) {
			if (ans.isSubmitted()) {
				nrOfAnswers++;
			}
		}
		int phase = 0;
		if (nrOfAnswers < this.getIntParam("tcm.answersBeforeRatings", 2)) {
			phase = 1;
		}
		else if (nrOfAnswers < (this.getIntParam("tcm.answersBeforeRatings", 2) + this.getIntParam("tcm.parallelAssignments", 1))) {
			phase = 2;
		}
		else {
			phase = 3;
		}

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
				tc.addCreativeTask(ex.getCreativeTask());
				tc.setCurrentTask(tc.getTaskCount() - 1);
			}

			else if (nrOfRatingTasks < ex.getMaxNumberOfRatingsPerAssignment()
					&& tc.getCurrentTask() instanceof RatingTask) {
				if (!generateAndAddRatingTask(tc, ex, ass)) {
					tc.setCurrentTask(tc.getTaskCount() - 1);
					return tc;
				}
				tc.setCurrentTask(tc.getTaskCount() - 1);
			}
			else {
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
			if (phase == 3) {
				if (!generateAndAddRatingTask(tc, ex, ass)) {
					tc.setCurrentTask(tc.getTaskCount() - 1);
					return tc;
				}
				tc.setCurrentTask(tc.getTaskCount() - 1);
			}
			else {
				tc.addCreativeTask(ex.getCreativeTask());
				tc.setCurrentTask(0);
			}
			break;
		case Submit:
			// no need to modify the task constellation here
			break;
		default:
			break;
		}

		// clicking next is allowed if the current task is still a creative
		// task but not if its the first assignment in this exp
		boolean nextAllowed = true;
		if (phase != 2) {
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
		if (tc.getCurrentTask() instanceof CreativeTask && nrOfCreativeTasks < ex.getMaxNumberOfAnswersPerAssignment()) {
			tc.setAgainButton(true);
		}
		else if (tc.getCurrentTask() instanceof RatingTask && nrOfRatingTasks < ex.getMaxNumberOfRatingsPerAssignment()) {
			tc.setAgainButton(true);
		}
		else {
			tc.setAgainButton(false);
		}

		tc.setSubmitButton(true);
		return ass.getTaskConstellation();
	}

}

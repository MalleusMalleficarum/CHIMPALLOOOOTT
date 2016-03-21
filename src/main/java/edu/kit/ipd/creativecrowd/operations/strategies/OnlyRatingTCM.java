package edu.kit.ipd.creativecrowd.operations.strategies;

import edu.kit.ipd.creativecrowd.mutablemodel.MutableAssignment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableTaskConstellation;
import edu.kit.ipd.creativecrowd.operations.NoValidTasksException;
import edu.kit.ipd.creativecrowd.operations.TaskConstellationMutator;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.Button;
import edu.kit.ipd.creativecrowd.readablemodel.RatingTask;
import edu.kit.ipd.creativecrowd.readablemodel.Task;

/**
 * This strategy is used if the experiment should stop giving out creative tasks and only the existing
 * answers have to be rated. This strategy is never loaded when creating a new experiment.
 * 
 * @author Thomas Friedel
 */
public class OnlyRatingTCM extends FreeformTaskConstellationMutator implements TaskConstellationMutator {

	@Override
	public MutableTaskConstellation run(MutableAssignment ass, MutableExperiment ex, Button btn)
			throws NoValidTasksException, DatabaseException {
		MutableTaskConstellation tc = ass.getTaskConstellation();
		switch(btn) {
		case Again:
			int nrOfRatingTasks = 0;
			for (Task t : tc.getTasks()) {
				if (t instanceof RatingTask) {
					nrOfRatingTasks++;
				}
			}
			if (nrOfRatingTasks < ex.getMaxNumberOfRatingsPerAssignment() &&
					tc.getCurrentTask() instanceof RatingTask) {
				if (!this.generateAndAddRatingTask(tc, ex, ass)) {
					tc.setCurrentTask(tc.getTaskCount() - 1);
					return tc;
				}
				tc.setCurrentTask(tc.getTaskCount() - 1);
			} else {
				throw new NoValidTasksException();
			}
			break;
		case Start:
			if (!this.generateAndAddRatingTask(tc, ex, ass)) {
				tc.setCurrentTask(tc.getTaskCount() - 1);
				return tc;
			}
			tc.setCurrentTask(tc.getTaskCount() - 1);
			break;
		default:
			break;
		}
		
		tc.setNextButton(false);
		
		int nrOfRatingTasks = 0;
		for (Task t : tc.getTasks()) {
			if (t instanceof RatingTask) {
				nrOfRatingTasks++;
			}
		}
		if (nrOfRatingTasks < ex.getMaxNumberOfRatingsPerAssignment() && 
				tc.getCurrentTask() instanceof RatingTask) {
			tc.setAgainButton(true);
		} else {
			tc.setAgainButton(false);
		}
		
		tc.setSubmitButton(true);
		
		return ass.getTaskConstellation();		
	}

}

package edu.kit.ipd.creativecrowd.operations;

import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;

/**
 * TotalAssignmentCountController calculates the number of Assignments which can
 * be published on MTURK
 * 
 * @author Anika + Jonas
 */
public interface TotalAssignmentCountController extends AbstractStrategy {
	int run(MutableExperiment experiment, int maxPayoutPerAssignmentCents,
			int bonusPoolCents) throws DatabaseException;
}

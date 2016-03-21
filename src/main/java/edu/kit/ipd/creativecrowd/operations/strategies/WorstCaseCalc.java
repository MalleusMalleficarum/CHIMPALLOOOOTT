package edu.kit.ipd.creativecrowd.operations.strategies;

import edu.kit.ipd.creativecrowd.connector.ModelException;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.operations.TotalAssignmentCountController;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;

/**
 * calculates a pessimistic number of assignments
 * 
 * @author Anika
 */
public class WorstCaseCalc extends StrategyWithParams implements TotalAssignmentCountController {

	@Override
	public int run(MutableExperiment experiment, int maxPayoutPerAssignmentCents, int bonusPoolCents) throws DatabaseException {
		int budgetCentsWithoutPool = experiment.getBudgetCents() - bonusPoolCents;
		if (maxPayoutPerAssignmentCents == 0) {
			try {
				throw new ModelException("Something went wrong with maxPayoutPerAssignment");
			} catch (ModelException e) {/*-?|Anika|Anika|c7|?*/
				e.printStackTrace();
			}
		}
		return budgetCentsWithoutPool / maxPayoutPerAssignmentCents;
	}

}

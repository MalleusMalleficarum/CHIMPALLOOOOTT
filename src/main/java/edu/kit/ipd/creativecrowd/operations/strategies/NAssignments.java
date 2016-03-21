package edu.kit.ipd.creativecrowd.operations.strategies;

import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.operations.TotalAssignmentCountController;

/**
 * this strategy is used if the requester has specified a fixed number of assignments to create
 * 
 * @author Anika
 */
public class NAssignments extends StrategyWithParams implements TotalAssignmentCountController {

	@Override
	public int run(MutableExperiment experiment, int maxPayoutPerAssignmentCents, int bonusPool) {
		return this.getIntParam("tacc.n", 5);
	}

}

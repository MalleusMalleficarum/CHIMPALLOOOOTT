package edu.kit.ipd.creativecrowd.operations;

import edu.kit.ipd.creativecrowd.mutablemodel.MutableAssignment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableTaskConstellation;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.Button;

/**
 * @author Anika
 *         represents the transaction for starting a new TaskConstellation or adding new Tasks
 */
public class UpdateTaskConstellationTransaction extends Transaction {
	/**
	 * This method loads the strategy for mutating the TaskConstellation and runs the mutation
	 * 
	 * @param experiment experiment the assignment belongs to
	 * @param as Assignment with the taskconstellation
	 * @param button contaisn the button the worker has chosen
	 * @return the taskconstellation with the new current task
	 * @throws NoValidTasksException when trying to request an invalid change to the constellation.
	 * @throws DatabaseException when an internal database error occurs
	 */
	public MutableTaskConstellation run(MutableExperiment experiment, MutableAssignment as, Button button) throws NoValidTasksException, DatabaseException {
		TaskConstellationMutator tcm;
		try {
			tcm = this.loadTCMStrategy(experiment.getStrategyParams());
		} catch (StrategyNotFoundException e) {
			// If this happens now, it's bad. We validate the strategy existence
			// while creating the experiment.
			throw new Error("Strategy class disappeared! " + e.getMessage());
		}
		return tcm.run(as, experiment, button);
	}

}

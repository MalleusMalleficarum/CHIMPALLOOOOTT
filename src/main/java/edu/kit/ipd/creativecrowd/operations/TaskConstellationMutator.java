package edu.kit.ipd.creativecrowd.operations;

import edu.kit.ipd.creativecrowd.mutablemodel.MutableAssignment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableTaskConstellation;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.Button;

/**
 * strategyinterface for a TaskConstellationMutator which adds new tasks for workers
 * 
 * @author Anika + Jonas
 */
public interface TaskConstellationMutator extends AbstractStrategy {
	public MutableTaskConstellation run(MutableAssignment ass, MutableExperiment ex, Button btn) throws NoValidTasksException, DatabaseException;
}

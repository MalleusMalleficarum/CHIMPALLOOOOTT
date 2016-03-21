package edu.kit.ipd.creativecrowd.view;

import edu.kit.ipd.creativecrowd.readablemodel.Experiment;
import edu.kit.ipd.creativecrowd.readablemodel.TaskConstellation;

public interface TaskView {
	public String createTaskView(Experiment ex, String assgId);
}

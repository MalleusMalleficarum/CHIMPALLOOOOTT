package edu.kit.ipd.creativecrowd.view;

import edu.kit.ipd.chimpalot.jsonclasses.ConfigModelJson;
import edu.kit.ipd.creativecrowd.readablemodel.Experiment;

public interface TaskView {
	
	public String createTaskView(Experiment ex, String assgId);
	
	/**
	 * Creates a non-interacive preview of a task with a given config.
	 * 
	 * @param config the configuration to create the preview
	 * @return a html representation of the preview
	 * @author Thomas Friedel
	 */
	public String createPreview(ConfigModelJson config);
}

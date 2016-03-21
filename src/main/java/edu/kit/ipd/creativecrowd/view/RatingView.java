package edu.kit.ipd.creativecrowd.view;

import edu.kit.ipd.chimpalot.jsonclasses.ConfigModelJson;
import edu.kit.ipd.creativecrowd.readablemodel.Experiment;

public interface RatingView {
	
	public String createView(Experiment ex, String assgId, int num);
	
	public String createPreview(ConfigModelJson config, int num);
}

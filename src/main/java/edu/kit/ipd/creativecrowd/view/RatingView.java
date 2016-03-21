package edu.kit.ipd.creativecrowd.view;

import edu.kit.ipd.creativecrowd.readablemodel.Experiment;

public interface RatingView {
	public String createView(Experiment ex, String assgId, int num);
}

package edu.kit.ipd.creativecrowd.view;

import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRatingOption;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.Experiment;
import edu.kit.ipd.creativecrowd.readablemodel.RatingOption;
import edu.kit.ipd.creativecrowd.util.Logger;

public class GenericRatingView implements RatingView {

	@Override
	public String createView(Experiment ex, String assgId, int num) {
		String view = "";

		try {
			int i = 0;
			for (RatingOption op : ex.getRatingOptions()) {
				view += "<input type='radio' name='rating" + num
						+ "option' value='" + op.getID() + "'>" + op.getText();
			}
		} catch (DatabaseException e) {
			Logger.logException(e.getMessage());
			e.printStackTrace();
		}

		return view;
	}

}

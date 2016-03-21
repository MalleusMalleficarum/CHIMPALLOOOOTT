package edu.kit.ipd.creativecrowd.view;

import edu.kit.ipd.chimpalot.jsonclasses.ConfigModelJson;
import edu.kit.ipd.chimpalot.util.Logger;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.Experiment;
import edu.kit.ipd.creativecrowd.readablemodel.RatingOption;

public class GenericRatingView implements RatingView {

	@Override
	public String createView(Experiment ex, String assgId, int num) {
		String view = "";

		try {
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

	@Override
	public String createPreview(ConfigModelJson config, int num) {
		String view = "";

		try {
			for (RatingOption op : config.getRatingOptionsJson()) {
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

package edu.kit.ipd.creativecrowd.view;

import edu.kit.ipd.chimpalot.util.Logger;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.Answer;
import edu.kit.ipd.creativecrowd.readablemodel.Experiment;
import edu.kit.ipd.creativecrowd.readablemodel.Rating;

public class ExportView {

	String createView(Experiment ex, String type) {
		if (type.equals("csv")) {
			return makeCSVExport(ex);
		} else {
			return makeTXTExport(ex);
		}
	}

	private String makeTXTExport(Experiment ex) {
		String view = "TXT Export of all the answers\n";
		try {
			for (Answer ans : ex.getCreativeTask().getAnswers()) {
				view += "Answer: " + ans.getText() + " || Final Quality index:" + ans.getFinalQualityIndex() + "\n";
				view += "Ratings for this answer: \n";
				for (Rating r : ans.getRatings()) {
					view += "Ratingoption selected: " + r.getSelectedRatingOption().getText();
					String text = r.getText();
					if (text != null) {
						view += ", Text submitted with the answer: " + r.getText() + "\n";
					} else {
						view += "\n";
					}
				}
			}
		} catch (DatabaseException e) {
			Logger.logException(e.getMessage());
		}

		return view;
	}

	private String makeCSVExport(Experiment ex) {
		String view = "";
		try {
			view += "id, text, timestamp, qualityindex\n";
			for (Answer ans : ex.getCreativeTask().getAnswers()) {
				view += ans.getID() + ",";
				view += ans.getText() + ",";
				view += ans.getTimestampBegin() + ",";
				view += ans.getFinalQualityIndex() + "\n";
			}
		} catch (DatabaseException e) {
			Logger.logException(e.getMessage());
		}

		return view;
	}

}

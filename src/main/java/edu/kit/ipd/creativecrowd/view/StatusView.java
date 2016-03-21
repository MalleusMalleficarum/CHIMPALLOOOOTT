package edu.kit.ipd.creativecrowd.view;

import edu.kit.ipd.chimpalot.util.Logger;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.Experiment;

/**
 * View for the status and the statistics of the experiment
 * 
 * @author simon
 */
public class StatusView {
	/**
	 * create a view containing the status of a given experiment
	 * 
	 * @param ex the experiment
	 * @return a String containing the view
	 */
	public String createView(Experiment ex) {
		String view = "";
		try {
			view += "ID: " + ex.getID() + "\n"
					+ "Creative task: " + ex.getCreativeTask().getDescription() + "\n"
					+ "Tags for this experiment: " + ex.getTags() + "\n"
					+ ex.getStats().getAcceptedHitCount() + " Workers accepted the HIT \n"
					+ ex.getStats().getSubmissionCount() + " workers submitted the hit \n"
					+ ex.getStats().getCancelledCount() + " workers accepted the HIT but did not submit anything \n"
					+ ex.getStats().getRatingCount() + " ratings were given in total \n"
					+ ex.getStats().getPreviewClicksCount() + " workers previewed the HIT \n "
					+ "The experiment has been running since " + ex.getStats().getTimestampBegin() + "\n";
		} catch (DatabaseException | NullPointerException exc) {
			Logger.logException(exc.getMessage());
			// view = exc.getMessage() +"\n";
			// exc.printStackTrace();
		}

		return view;
	}

}

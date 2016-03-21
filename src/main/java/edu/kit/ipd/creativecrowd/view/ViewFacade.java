package edu.kit.ipd.creativecrowd.view;

import edu.kit.ipd.creativecrowd.readablemodel.Experiment;
import edu.kit.ipd.creativecrowd.readablemodel.TaskConstellation;

/**
 * the class ViewFacade provides an interface for the Controller to interact with the view package
 * 
 * @author simon
 */
public class ViewFacade {

	/**
	 * show the status of a particular experiment
	 * 
	 * @param ex the experiment
	 * @return a String containing statistics about an experiment
	 */
	public String showStatus(Experiment ex) {
		StatusView sta = new StatusView();
		return sta.createView(ex);
	}

	/**
	 * shows the current task in a Taskconstellation(specified by assgId)
	 * the method will choose the right
	 * 
	 * @param ex
	 * @param assgId
	 * @return
	 */
	public String showTaskConstellation(Experiment ex, String assgId) {
		WorkerView v = new WorkerView();
		return v.searchRightTaskView(ex, assgId);
	}

	public String showDialog(String message) {
		DialogView dia = new DialogView();
		return dia.createView(message);

	}

	public String showExport(Experiment ex, String type) {
		ExportView expV = new ExportView();
		return expV.createView(ex, type);
	}

}

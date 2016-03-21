package edu.kit.ipd.creativecrowd.view;

import edu.kit.ipd.chimpalot.jsonclasses.ConfigModelJson;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.Experiment;

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
	
	public String showCalibquest(Experiment ex, String assgId) {
		TaskView v = new CalibrationTaskView();
		return v.createTaskView(ex, assgId);
	}
	
	public String showCtrlquest(Experiment ex, String assgId) {
		TaskView v = new ControlTaskView();
		return v.createTaskView(ex, assgId);
	}
	
	public String previewRating(ConfigModelJson conf) {
		TaskView v = new RatingTaskView();
		return v.createPreview(conf);
	}
	
	public String previewCreative(ConfigModelJson conf) {
		CreativeTaskView v = new CreativeTaskView();
		return v.createPreview(conf);
	}
	
	public String previewCreative(Experiment exp) throws DatabaseException {
		CreativeTaskView v = new CreativeTaskView();
		return v.createPreview(exp);
	}
	
	public String previewCalibQuestion(ConfigModelJson conf) {
		TaskView v = new CalibrationTaskView();
		return v.createPreview(conf);
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

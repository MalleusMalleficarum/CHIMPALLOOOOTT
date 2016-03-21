package edu.kit.ipd.creativecrowd.view;

import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.CreativeTask;
import edu.kit.ipd.creativecrowd.readablemodel.Experiment;
import edu.kit.ipd.creativecrowd.readablemodel.TaskConstellation;
import edu.kit.ipd.creativecrowd.util.Logger;
import freemarker.template.Configuration;

public class WorkerView {
	protected String searchRightTaskView(Experiment ex, String assgId) {
		try {
			if (assgId == null || assgId.equals("ASSIGNMENT_ID_NOT_AVAILABLE") || ex.getAssignment(assgId).getTaskConstellation().getCurrentTask() instanceof CreativeTask) {
				CreativeTaskView cr = new CreativeTaskView();
				return cr.createTaskView(ex, assgId);
			} else {
				RatingTaskView ra = new RatingTaskView();
				return ra.createTaskView(ex, assgId);
			}
		} catch (Exception e) {
			Logger.logException(e.getMessage());
			e.printStackTrace();
			return "Error";
		}

	}
}

package edu.kit.ipd.creativecrowd.view;

import edu.kit.ipd.chimpalot.util.Logger;
import edu.kit.ipd.creativecrowd.crowdplatform.PlatformIdentity;
import edu.kit.ipd.creativecrowd.readablemodel.CreativeTask;
import edu.kit.ipd.creativecrowd.readablemodel.Experiment;

public class WorkerView {
	protected String searchRightTaskView(Experiment ex, String assgId) {
		try {
			//mturk
			if (ex.getAssignment(assgId).getMTurkAssignmentID().getId().startsWith(PlatformIdentity.MTurk.getPrefix())) {
				//check if creative/ratings are available
				if (ex.getConfig().getSendCreativeTo(PlatformIdentity.MTurk) &&
						assgId == null || assgId.equals("ASSIGNMENT_ID_NOT_AVAILABLE") || 
						ex.getAssignment(assgId).getTaskConstellation().getCurrentTask() instanceof CreativeTask) {


					CreativeTaskView cr = new CreativeTaskView();
					return cr.createTaskView(ex, assgId);
				}
				else if (ex.getConfig().getSendRatingTo(PlatformIdentity.MTurk)) {
					RatingTaskView ra = new RatingTaskView();
					return ra.createTaskView(ex, assgId);
				}
				//pybossa
			} else if (ex.getAssignment(assgId).getMTurkAssignmentID().getId().startsWith(PlatformIdentity.PyBossa.getPrefix())) {


				if (ex.getConfig().getSendCreativeTo(PlatformIdentity.PyBossa) &&
						assgId == null || assgId.equals("ASSIGNMENT_ID_NOT_AVAILABLE") || 
						ex.getAssignment(assgId).getTaskConstellation().getCurrentTask() instanceof CreativeTask) {


					CreativeTaskView cr = new CreativeTaskView();
					return cr.createTaskView(ex, assgId);
				}
				else if (ex.getConfig().getSendRatingTo(PlatformIdentity.PyBossa)) {
					RatingTaskView ra = new RatingTaskView();
					return ra.createTaskView(ex, assgId);
				}
			}


			//			if (assgId == null || assgId.equals("ASSIGNMENT_ID_NOT_AVAILABLE") || ex.getAssignment(assgId).getTaskConstellation().getCurrentTask() instanceof CreativeTask) {
			//				CreativeTaskView cr = new CreativeTaskView();
			//				return cr.createTaskView(ex, assgId);
			//			} else {
			//				RatingTaskView ra = new RatingTaskView();
			//				return ra.createTaskView(ex, assgId);
			//			}
		} catch (Exception e) {
			Logger.logException(e.getMessage());
			e.printStackTrace();
			return "Error";
		}
		return "Error";
	}
}

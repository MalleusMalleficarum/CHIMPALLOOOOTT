package edu.kit.ipd.creativecrowd.controller;

import java.util.Map.Entry;

import edu.kit.ipd.creativecrowd.connector.Connector;
import edu.kit.ipd.creativecrowd.connector.ModelException;
import edu.kit.ipd.creativecrowd.connector.RatingSpec;
import edu.kit.ipd.creativecrowd.util.Logger;
import edu.kit.ipd.creativecrowd.view.ViewFacade;
import edu.kit.ipd.creativecrowd.mturk.AssignmentId;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.Answer;
import edu.kit.ipd.creativecrowd.readablemodel.Assignment;
import edu.kit.ipd.creativecrowd.readablemodel.Button;
import edu.kit.ipd.creativecrowd.readablemodel.Experiment;
import edu.kit.ipd.creativecrowd.readablemodel.RatingTask;
import edu.kit.ipd.creativecrowd.readablemodel.TaskConstellation;
import edu.kit.ipd.creativecrowd.readablemodel.Task;
import spark.Request;
import spark.Response;

public class AssignmentController {
	private Connector connector;
	private ViewFacade view;

	// TODO preview anzeigen wenn assignment_id = ASSIGNMENT_ID_NOT_AVAILABLE
	public Response show(Request sparkRequest, Response resp) {
		try {
			String expId = sparkRequest.params(":expId");
			String assgId = sparkRequest.queryParams("assignmentId");
			String wrkrId = sparkRequest.queryParams("workerId");
			String assignmentId = null;
			connector = new Connector();
			if (assgId != null
					&& !(assgId.equals("ASSIGNMENT_ID_NOT_AVAILABLE"))) {
				AssignmentId mturkAssignmentId = new AssignmentId(
						sparkRequest.queryParams("assignmentId"));
				assignmentId = connector.findInternalAssignmentId(
						mturkAssignmentId, expId);
				sparkRequest.session(true).attribute("assignment_id",
						assignmentId);

				Button b = Button.Start;
				if (assignmentId != null && !assignmentId.equals("")) {
					TaskConstellation t = connector.getNewTaskConstellation(
							expId, assignmentId, b);

					connector.updateWorkerId(expId, assignmentId, wrkrId);
				}
			} else {
				connector.increasePreviewClickCount(expId);
			}

			view = new ViewFacade();
			Experiment exp = connector.getExperiment(expId);
			resp.body(view.showTaskConstellation(exp, assignmentId));
			resp.status(200);
		} catch (ModelException e) {
			Logger.logException(e.getMessage());
			e.printStackTrace();
		}

		return resp;
	};

	public Response update(Request sparkRequest, Response resp) {
		try {
			String button = "";
			String answer = null;
			java.util.Map<String, String[]> m = sparkRequest.raw().getParameterMap();

			for (Entry<String, String[]> e : m.entrySet()) {
				if (e.getKey().equals("button")) {
					button = e.getValue()[0];
				}
				if (e.getKey().equals("answer"))
					answer = e.getValue()[0];
			}
			view = new ViewFacade();
			String expId = sparkRequest.params("expId");
			String assignmentId = sparkRequest.session(true).attribute("assignment_id");
			Button b = Button.valueOf(button);
			connector = new Connector();
			Experiment ex = connector.getExperiment(expId);
			Assignment as = ex.getAssignment(assignmentId);
			Task currentTask = (as.getTaskConstellation().getCurrentTask());

			if (answer != null) {
				try {
					String id = ex.getCreativeTask().getID();
					AnswerSpecFromConfig ans = new AnswerSpecFromConfig(id,
							answer);
					connector.store(expId, ans, assignmentId);
					TaskConstellation newTC = connector
							.getNewTaskConstellation(expId, assignmentId, b);
					resp.body(view.showTaskConstellation(ex, assignmentId));
				} catch (DatabaseException e) {
					Logger.logException(e.getMessage());
				}
			} else {
				// TODO: Ratings
				int i = 0;
				for (Answer a : ((RatingTask) currentTask)
						.getAnswersToBeRated()) {
					String[] ids = m.get("rating" + i + "ansid");
					String[] opts = m.get("rating" + i + "option");
					String[] texts = m.get("rating" + i + "text");
					if (ids == null || opts == null || texts == null
							|| ids.length != 1 || opts.length != 1 || texts.length != 1) {
						// throw some kind of InvalidUserDataException here .. ?
					} else {
						RatingSpec ratSp = new RatingSpecImp(ids[0], opts[0], texts[0]);
						connector.store(expId, ratSp, assignmentId);
					}
					i++;
				}

				Experiment exp = connector.getExperiment(expId);
				TaskConstellation newTC = connector
						.getNewTaskConstellation(expId, assignmentId, b);
				resp.body(view.showTaskConstellation(exp, assignmentId));
			}
			if (b.equals(Button.Submit)) {
				connector.submit(expId, assignmentId);

				// TODO: Unterscheidung zwischen sandbox und nicht-sandbox
				String assgId = as.getMTurkAssignmentID().getId();
				System.out.println(assgId);
				String mturk_url = "https://workersandbox.mturk.com/mturk/externalSubmit?assignmentId="
						+ assgId + "&foo=bar";
				resp.redirect(mturk_url);
				resp.body("Thank you");
			}

		} catch (ModelException e) {
			Logger.logException(e.getMessage());
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return resp;
	};

}

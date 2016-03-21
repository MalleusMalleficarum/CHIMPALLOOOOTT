package edu.kit.ipd.chimpalot.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.kit.ipd.chimpalot.jsonclasses.CalibrationAnswerJson;
import edu.kit.ipd.chimpalot.jsonclasses.AnswerSpecFromConfig;
import edu.kit.ipd.chimpalot.jsonclasses.RatingSpecFromConfig;
import edu.kit.ipd.chimpalot.util.Logger;
import edu.kit.ipd.creativecrowd.connector.Connector;
import edu.kit.ipd.creativecrowd.connector.ModelException;
import edu.kit.ipd.creativecrowd.connector.RatingSpec;
import edu.kit.ipd.creativecrowd.crowdplatform.AssignmentId;
import edu.kit.ipd.creativecrowd.crowdplatform.PlatformIdentity;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.Answer;
import edu.kit.ipd.creativecrowd.readablemodel.Assignment;
import edu.kit.ipd.creativecrowd.readablemodel.Button;
import edu.kit.ipd.creativecrowd.readablemodel.Experiment;
import edu.kit.ipd.creativecrowd.readablemodel.RatingTask;
import edu.kit.ipd.chimpalot.util.GlobalApplicationConfig;
import edu.kit.ipd.creativecrowd.view.ViewFacade;

/**
 * This class allows workers to get and commit assignments.
 * 
 * @author Thomas Friedel & (the unknown author of creativecrowd.AssignmentController)
 */
//For a proper conversion to Spring (and MVC) would we need a ViewController, but that would require changing creativecrowd.view
@RestController //So the html is put in the response-body.
@RequestMapping("/assignment")
public class AssignmentController {
	//TODO check that Session is handled correctly.
	private Connector conn;
	private ViewFacade view;
	
	/**
	 * This method is used when a MTurk-worker accepts a HIT. It assigns an Assignment to the worker and shows 
	 * the first task of the generated task-constellation to the worker. It also shows a preview, if a worker
	 * wants to view a HIT before/without accepting it.
	 * 
	 * @param session the associated http-session. The assignment-id will be put in there.
	 * @param expid The experiment
	 * @param platform on which platform this assignment is
	 * @param assignmentid The assignmentid. If this is {@code null}, a preview of a task will be given instead
	 * @param workerid the worker
	 * @return an html-representation of a task. See {@link edu.kit.ipd.creativecrowd.view.TaskView}
	 */
	@RequestMapping(value = "/{id}/MTurk", method = RequestMethod.GET)
	public String showMTurk(HttpSession session, @PathVariable("id") String expid,
			@RequestParam(value = "assignmentId", required = false) String assignmentid,
			@RequestParam(value = "workerId", required = false) String workerid) {
		if (workerid != null) {
			Logger.log("A MTurk worker with id '" + workerid + "' started an assignment.");
		} else {
			Logger.log("A MTurk worker without id requested a task.");
		}
		AssignmentId assgId;
		String internalAssgId = null;
		if (assignmentid != null && !(assignmentid.equals("ASSIGNMENT_ID_NOT_AVAILABLE"))) {
			assgId = new AssignmentId(PlatformIdentity.MTurk.getPrefix() + assignmentid);
			try {
				internalAssgId = getConnector().findInternalAssignmentId(assgId, expid);
			} catch (ModelException e) {
				Logger.logException(e.getMessage());
				throw new InternalServerException(e.getMessage());
			}
			session.setAttribute("assignment_id", internalAssgId);
			Button b = Button.Start;
			try {
				getConnector().updateWorkerId(expid, internalAssgId, workerid);
				getConnector().getNewTaskConstellation(expid, internalAssgId, b);
			} catch (ModelException e) {
				Logger.logException(e.getMessage());
				throw new InternalServerException(e.getMessage());
			}
		}  else {
			//A preview.
			try {
				getConnector().increasePreviewClickCount(expid);
				return getView().previewCreative(getConnector().getExperiment(expid));
			} catch (ModelException | DatabaseException e) {
				Logger.logException(e.getMessage());
				throw new InternalServerException(e.getMessage());
			}
		}
		try {
			Experiment exp = getConnector().getExperiment(expid);
			if (exp.getControlQuestions().iterator().hasNext()) {
				return getView().showCtrlquest(this.getConnector().getExperiment(expid), internalAssgId);
			} else {
				return getView().showTaskConstellation(getConnector().getExperiment(expid), internalAssgId);
			}
		} catch (ModelException | DatabaseException e) {
			Logger.logException(e.getMessage());
			throw new InternalServerException(e.getMessage());
		}

	}
	
	
	/**
	 * This method is used when a PyBossa-worker starts a task. It assigns an Assignment to him and shows the first task
	 * of the generated task-constellation to him.
	 * 
	 * @param session the associated http-session. The assignment-id will be put in there.
	 * @param expid the experiment
	 * @param workerid the id of the worker
	 * @return an html-representation of a task. See {@link edu.kit.ipd.creativecrowd.view.TaskView}
	 */
	@RequestMapping(value = "/{id}/PyBossa/{workerid}", method = RequestMethod.GET)
	public String showPyBossa(HttpSession session, @PathVariable("id") String expid,
			@PathVariable("workerid") String workerid) {
		if (workerid != null) {
			Logger.log("A PyBossa worker with id '" + workerid + "' started an assignment.");
		} else {
			Logger.log("A PyBossa worker without id requested a task.");
		}
		AssignmentId assgId = new AssignmentId(PlatformIdentity.PyBossa.getPrefix() + "assignment_"
				+ UUID.randomUUID().toString().replaceAll("-", ""));
		try {
			String internalAssgId = getConnector().findInternalAssignmentId(assgId, expid);
			session.setAttribute("assignment_id", internalAssgId);
			Button b = Button.Start;
			getConnector().updateWorkerId(expid, internalAssgId, workerid);
			getConnector().getNewTaskConstellation(expid, internalAssgId, b);
			Experiment exp = getConnector().getExperiment(expid);
			
			if (exp.getCalibrationQuestions().iterator().hasNext()) {
				return getView().showCalibquest(exp, internalAssgId);
			} else {
				if (exp.getControlQuestions().iterator().hasNext()) {
					return getView().showCtrlquest(exp, internalAssgId);
				}
				return getView().showTaskConstellation(exp, internalAssgId);
			}
			
			
		} catch (ModelException | DatabaseException e) {
			Logger.logException(e.getMessage());
			throw new InternalServerException(e.getMessage());
		}
	}
	
	/**
	 * Saves creative-answers of the worker and decides the next step based on the value of {@code button}
	 * 
	 * @param session the associated http-session. The assignment-id is in there.
	 * @param expid The experiment
	 * @param button The name of the button the worker pressed
	 * @param answer The answer of the worker
	 * @return an html-representation of a task, if the worker continues.
	 */
	@RequestMapping({"/{id}/creative"}) //Really don't like this system, but we will keep it for now.
	public String updateCreative(HttpSession session, HttpServletResponse response, @PathVariable("id") String expid,
			@ModelAttribute("button") String button, @ModelAttribute("answer") String answer) {		
		String assignmentid = (String) session.getAttribute("assignment_id");
		
		Button b;
		try {
			b = Button.valueOf(button);
		} catch (IllegalArgumentException e) {
			Logger.logException("Unknown button '" + button + "'.");
			throw new ResourceNotFoundException("Unknown button '" + button + "'.");
		}
		Experiment exp;
		Assignment assg;
		try {
			Logger.debug(expid+  ", " + button + ", " + assignmentid + ", " + answer);
			exp = getConnector().getExperiment(expid);
			assg = exp.getAssignment(assignmentid);
			String id = exp.getCreativeTask().getID();
			AnswerSpecFromConfig ans = new AnswerSpecFromConfig(id, answer);
			getConnector().store(expid, ans, assignmentid);	
			getConnector().getNewTaskConstellation(expid, assignmentid, b);	
			
			if (b.equals(Button.Submit)) {
				getConnector().submit(expid, assignmentid);
				String assgId = assg.getMTurkAssignmentID().getId();
				if (PlatformIdentity.getIdentityFromPrefix(assgId).equals(PlatformIdentity.MTurk)) {
					String mturk_url;
					assgId = assgId.substring(PlatformIdentity.MTurk.getPrefix().length());
					if (GlobalApplicationConfig.isSandbox()) {
						mturk_url = "https://workersandbox.mturk.com/mturk/externalSubmit?assignmentId="
								+ assgId + "&foo=bar";
					}
					else {
						mturk_url = "https://www.mturk.com/mturk/externalSubmit?assignmentId=" + assgId + "&foo=bar";
					}
					response.sendRedirect(mturk_url);
				}

				//TODO what to do if its pybossa?
				
				return "Thank you";
			}
		} catch (ModelException | DatabaseException | IOException e) {
			Logger.logException(e.getMessage());
			throw new InternalServerException(e.getMessage());
		}				
		return getView().showTaskConstellation(exp, assignmentid);
	}
	
	/**
	 * Saves rating-answers  of the worker and decides the next step based on the value of {@code button}
	 * 
	 * @param session the associated http-session. The assignment-id is in there.
	 * @param expid The experiment
	 * @param button The name of the button the worker pressed
	 * @param params a Map of the ratings of the worker
	 * @return an html-representation of a task, if the worker continues.
	 */
	@RequestMapping("/{id}/rating")
	public String updateRating(HttpSession session, HttpServletResponse response, @PathVariable("id") String expid, 
			 @RequestParam("button") String button,  @RequestParam Map<String, String> params) {
		String assignmentid = (String) session.getAttribute("assignment_id");
		Button b;
		try {
			b = Button.valueOf(button);
		} catch (IllegalArgumentException e) {
			Logger.logException("Unknown button '" + button + "'.");
			throw new ResourceNotFoundException("Unknown button '" + button + "'.");
		}
		Experiment exp;
		Assignment assg;
		try {
			exp = getConnector().getExperiment(expid);
			assg = exp.getAssignment(assignmentid);
			RatingTask currentTask = (RatingTask) assg.getTaskConstellation().getCurrentTask();
			
			int i = 0;
			Iterator<? extends Answer> answersIterator = currentTask.getAnswersToBeRated().iterator();
			while (answersIterator.hasNext()) {
				String id = (String) params.get("rating" + i + "ansid");
				String opt = (String) params.get("rating" + i + "option");
				String text = (String) params.get("rating" + i + "text");
				if (id == null || opt == null || text == null) {
					
					throw new BadRequestException("No correct rating was given.");
				} else {
					RatingSpec ratSp = new RatingSpecFromConfig(id, opt, text);
					getConnector().store(expid, ratSp, assignmentid);
				}
				answersIterator.next();
				i++;
				
				if (b.equals(Button.Submit)) {
					getConnector().submit(expid, assignmentid);
					String assgId = assg.getMTurkAssignmentID().getId();
					if (PlatformIdentity.getIdentityFromPrefix(assgId).equals(PlatformIdentity.MTurk)) {
						String mturk_url;
						assgId = assgId.substring(PlatformIdentity.MTurk.getPrefix().length());
						if (GlobalApplicationConfig.isSandbox()) {
							mturk_url = "https://workersandbox.mturk.com/mturk/externalSubmit?assignmentId="
									+ assgId + "&foo=bar";
						}
						else {
							mturk_url = "https://www.mturk.com/mturk/externalSubmit?assignmentId=" + assgId + "&foo=bar";
						}
						response.sendRedirect(mturk_url);
					}

					//TODO what to do if its pybossa?
					
					return "Thank you";
				}
			}
		} catch (DatabaseException | ModelException | IOException e) {
			Logger.logException(e.getMessage());
			throw new InternalServerException(e.getMessage());
		}
		return getView().showTaskConstellation(exp, assignmentid);
	}
	
	/**
	 * 
	 * @param session
	 * @param expid
	 * @param params
	 * @return
	 * @author Basti
	 */
	@RequestMapping("/{id}/calibration")
	public String updateCalibration(HttpSession session, @PathVariable("id") String expid,
			 @RequestParam("answer") String answer,
			 @RequestParam("calibid") String calibid) {
		boolean val = false;
		String assignmentid = (String) session.getAttribute("assignment_id");
		Experiment exp;
		Assignment assg;
		Map<String, String> params = new HashMap<String, String>();
		params.put(calibid, answer);
		try {
			exp = this.getConnector().getExperiment(expid);
			assg = exp.getAssignment(assignmentid);
			String workerid =  assg.getWorker().getID();
	
			val = this.getConnector().saveCalibanswers(new CalibrationAnswerJson(workerid, params)
					, workerid, expid, assignmentid);
		} catch (DatabaseException | ModelException e) {
			Logger.logException(e.getMessage());
			throw new InternalServerException(e.getMessage());
		}
		if (val) {
			return getView().showCalibquest(exp, assignmentid);
			
			//return getView().showTaskConstellation(exp, assignmentid);
		}
		
		return "You are not the kind of Worker we are looking for, but still thank you!";
	} 

	@RequestMapping("/{id}/control/{countI}")
	public String updateControl(HttpSession session, @PathVariable("id") String expid, @PathVariable("countI") String countI,
			@RequestBody String body) {
		Logger.debug(body);
		String assignmentid = (String) session.getAttribute("assignment_id");
		Map<String, String> params = new HashMap<String, String>();
		String[] questions = body.split("&");
		for (String question : questions) {
			String[] answer = question.split("=");
			params.put(answer[0], answer[1]);
		}
		for (int i = 0; i < Integer.parseInt(countI); i++) {
			if (!params.get(String.valueOf(i)).equals("true"))
				return "You answered atleast one controlquestion wrong!";
			//TODO reject HIT
		}
		try {
			return getView().showTaskConstellation(getConnector().getExperiment(expid), assignmentid);
		} catch (ModelException e) {
			throw new InternalServerException(e.getMessage());
		}
	}
	
	private Connector getConnector() {
		if (this.conn == null) {
			this.conn = new Connector();
		}
		return this.conn;
	}

	private ViewFacade getView() {
		if (this.view == null) {
			this.view = new ViewFacade();
		}
		return this.view;
	}
}

package edu.kit.ipd.creativecrowd.view;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import edu.kit.ipd.chimpalot.jsonclasses.ConfigModelJson;
import edu.kit.ipd.chimpalot.util.GlobalApplicationConfig;
import edu.kit.ipd.chimpalot.util.Logger;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.Answer;
import edu.kit.ipd.creativecrowd.readablemodel.Experiment;
import edu.kit.ipd.creativecrowd.readablemodel.RatingTask;
import edu.kit.ipd.creativecrowd.readablemodel.TaskConstellation;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class RatingTaskView implements TaskView {

	public String createTaskView(Experiment ex, String assgId) {
		try {
			TaskConstellation t = ex.getAssignment(assgId)
					.getTaskConstellation();
			// Is a singleton
			Configuration cfg = FreemarkerConfig.getConfig();
			String view = "";
			String className = ex.getRatingTaskViewClass();

			RatingView rv = null;
			try {
				@SuppressWarnings("unchecked")
				Class<RatingView> c = (Class<RatingView>) Class
				.forName(className);
				rv = c.newInstance();
			} catch (InstantiationException | IllegalAccessException
					| ClassNotFoundException exc) {
				Logger.logException(exc.getMessage());
				return "There was a problem creating a task for you";
			}
			int i = 0;
			// wat
			// TODO
			// must be submitted via javascript to make a difference between
			// different buttons pressed
			view += "";
			view += "<table>";
			@SuppressWarnings("unchecked")
			Iterable<Answer> ansList = (Iterable<Answer>) ((RatingTask) t
					.getCurrentTask()).getAnswersToBeRated(); /*-?|Simon|simon|c3|?*/
			for (Answer ans : ansList) {
				view += "<tr><td class='answered'>" + ans.getText()
				+ "</td><td><div id='ratingoptionsandtext'>"; /*-?|Simon|simon|c0|?*/
				view += rv.createView(ex, assgId, i)
						+ "<input type='text' name='rating" + i
						+ "text' placeholder='Comment this answer here'>"
						+ "<input type='hidden' name='rating" + i
						+ "ansid' value='" + ans.getID() + "'></div>";
				view += "</div></td></tr>";
				i++;
			}
			view += "</table>";

			// Create the root hash
			Map<String, String> root = new HashMap<String, String>();
			// Put string ``user'' into the root
			root.put("task", ex.getCreativeTask().getDescription());
			root.put("pic", (ex.getCreativeTask().getPictureURL()));
			root.put("expId", ex.getID());
			root.put("ratingTable", view);
			root.put("desc", ex.getCreativeTask()
					.getAccordingRatingTaskDescription());
			String exdesc = ex.getDescription();
			if(exdesc!=null){
				root.put("exdesc", exdesc);
			}else{
				root.put("exdesc", "");
			}
			String licenseUrl = ex.getCreativeTask().getPictureLicenseURL();
			if (licenseUrl != null) {

				root.put("iframe", ex.getCreativeTask().getPictureLicenseURL()); /*-?|Test Repo-Review|simon|c0|?*/
			} else {
				root.put("iframe", "");
			}
			// set if the buttons appear
			if (t.againButtonExists())
				root.put("again", "true");
			else
				root.put("again", "");
			if (t.nextButtonExists())
				root.put("next", "true");
			else
				root.put("next", "");
			// submit button always exists
			root.put("sub", "true");

			root.put("submitaction", "somemturkurl");
			root.put("againaction", GlobalApplicationConfig.getPublicBaseURL()
					+ "/assignment/" + ex.getID() + "?assignmentId=" + assgId);
			// TODO: Macht das hier Sinn?
			root.put("nextaction", GlobalApplicationConfig.getPublicBaseURL()
					+ "/assignment/" + ex.getID() + "?assignmentId=" + assgId);

			try {
				Template temp = cfg.getTemplate("rateTask.ftl");
				Writer out = new StringWriter();
				temp.process(root, out);
				return out.toString();
			} catch (IOException | TemplateException exc) {
				Logger.logException(exc.getMessage());
			}

		} catch (DatabaseException e) {
			Logger.logException(e.getMessage());
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.view.TaskView#createPreview(edu.kit.ipd.creativecrowd.readablemodel.ConfigModel)
	 */
	@Override //TODO buttons are still working, kill 'em
	public String createPreview(ConfigModelJson config) {
		Configuration cfg = FreemarkerConfig.getConfig();
		RatingView rv = null;
		try {
			try {
				@SuppressWarnings("unchecked")
				Class<RatingView> c = (Class<RatingView>) Class.forName(config.getEvaluationType());
				rv = c.newInstance();
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException exc) {
				return "Unknown evaluation type '" + config.getEvaluationType() + "'";
			}
			String view = new String();
			view += "<table>";
			for (int i = 0; i < config.getMaxRatingTask(); i++) {
				view += "<tr><td class='answered'>" + "Placeholder-text for answer " + i + "."
						+ "</td><td><div id='ratingoptionsandtext'>";
				view += rv.createPreview(config, i)
						+ "<input type='text' name='rating" + i
						+ "text' placeholder='Comment this answer here'>"
						+ "<input type='hidden' name='rating" + i
						+ "ansid' value='" + "ansid" + i + "'></div>";
				view += "</div></td></tr>";

			}
			view += "</table>";

			// Create the root hash
			Map<String, String> root = new HashMap<String, String>();
			// Put string ``user'' into the root
			root.put("task", config.getTaskDescription());
			root.put("pic", config.getPictureURL());
			root.put("expId", "expid");
			root.put("ratingTable", view);
			root.put("desc", config.getRatingTaskQuestion());
			String exdesc = config.getTaskDescription();
			if(exdesc!=null){
				root.put("exdesc", exdesc);
			}else{
				root.put("exdesc", "");
			}
			String licenseUrl = config.getTaskSourceURL();
			if (licenseUrl != null) {

				root.put("iframe", config.getTaskSourceURL());
			} else {
				root.put("iframe", "");
			}

			root.put("again", "true");
			root.put("next", "true");
			root.put("sub", "true");

			root.put("submitaction", "");
			//TODO scrap the unneeded.
			root.put("againaction", "");
			root.put("nextaction", "");

			try {
				Template temp = cfg.getTemplate("rateTask.ftl");
				Writer out = new StringWriter();
				temp.process(root, out);
				return out.toString();
			} catch (IOException | TemplateException exc) {
				Logger.logException(exc.getMessage());
			}

		} catch (DatabaseException e) {
			Logger.logException(e.getMessage());
		}

		return null;
	}

}

package edu.kit.ipd.creativecrowd.view;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.Answer;
import edu.kit.ipd.creativecrowd.readablemodel.CreativeTask;
import edu.kit.ipd.creativecrowd.readablemodel.Experiment;
import edu.kit.ipd.creativecrowd.readablemodel.RatingTask;
import edu.kit.ipd.creativecrowd.readablemodel.TaskConstellation;
import edu.kit.ipd.creativecrowd.util.GlobalApplicationConfig;
import edu.kit.ipd.creativecrowd.util.Logger;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class RatingTaskView {

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
				return "There was a problem creating a task for you";
			}
			int i = 0;
			// wat
			// TODO
			// must be submitted via javascript to make a difference between
			// different buttons pressed
			view += "";
			view += "<table>";
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

}

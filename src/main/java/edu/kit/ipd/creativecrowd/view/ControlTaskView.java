package edu.kit.ipd.creativecrowd.view;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.kit.ipd.chimpalot.jsonclasses.ConfigModelJson;
import edu.kit.ipd.chimpalot.util.Logger;
import edu.kit.ipd.creativecrowd.mutablemodel.MutablePossibleControlAnswer;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.ControlQuestion;
import edu.kit.ipd.creativecrowd.readablemodel.Experiment;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;


/**
 * @author basti
 *
 */
public class ControlTaskView implements TaskView {

	@Override
	public String createTaskView(Experiment ex, String assgId) {
		
		
		try {
			Iterable<? extends ControlQuestion> ctrllist =  ex.getControlQuestions();
			
	
			
			Configuration cfg = FreemarkerConfig.getConfig();
			// Create the root hash
			Map<String, Object> root= new HashMap<String, Object>();
			// Put strings into map
			
			root.put("taskdesc", ex.getCreativeTask().getDescription());

			List<CalibData> cd = new ArrayList<CalibData>();			
			for(ControlQuestion cq : ctrllist) {
								
				Iterable<? extends MutablePossibleControlAnswer> posans = cq.getPossibleAnswers();
				List<AnsData> ans = new ArrayList<AnsData>();
				for(MutablePossibleControlAnswer pa : posans) {
					ans.add(new AnsData(pa.getText(), pa.getIsTrue()));
				}
				CalibData c = new CalibData(cq.getQuestion(), ans);
				cd.add(c);
			}
			root.put("controlquests", cd);
			root.put("actval", true);
			root.put("expid", ex.getID());
			
			String view = "<tr>";
			int i = 0;
			for (CalibData c : cd) {
				view +="<td>" + c.getQuest() + "</td>";
				view += "<p>";
				view += "<select name=" + i + ">";
				for (AnsData d : c.getAns()) {
					 view += "<option value=" +  Boolean.toString(d.getValue()) + ">" + d.getAnswer() + "</option>";
					root.put(d.getAnswer(), d.getValue());
				}
				i++;
				view += "</select>";
				view += "</p>";
			}
			view += "</tr>";
			
			root.put("countI", Integer.toString(i));
			root.put("view", view);
			
			
			try{
				Template temp = cfg.getTemplate("controlquest.ftl");  //TODO erstellen
				Writer out = new StringWriter();
				temp.process(root, out);  
				return out.toString();
			}catch(IOException|TemplateException exc){
				Logger.logException(exc.getMessage());

			}
			
		} catch (DatabaseException e) {
			Logger.logException(e.getMessage());
			e.printStackTrace();
		}
		
		
		
		
		return "";
	}

	@Override
	public String createPreview(ConfigModelJson config) {
		// TODO Auto-generated method stub
		return null;
	} 

}
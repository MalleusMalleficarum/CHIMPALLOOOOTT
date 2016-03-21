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
import edu.kit.ipd.creativecrowd.mutablemodel.MutableCalibrationQuestion;
import edu.kit.ipd.creativecrowd.mutablemodel.MutablePossibleCalibrationAnswer;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.CalibrationQuestion;
import edu.kit.ipd.creativecrowd.readablemodel.Experiment;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;


/**
 * @author basti
 *
 */
public class CalibrationTaskView implements TaskView {
	


	@Override
	public String createTaskView(Experiment ex, String assgId) {
		
		
		try {
			Iterable<? extends CalibrationQuestion> caliblist = ex.getCalibrationQuestions();
			
			Iterable<? extends MutableCalibrationQuestion> caliblist2 = ex.getAssignment(assgId).getWorker().getDoneCalibQuestWorker();
			
			for (CalibrationQuestion quest : caliblist) {
				for (MutableCalibrationQuestion quest2 : caliblist2) {
					if (quest.equals(quest2)) 
						caliblist.iterator().remove();
				}
			}
	
			
			Configuration cfg = FreemarkerConfig.getConfig();
			// Create the root hash
			Map<String, Object> root= new HashMap<String, Object>();
			// Put strings into map
			if (!caliblist.iterator().hasNext()) {
				return new ViewFacade().showCtrlquest(ex, assgId);
			}
			
			CalibrationQuestion cq = caliblist.iterator().next();
			
			String exdesc = cq.getQuestion();
			if(exdesc!=null){
				root.put("exdesc", exdesc);
			}else{
				root.put("exdesc", "");
			}
			
			
			Iterable<? extends MutablePossibleCalibrationAnswer> posans =  cq.getPossibleAnswers();
			List<String> ans = new ArrayList<String>();
			for(MutablePossibleCalibrationAnswer pa : posans) {
				ans.add(pa.getText());
			}
			root.put("calibdata", ans);
			root.put("calibid", cq.getID());
			
			try{
				Template temp = cfg.getTemplate("calibquest.ftl");
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

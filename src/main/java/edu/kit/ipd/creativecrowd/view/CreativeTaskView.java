package edu.kit.ipd.creativecrowd.view;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import edu.kit.ipd.chimpalot.jsonclasses.ConfigModelJson;
import edu.kit.ipd.chimpalot.util.Logger;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.ConfigModel;
import edu.kit.ipd.creativecrowd.readablemodel.CreativeTask;
import edu.kit.ipd.creativecrowd.readablemodel.Experiment;
import edu.kit.ipd.creativecrowd.readablemodel.TaskConstellation;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class CreativeTaskView implements TaskView{

	@Override
	public String createTaskView(Experiment ex, String assgId) {
		if(assgId != null && !(assgId.equals("ASSIGNMENT_ID_NOT_AVAILABLE"))){
			try{
				TaskConstellation t = ex.getAssignment(assgId).getTaskConstellation();
			
		
				//is Singleton
				Configuration cfg = FreemarkerConfig.getConfig();

				// Create the root hash
				Map<String, String> root = new HashMap<String, String>();
				// Put strings into map
				root.put("task", ((CreativeTask) t.getCurrentTask()).getDescription());
				String picUrl = ((CreativeTask) t.getCurrentTask()).getPictureURL();
				if(picUrl!=null){
					root.put("pic", picUrl);
				}else{
					root.put("pic", "");
				}
				String exdesc = ex.getDescription();
				if(exdesc!=null){
					root.put("exdesc", exdesc);
				}else{
					root.put("exdesc", "");
				}
				root.put("expid", ex.getID());
				String licenseUrl = ((CreativeTask) t.getCurrentTask()).getPictureLicenseURL();
				if(licenseUrl != null){
					
					root.put("iframe", ((CreativeTask) t.getCurrentTask()).getPictureLicenseURL()); /*-?|Test Repo-Review|simon|c0|?*/
				}else{
					root.put("iframe", "");
				}
				
				//TODO 
				if(t.nextButtonExists()){
					root.put("next", "n");
				}
				else{
					root.put("next", "");
				}
				//TODO 
				if(t.againButtonExists()) root.put("again", "a");
				else root.put("again", "");
				//TODO 
				if(t.submitButtonExists()){
					if(t.againButtonExists() || t.nextButtonExists()){
						root.put("sub", "s");
					}else{
						root.put("sub", "only");
					}
				}
				else root.put("sub", "");
				
				try{
					Template temp = cfg.getTemplate("creativetask.ftl");  
					Writer out = new StringWriter();
					temp.process(root, out);  
					return out.toString();
				}catch(IOException|TemplateException exc){
					Logger.logException(exc.getMessage());

				}
			
			}catch(DatabaseException e){
				Logger.logException(e.getMessage());
				e.printStackTrace();
			}
		} else {
			try{
				//is Singleton
				Configuration cfg = FreemarkerConfig.getConfig();

				// Create the root hash
				Map<String, String> root = new HashMap<String, String>();
				// Put strings into map
				String task = ex.getCreativeTask().getDescription();
				System.out.print(task);
				root.put("task", task);
				if( ex.getCreativeTask().getPictureURL()!=null){
					root.put("pic", (ex.getCreativeTask().getPictureURL()));
				}else{
					root.put("pic", "");
				}
				root.put("expid", ex.getID());
				root.put("again", "true");
				root.put("next", "true");
				root.put("sub", "true");
				String exdesc = ex.getDescription();
				if(exdesc!=null){
					root.put("exdesc", exdesc);
				}else{
					root.put("exdesc", "");
				}
				String licenseUrl = ex.getCreativeTask().getPictureLicenseURL();
				if(licenseUrl != null){
					
					root.put("iframe", ex.getCreativeTask().getPictureLicenseURL()); /*-?|Test Repo-Review|simon|c0|?*/
				}else{
					root.put("iframe", "");
				}
				
				try{
					Template temp = cfg.getTemplate("creativetaskpreview.ftl");  
					Writer out = new StringWriter();
					temp.process(root, out);  
					return out.toString();
				}catch(IOException|TemplateException exc){
					Logger.logException(exc.getMessage());
				}
			
			}catch(DatabaseException e){
				Logger.logException(e.getMessage());
				e.printStackTrace();
			}
		}
		

		return "";
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.view.TaskView#createPreview(edu.kit.ipd.creativecrowd.readablemodel.ConfigModel)
	 */
	@Override
	public String createPreview(ConfigModelJson config) {
			return createAnyPreview(config);
	}

	public String createPreview(Experiment exp) throws DatabaseException {
		return this.createAnyPreview(exp.getConfig());
	}

	private String createAnyPreview(ConfigModel config) {
		//is Singleton
		Configuration cfg = FreemarkerConfig.getConfig();

		// Create the root hash
		Map<String, String> root = new HashMap<String, String>();
		// Put strings into map
		try {
			root.put("task", config.getTaskQuestion());
			String picUrl = config.getPictureURL();
			if(picUrl != null){
				root.put("pic", picUrl);
			}else{
				root.put("pic", "");
			}
			String exdesc = config.getTaskDescription();
			if(exdesc != null){
				root.put("exdesc", exdesc);
			}else{
				root.put("exdesc", "");
			}
			root.put("expid", "id");
			String licenseUrl = config.getTaskSourceURL();
			if(licenseUrl != null){

				root.put("iframe", config.getTaskSourceURL());
			} else {
				root.put("iframe", "");
			}
		} catch (DatabaseException e) {
			Logger.logException(e.getMessage());
		}
		root.put("next", "true");		
		root.put("again", "true");		
		root.put("sub", "true");

		try{
			Template temp = cfg.getTemplate("creativetaskpreview.ftl");  
			Writer out = new StringWriter();
			temp.process(root, out);  
			return out.toString();
		}catch(IOException|TemplateException exc){
			Logger.logException(exc.getMessage());
		}
	return "";
	}

}

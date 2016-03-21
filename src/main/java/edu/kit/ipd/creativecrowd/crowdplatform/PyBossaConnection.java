package edu.kit.ipd.creativecrowd.crowdplatform;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import edu.kit.ipd.chimpalot.util.GlobalApplicationConfig;
import edu.kit.ipd.chimpalot.util.Logger;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableWorker;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.persistentmodel.PersistentWorkerRepo;





/**
 * A Task at Pybossa is the equivalent to a HIT on MTurk.
 * 
 * 
 * @author Robin
 *
 */

public class PyBossaConnection extends AbstractConnection {
    private static final String MAINURL = GlobalApplicationConfig.getPyBossaMainURL();
    private static final String ACCESSKEY = GlobalApplicationConfig.getPyBossaAccessKey();
    private static final int PROJECT_ID = GlobalApplicationConfig.getPyBossaProjectId();
    public boolean checkBudget(int amountInCents)  throws ConnectionFailedException, IllegalInputException {
    	Logger.log("[PyBossa]: We currently do not verify whether there are sufficient gift cards for the experiments "
    			+ "\n it may occur that some people do not get paid appropriatly");
    	//Connect to the Gift Card storage and ask if it can work (most likely we do not need this)
        return true;
    }
    /**
     * This method publishes a Task on PyBossa. Note that the meaning of 
     * Task is equivalent to a HIT on MTurk.
     * NOTE: the reason why there is such a convoluted method to extract the id is because
     * the id is generated internally from pybossa and thus cannot be extracted by JACKSON
     * 
     * @param spec the Spec originally thought for MTurk which is reformatted for PyBossa.
     * @return The id of the Task, which is a String containing only numbers. (sufficient to reference it on the server)
     */
    public String publishHIT(HITSpec spec) throws ConnectionFailedException {
    	
    	PyBossaTask task;
    	try {
		task = new PyBossaTask(spec, PROJECT_ID);
    	} catch (NullPointerException e) {
    		throw new ConnectionFailedException(e.getMessage());
    	}
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json ="";
		try {
			json = ow.writeValueAsString(task);
		} catch (JsonProcessingException e1) {
			Logger.error("HITSpec not convertable to JSON");
			
		}

    	String stringyURL = MAINURL + "task?" + ACCESSKEY;
    	String result;
		try {
			result = post(stringyURL, json);
		} catch (IllegalInputException e) {
			throw new ConnectionFailedException();
		}
	      String s = result.toString();
	      if (s.indexOf("\"id\":") == -1) {
	    	  // This means that the field id is not in the json returned from the server
	    	  // either because the server is gone or the resource may not be accessed anymore
	    	  // this should not occur.
	    	  Logger.error("Something is really wrong with the Pybossa Server, connection possible but response useless");
	    	  throw new ConnectionFailedException();
	      }
	      String sub = s.substring(s.indexOf("\"id\":") + 6);
	      if(sub.indexOf(",") == -1) {
	    	  // If the field id is present in the json, it should be terminated by a , otherwise
	    	  //the structure of the json has changed. 
	    	  Logger.error("The JSON returned from PyBossa is corrupt");
	      }
	      String task_id = sub.substring(0, sub.indexOf(","));
    	
    	
		

        return task_id;
    }
    /**
     * This method ends a task. 
     * @param hitId the identifier for the task.
     */
    public void endHIT(String hitId) throws IllegalInputException {
    	
    	
    	
    	String url = MAINURL + "task/" + hitId + "?" + ACCESSKEY;
    	delete(url);
    	
    	
    	
    }
    /**
     * is meant to validate Requester Data but there is no need for this at pybossa
     */
    public boolean validateRequesterData() throws ConnectionFailedException {
    	//Currently Useless, we do not have requester data
    	return super.validateRequesterData();
    }
    /**
     * This method approves a Taskrun and grants the worker credit in the WorkerRepo
     */
    public void approveHIT(AssignmentId assignmentId, WorkerId workerId, String feedback) throws IllegalInputException {
    	PyBossaTaskrun taskrun = getTaskRun(assignmentId.getId());
    	PyBossaTask task = getTask(String.valueOf(taskrun.getTask_id()));
    	int basicPayment = task.getInfo().getRewardCents();
    	try {
			PersistentWorkerRepo repo = new PersistentWorkerRepo();
			MutableWorker worker = repo.loadWorker(assignmentId.getId());
			worker.increaseCredit(basicPayment);
		} catch (DatabaseException e) {
			Logger.logException(e.getMessage());
			throw new IllegalInputException(e.getMessage(), null);
		}
    	
    	
    	
    	
    	
    
    }
    public void sendBonus(AssignmentId asId, WorkerId workerId, int bonusCent, String message) throws IllegalInputException {
    	try {
			PersistentWorkerRepo repo = new PersistentWorkerRepo();
			MutableWorker worker = repo.loadWorker(asId.getId());
			worker.increaseCredit(bonusCent);
		} catch (DatabaseException e) {
			Logger.logException(e.getMessage());
			throw new IllegalInputException(e.getMessage(), null);
		}
        
    }
    /**
     * Deletes a Taskrun, the reason for denying is not presented to the worker.
     */
    public void rejectAssignment(AssignmentId asId, String reason) throws IllegalInputException {

    		String url = MAINURL + "taskrun/" + asId.getId() +"?"+ACCESSKEY;
    		delete(url);
    		
    }
    /**
     * Right as of now this returns the amount of expected assignments regardless of the
     * amount of submitted assignments.
     */
    public int getAvailableAssignments(String hitId) throws IllegalInputException {
    	PyBossaTask task = getTask(hitId);
    	return task.getN_answers();
    	
    }
    /**
     * Extends the amount of available assignments
     */
    public void extendAssignmentNumber(int nrOfAssignments, String hitId) throws IllegalInputException {
    	PyBossaTask task = getTask(hitId);
    	int currentTasks = task.getN_answers();
    	String url = MAINURL + "task/" + hitId + "?" + ACCESSKEY;
    	String number = "{\"n_answers\":" + String.valueOf(currentTasks + nrOfAssignments) + "}";
    	Logger.debug(url + " "+ number);
    	put(url, number);
    	
    	
    	
    } 
    public PlatformIdentity getPlatformIdentity() {
        return PlatformIdentity.PyBossa;
    }
    public String getAbbrevation() {
    	return PlatformIdentity.PyBossa.getPrefix();
    }
    /**
     * This method gets a Task from PyBossa, it uses JACKSON to convert it
     * to a PyBossaTask right away.
     * @param hitId the identifier of the Task
     * @return the Task object
     * @throws IllegalInputException should the requested resource not be available or if something
     * goes wrong in the http request
     */
    private PyBossaTask getTask(String hitId) throws IllegalInputException {
    	String url = MAINURL + "/task/" + hitId;
    	String httpResponse = get(url);
    	ObjectMapper mapper = new ObjectMapper();
    	mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    	PyBossaTask task = null;
    	try {
			task = mapper.readValue(httpResponse, PyBossaTask.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Logger.logException(e.getMessage());
		}
    	return task;
    }
    /**
     * This method gets a TaskRun Object, behavior is ecactly as above
     * @param assignmentId the id of the taskrun
     * @return the taskrun object
     * @throws IllegalInputException should the http get not return a suitable result.
     */
    private PyBossaTaskrun getTaskRun(String assignmentId) throws IllegalInputException {
    	String url = MAINURL + "/taskrun/" + assignmentId;
    	String httpResponse = get(url);
    	ObjectMapper mapper = new ObjectMapper();
    	mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    	PyBossaTaskrun taskrun = null;
    	try {
			taskrun = mapper.readValue(httpResponse, PyBossaTaskrun.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Logger.logException(e.getMessage());
		}
    	return taskrun;
    }
    /**
     * This is the core http get method. 
     * @param urlString the url to which the get should be sent
     * @return the response body as string.
     * @throws IllegalInputException if something went wrong in the http protocols, when the resource is unreadable
     * or if the resource doesn't even exist.
     */
    private String get(String urlString) throws IllegalInputException {
    	String result = "";
    	try {
    		URL url = new URL(urlString);
    		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
    		connection.setRequestMethod("GET");
    		
    	      InputStream is = connection.getInputStream();
    	      BufferedReader rd = new BufferedReader(new InputStreamReader(is));
    	      String line;
    	      StringBuffer response = new StringBuffer(); 
    	      while((line = rd.readLine()) != null) {
    	        response.append(line);
    	        response.append('\r');
    	      }
    	      rd.close();
    	      result = response.toString();
    	      if(connection.getResponseCode() == 404) {
    	    	  throw new IllegalInputException("Resource is not locatable, Error 404", urlString);
    	      }
    	      
        	} catch (MalformedURLException e) {
        		Logger.logException(e.getMessage());
        		Logger.logException("---------------");
        		throw new IllegalInputException("URL is malformed", urlString);
        		
    		} catch (ProtocolException e) {
    			Logger.logException(e.getMessage());
    			Logger.logException("---------------");
    			throw new IllegalInputException("Protocol is not suitable for http" , urlString);
    		} catch (IOException e) {
    			Logger.logException(e.getMessage());
    			Logger.logException("---------------");
    			throw new IllegalInputException(e.getMessage(), urlString);
    		}
        	
            return result;
    }
    /**
     * The core http delete. it will try to delete any object at the specified resource.
     * @param urlString the url
     * @return the response body (should only contain whitespace if successful.)
     * @throws IllegalInputException should it not be possible to successfully delete the targeted resource
     */
    
    
    
    public String testDelete(String urlString) throws IllegalInputException {
    	return delete(urlString);
    }
    public String testUpdate(String urlString, String output) throws IllegalInputException {
    	return put(urlString, output);
    	
    }
    private String delete(String urlString) throws IllegalInputException {
    	String result = "";
    	int responseCode = 555;
    	try {
    		URL url = new URL(urlString);
        	HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
    		httpCon.setDoOutput(true);
    		httpCon.setRequestProperty(
    		    "Content-Type", "application/json" );
    		httpCon.setRequestMethod("DELETE");
    		httpCon.connect();
    		responseCode = httpCon.getResponseCode();
    	    Logger.log(httpCon.getResponseMessage());
    	    result = httpCon.getResponseMessage();

    	} catch (IOException e) {
    		Logger.logException(e.getMessage());
    		throw new IllegalInputException("cannot delete targeted source. Are you connected to the internet?", urlString);
    	}
    	Logger.debug(result);
    	if (responseCode >= 400 && responseCode != 404) {
    		//something went wrong
    		throw new IllegalInputException("the targeted resource cannot be deleted,"
    				+ " maybe it does not exist, maybe you cannot access it", urlString);
    	}
    	
    	return result;
    	
    }
    /**
     * the core http post method, will try to post a string to the target location
     * @param urlString the target location url
     * @param output the output that shall be used
     * @return the returncode of the request
     * @throws IllegalInputException if something goes wrond in the write process
     */
    private String post(String urlString, String output) throws IllegalInputException {
    	return httpWrite(urlString, output, "POST");
    }
    /**
     * the core http put method will try to put a string to the target location
     * @param urlString the target location url
     * @param output the output that shall be used
     * @return the returncode of the request
     * @throws IllegalInputException if something goes wrond in the write process
     */
    private String put(String urlString, String output) throws IllegalInputException {
    	return httpWrite(urlString, output, "PUT");
    }
    /**
     * This is the shared method from post and put, it will try to write the given output
     * to the destined location. 
     * @param urlString the url
     * @param output the string output
     * @param method whether is shall post or put
     * @return the return code
     * @throws IllegalInputException if something throws an exception in the write process.
     */
    private String httpWrite(String urlString, String output, String method) throws IllegalInputException {
    	String returnCode = "";
    	try {
    	URL url = new URL(urlString);
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		connection.setRequestMethod(method);
		connection.setRequestProperty("Content-Type", "text/html; charset=utf-8");;
		connection.setDoOutput(true);
		connection.setDoInput(true);
			
		
	      DataOutputStream wr = new DataOutputStream (
                  connection.getOutputStream ());
	      
	      wr.writeBytes(output);
	      wr.flush();
	      wr.close();

	      InputStream is = connection.getInputStream();
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	      String line;
	      StringBuffer response = new StringBuffer(); 
	      while((line = rd.readLine()) != null) {
	        response.append(line);
	        response.append('\r');
	      }
	      rd.close();
	      returnCode =  response.toString();
    	} catch (IOException e) {
			Logger.debug(returnCode);
			Logger.logException(e.getMessage());
			throw new IllegalInputException("Cannot perform http request " + method + " is it possible to build up a connection",
					urlString + " " + output + " " + returnCode);
		}
    	return returnCode;
    }
}

package edu.kit.ipd.creativecrowd.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Map;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.Part;

import spark.Request;
import spark.Response;

import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

import edu.kit.ipd.creativecrowd.connector.Connector;
import edu.kit.ipd.creativecrowd.connector.IModel;
import edu.kit.ipd.creativecrowd.connector.ModelException;
import edu.kit.ipd.creativecrowd.readablemodel.Experiment;
import edu.kit.ipd.creativecrowd.readablemodel.ExperimentSpec;
import edu.kit.ipd.creativecrowd.util.GlobalApplicationConfig;
import edu.kit.ipd.creativecrowd.util.Logger;
import edu.kit.ipd.creativecrowd.view.ViewFacade;

/**
 * The class ExperimentController manages all operations
 * that have to do with the status of an experiment
 * 
 * @author simon
 */

public class ExperimentController extends AbstractController {
	private ViewFacade view;

	/**
	 * provides a String with some statistics about an experiment
	 * 
	 * @param sparkRequest the request sent by the client
	 * @param resp the response to be sent back to the server
	 * @return a spark.Response object containing a String with the most important statistics about the requested experiment
	 */
	public Response getStatistics(Request sparkRequest, Response resp) {
		if (!authenticateRequester(sparkRequest, resp))
			return resp;

		String expId = sparkRequest.params("expID");

		IModel connector = new Connector();
		try {
			Experiment ex = connector.getExperiment(expId);
			view = new ViewFacade();
			if (sparkRequest.pathInfo().equals("/experiments/" + expId + "/csv")) {
				resp.body(view.showExport(ex, "csv"));
				return resp;
			} else if (sparkRequest.pathInfo().equals("/experiments/" + expId + "/txt")) {
				resp.body(view.showExport(ex, "txt"));
				return resp;
			}
			String finalView = view.showStatus(ex);
			resp.status(200);
			resp.body(finalView);
		} catch (ModelException e) {
			System.out.println(e.getMessage());
		}

		return resp;
	};

	/**
	 * create a new experiment according to the parameters set by the client
	 * 
	 * @param sparkRequest the request sent by the client
	 * @param resp the response to be sent back to the server
	 * @return spark.Response object containing a String with the most important statistics about the newly created experiment
	 */
	public Response createExperiment(Request sparkRequest, Response resp) {
		if (!authenticateRequester(sparkRequest, resp))
			return resp;

		String actualContentType = sparkRequest.headers("Content-Type");
		if (actualContentType == null || !actualContentType.equals("application/json")) {
			resp.body("Content-Type must be 'application/json'!");
			resp.status(400);
			resp.type("text/plain");
			return resp;
		}

		String configString = sparkRequest.body();

		configString = new JSONMinifier().minify(configString);

		String id = sparkRequest.params("id");

		ExperimentSpec spec;

		try {
			JSONParser parser = new JSONParser();
			Map jsonVal = (Map) parser.parse(configString);
			Integer budget = intFromJSON(jsonVal, "budget");
			String picture = stringFromJSON(jsonVal, "task_picture_url");
			String picLicense = stringFromJSON(jsonVal, "task_picture_license_url");
			String desc = stringFromJSON(jsonVal, "task_description");
			String question = stringFromJSON(jsonVal, "question");
			String ratingTaskQuestion = stringFromJSON(jsonVal, "ratingTaskQuestion");
			int basicPaymentHIT = intFromJSON(jsonVal, "basicPaymentHIT");
			int basicPaymentAnswer = intFromJSON(jsonVal, "basicPaymentAnswer");
			int bonusPayment = intFromJSON(jsonVal, "bonusPayment");
			int basicPaymentRating = intFromJSON(jsonVal, "basicPaymentRating");
			Map<String, String> strategyParams = (Map<String, String>) jsonVal.get("strategyParams");
			Iterable<String> qualifications = (Iterable<String>) jsonVal.get("qualifications");
			Iterable<String> tags = (Iterable<String>) jsonVal.get("tags");
			Map<String, Double> ratingOptions = (Map<String, Double>) jsonVal.get("ratingOptions");
			String hitTi = stringFromJSON(jsonVal, "hit_title");
			String hitDesc = stringFromJSON(jsonVal, "hit_description");
			int maxAnsNum = intFromJSON(jsonVal, "maxAnswersPerAssignment");
			int maxRatNum = intFromJSON(jsonVal, "maxRatingsPerAssignment");
			String ratViewClass = stringFromJSON(jsonVal, "ratingView_class");
			spec = new ExperimentSpecFromConfig(budget, basicPaymentHIT, basicPaymentAnswer, bonusPayment, basicPaymentRating,
					question, ratingTaskQuestion, picture, picLicense, id, desc, ratViewClass, ratingOptions, strategyParams, qualifications, tags, hitTi, hitDesc, maxAnsNum, maxRatNum);
		} catch (ParseException pe) {
			Logger.logException(pe.getMessage());
			resp.body("Error while parsing experiment config: " + pe.toString());
			resp.status(400);
			resp.type("text/plain");
			return resp;
		} catch (InvalidInputException e) {
			Logger.logException(e.getMessage());
			resp.body("Invalid experiment config: " + e.getMessage());
			resp.status(400);
			resp.type("text/plain");
			return resp;
		}

		IModel model = new Connector();
		try {
			model.createExperimentFromSpecs(spec);
			Experiment ex = model.getExperiment(id);
			view = new ViewFacade();
			resp.body(view.showStatus(ex));
			resp.status(200);
			resp.type("text/html");
		} catch (ModelException e) {
			Logger.logException(e.getMessage());
		}

		return resp;
	}

	private String stringFromJSON(Map sourceJson, String key) {
		Object v = sourceJson.get(key);
		if (v == null)
			throw new InvalidInputException("Missing field " + key);
		return (String) v;
	};

	private int intFromJSON(Map sourceJson, String key) {
		Object v = sourceJson.get(key);
		if (v == null)
			throw new InvalidInputException("Missing field " + key);
		return (int) (long) v;
	};

	/**
	 * delete an experiment
	 * 
	 * @param sparkRequest the request containing the ID of the experiment to be deleted
	 * @param resp the response to be sent back to the client
	 * @return a spark.Response object
	 */
	public Response deleteExperiment(Request sparkRequest, Response resp) {
		if (!authenticateRequester(sparkRequest, resp))
			return resp;

		String expId = sparkRequest.params("expID");
		Connector connector = new Connector();
		view = new ViewFacade();

		try {
			connector.deleteExperiment(expId);
			resp.body(view.showDialog("successfully deleted experiment with id " + expId));
		} catch (ModelException ex) {
			Logger.logException(ex.getMessage());
			resp.body(view.showDialog(ex.getMessage()));
		}
		return resp;
	};

	/**
	 * end an experiment, pay all workers
	 * 
	 * @param sparkRequest the request containing the ID of the experiment to be ended
	 * @param resp the response to be sent back to the client
	 * @return a spark.Response object
	 */
	public Response endExperiment(Request sparkRequest, Response resp) {
		if (!authenticateRequester(sparkRequest, resp))
			return resp;

		String expId = sparkRequest.params("expID");
		IModel connector = new Connector();
		view = new ViewFacade();
		try {
			connector.endExperiment(expId);
		} catch (ModelException e) {
			resp.body(view.showDialog(e.getMessage()));
			return resp;
		}
		resp.body(view.showDialog("successfully ended experiment with id " + expId + "\n"));

		return resp;
	}

	private String fileToString(Part p) {
		try {
			java.util.Scanner s = new java.util.Scanner(p.getInputStream()).useDelimiter("\\A");
			return s.hasNext() ? s.next() : "";
		} catch (Exception ex) {
			Logger.logException(ex.getMessage());
		}
		return "";
	}
	
	/**
	 * exits the entire programm
	 * @param sparkRequest the request
	 * @param resp the response
	 */
	public void exit(Request sparkRequest, Response resp){
		if(authenticateRequester(sparkRequest, resp))
		{
			System.exit(0);
		}
	}

	/**
	 * Try to authenticate the requester given the data sent in the request.
	 * If authentication is not required or was successful, returns true.
	 * If authentication failed, return false, and sets a HTTP 401 Unauthorized
	 * response code and text. In this case, the caller should not write to the response
	 * object anymore.
	 * 
	 * @param req The user's request.
	 * @param resp The response being created.
	 * @return true on success, false on failed authentication
	 */
	protected boolean authenticateRequester(Request req, Response resp) {
		String creds = GlobalApplicationConfig.getRequesterCredentials();
		if (creds.equals(""))
			return true; // auth is disabled
		boolean ok = false;

		String authData = req.headers("Authorization");

		if (authData != null) {
			String[] parts = authData.split(" ");
			if (parts.length == 2) {
				if (parts[0].equals("Basic")) {
					String encodedCreds;
					try {
						encodedCreds = new String(Base64.getEncoder().encode(creds.getBytes()), "UTF-8");
					} catch (UnsupportedEncodingException e) {
						throw new Error("Failed to encode credentials");
					}
					if (parts[1].equals(encodedCreds)) {
						// creds ok!
						ok = true;
					}
				}
			}
		}

		if (ok) {
			// all is well
			return true;
		} else {
			// nope. write the response immediately.
			resp.body("Invalid credentials.");
			resp.status(401);
			resp.type("text/plain");
			return false;
		}
	}
}

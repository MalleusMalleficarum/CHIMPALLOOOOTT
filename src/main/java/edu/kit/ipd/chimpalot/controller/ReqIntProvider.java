package edu.kit.ipd.chimpalot.controller;

import java.lang.Iterable;
import java.util.LinkedList;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.kit.ipd.chimpalot.jsonclasses.CalibrationQuestionJson;
import edu.kit.ipd.chimpalot.jsonclasses.ConfigModelJson;
import edu.kit.ipd.chimpalot.jsonclasses.ExperimentDetails;
import edu.kit.ipd.chimpalot.util.GlobalApplicationConfig;
import edu.kit.ipd.chimpalot.util.Logger;
import edu.kit.ipd.creativecrowd.connector.Connector;
import edu.kit.ipd.creativecrowd.connector.ModelException;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.CalibrationQuestion;
import edu.kit.ipd.creativecrowd.readablemodel.ConfigModel;
import edu.kit.ipd.creativecrowd.readablemodel.Experiment;
import edu.kit.ipd.creativecrowd.readablemodel.Worker;
import edu.kit.ipd.creativecrowd.view.ViewFacade;

/**
 * ReqIntProvider is responsible for all requests of the frontend which do not result in any changes to the database.
 *
 * @author Thomas Friedel
 *
 */
@RestController
@RequestMapping("/requester")
public class ReqIntProvider {

	private Connector provider;
	private String previewCreative;
	private String previewRating;

	/**
	 *
	 * @return a list of the names of all saved Configfiles.
	 */
	@RequestMapping(value = "/configfile", method = RequestMethod.GET)
	public Iterable<String> getSavedConfigs() {
		Logger.log("Requested list of configfiles.");
		LinkedList<String> result = new LinkedList<String>();
		try {
			Iterable<ConfigModel> confModels = getConnector().getConfigList();
			for (ConfigModel conf : confModels)	{
				result.add(conf.getID());
			}
		} catch (ModelException e) {
			//Logging is already done in Connector.
			throw new InternalServerException(e.getMessage());
		} catch (DatabaseException e) {
			Logger.logException(e.getMessage());
			throw new InternalServerException(e.getMessage());
		}
		return result;
	}

	/**
	 *
	 * @return a list of the ids of all saved CalibrationQuestions.
	 */
	@RequestMapping(value = "/calibrationquestion", method = RequestMethod.GET)
	public Iterable<CalibrationQuestionJson> getCalibrationQuestions() {
		Logger.log("Requested list of CalibrationQuestions.");
		LinkedList<CalibrationQuestionJson> result = new LinkedList<CalibrationQuestionJson>();
		try {
			Iterable<CalibrationQuestion> calibList = getConnector().getCalibList();
			for (CalibrationQuestion calib : calibList) {
				result.add(new CalibrationQuestionJson(calib));
			}
		} catch (ModelException e) {
			//Logging is already done in Connector.
			throw new InternalServerException(e.getMessage());
		}
		return result;
	}

	/**
	 *
	 * @return a list of all the workers
	 */
	@RequestMapping(value = "/worker", method = RequestMethod.GET)
	public Iterable<Worker> getWorker() {
		Logger.log("Requested list of worker.");
		try {
			return getConnector().getWorkerList();
		} catch (ModelException e) {
			//Logging is already done in Connector.
			throw new InternalServerException(e.getMessage());
		}
	}

	/**
	 *
	 * @return a list with details about all existing experiments
	 */
	@RequestMapping(value = "/experiment", method = RequestMethod.GET)
	public Iterable<ExperimentDetails> getExperiments() {
		Logger.log("Requested list of experiments.");
		LinkedList<ExperimentDetails> result = new LinkedList<ExperimentDetails>();
		try {
			Iterable<Experiment> expList = getConnector().getExperimentList();
			for (Experiment exp : expList)	{
				result.add(new ExperimentDetails(exp));
			}
		} catch (ModelException e) {
			//Logging is already done in Connector.
			throw new InternalServerException(e.getMessage());
		} catch (DatabaseException e) {
			Logger.logException(e.getMessage());
			throw new InternalServerException(e.getMessage());
		}
		return result;
	}

	/**
	 *
	 * @param name the name of the Configfile
	 * @return The ConfigFile with the name {@code name}
	 */
	@RequestMapping(value = "/configfile/{id}", method = RequestMethod.GET)
	public ConfigModelJson loadConfig(@PathVariable(value = "id") String id) {
		try {
			return new ConfigModelJson(getConnector().getConfigModel(id));
		} catch (ModelException e) {
			throw new ResourceNotFoundException(e.getMessage());
		} catch (DatabaseException e) {
			Logger.logException(e.getMessage());
			throw new InternalServerException(e.getMessage());
		}
	}

	/**
	 *
	 * @param id the id of the CalibrationQuestion
	 * @return the CalibrationQuestion
	 */
	@RequestMapping(value = "/calibrationquestion/{id}", method = RequestMethod.GET)
	public CalibrationQuestionJson getCalibrationQuestion(@PathVariable(value = "id") String id) {
		try {
			return new CalibrationQuestionJson(getConnector().getCalibrationQuestion(id));
		} catch (ModelException e) {
			throw new ResourceNotFoundException(e.getMessage());
		}
	}

	/**
	 *
	 * @param id id of the worker
	 * @return the not-anonymized worker
	 */
	@RequestMapping(value = "/worker/{id}", method = RequestMethod.GET)
	public Worker getWorker(@PathVariable(value = "id") String id) {
		try {
			return getConnector().getWorker(id);
		} catch (ModelException e) {
			throw new ResourceNotFoundException(e.getMessage());
		}
	}

	/**
	 * Generates a preview for a creative task
	 * @param conf the Configfile to generate the preview for
	 */
	@RequestMapping(value = "/preview/creative", method = RequestMethod.POST)
	public void updateCreativePreview(@RequestBody ConfigModelJson conf) {
		ViewFacade view = new ViewFacade();
		previewCreative = view.previewCreative(conf);
	}
	
	/**
	 * 
	 * @return an html-preview of a creative task generated in the last call to
	 * {@link #updateCreativePreview(ConfigModelJson)}
	 */
	@RequestMapping(value = "/preview/creative", method = RequestMethod.GET)
	public String showCreativePreview() {
		return previewCreative;
	}

	/**
	 * Generates a rating preview
	 * @param conf the Configfile to generate the preview for
	 */
	@RequestMapping(value = "/preview/rating", method = RequestMethod.POST)
	public void updateRatingPreview(@RequestBody ConfigModelJson conf) {
		ViewFacade view = new ViewFacade();
		previewRating = view.previewRating(conf);
	}
	
	/**
	 * 
	 * @return an html-preview of a rating task generated in the last call to
	 *  {@link #updateRatingPreview(ConfigModelJson)}
	 */
	@RequestMapping(value = "/preview/rating", method = RequestMethod.GET)
	public String showRatingPreview() {
		return previewRating;
	}

	/**
	 *
	 * @param expid the id of the experiment
	 * @return some details about the experiment
	 */
	@RequestMapping(value = "/experiment/{id}", method = RequestMethod.GET)
	public ExperimentDetails getExperimentDetails(@PathVariable(value = "id") String expid) {
		try {
			Experiment exp = getConnector().getExperiment(expid);
			return new ExperimentDetails(exp);
		} catch (ModelException e) {
			//Logging already done in Connector.
			throw new ResourceNotFoundException(e.getMessage());
		} catch (DatabaseException e) {
			Logger.logException(e.getMessage());
			throw new InternalServerException(e.getMessage());
		}
	}

	/**
	 *
	 * @param expid the id of the experiment
	 * @return a .csv-file of the experiment
	 */
	@RequestMapping(value = "/experiment/{id}/CSV", method = RequestMethod.GET)
	public String getExperimentResultsCSV(@PathVariable(value = "id") String expid) {
		ViewFacade view = new ViewFacade();
		Experiment exp;
		try {
			exp = getConnector().getExperiment(expid);
		} catch (ModelException e) {
			throw new ResourceNotFoundException(e.getMessage());
		}
		//String json = "{ \"content\": \"" + view.showExport(exp, "csv");
		return view.showExport(exp, "csv");
		//return json + "\"}";
	}

	/**
	 *
	 * @param expid the id of the experiment
	 * @return a .txt-file representation of the experiment
	 */
	@RequestMapping(value = "/experiment/{id}/TXT", method = RequestMethod.GET)
	public String getExperimentResultsTXT(@PathVariable(value = "id") String expid) {
		ViewFacade view = new ViewFacade();
		Experiment exp;
		try {
			exp = getConnector().getExperiment(expid);
		} catch (ModelException e) {
			throw new ResourceNotFoundException(e.getMessage());
		}
		//String json = "{ \"content\": \"" + view.showExport(exp, "txt");
		return view.showExport(exp, "txt");
		//return json + "\"}";
	}

	/**
	 *
	 * @return a jsonified representation of GlobalApplicationConfig
	 */
	@RequestMapping(value = "/globalconfig", method = RequestMethod.GET)
	public String getGlobalApplicationConfig() {

		return GlobalApplicationConfig.jsonify();
	}

	/**
	 * No need to create new instances for every method.
	 *
	 * @return a Connector instance
	 */
	private Connector getConnector() {
		if (this.provider == null) {
			this.provider = new Connector();
		}
		return provider;
	}
}

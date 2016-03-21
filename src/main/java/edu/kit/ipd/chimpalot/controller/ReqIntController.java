package edu.kit.ipd.chimpalot.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.kit.ipd.chimpalot.jsonclasses.CalibrationQuestionJson;
import edu.kit.ipd.chimpalot.jsonclasses.ConfigModelJson;
import edu.kit.ipd.chimpalot.util.GlobalApplicationConfig;
import edu.kit.ipd.chimpalot.util.Logger;
import edu.kit.ipd.creativecrowd.connector.Connector;
import edu.kit.ipd.creativecrowd.connector.ModelException;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.CalibrationQuestion;

/**
 * ReqIntController is responsible for all requests of the frontend which do result in changes to the database.
 *
 * @author Thomas Friedel
 *
 */
@RestController
@RequestMapping("/requester")
public class ReqIntController {

	private Connector connector;

	/**
	 * Saves the given configfile on the server with the id {@code id}.
	 *
	 * @param id the id of the configfile
	 * @param conf the configfile
	 */
	@RequestMapping(value = "/configfile/{id}", method = RequestMethod.POST)
	public void saveConfig(@PathVariable("id") String id, @RequestBody ConfigModelJson conf) {
		conf.setID(id);
		try {
			getConnector().createConfigModel(conf);
		} catch (ModelException e) {
			throw new ConflictException(e.getMessage());
		}
		Logger.log("Created config with id: '" + id + "'");
	}

	/**
	 * Deletes the configfile with the id {@code id}.
	 *
	 * @param id the id
	 */
	@RequestMapping(value = "/configfile/{id}", method = RequestMethod.DELETE)
	public void deleteConfig(@PathVariable("id") String id) {
		try {
			String expid = getConnector().getConfigModel(id).getExperimentID();
			if (expid != null && !expid.isEmpty()) {
				throw new BadRequestException("Configfile '" + id + "' is used by experiment '" + expid
						+ "', can't delete.");
			}
			getConnector().deleteConfigModel(id);
		} catch (ModelException e) {
			throw new ResourceNotFoundException(e.getMessage());
		} catch (DatabaseException e) {
			Logger.logException(e.getMessage());
			throw new InternalServerException(e.getMessage());
		}
		Logger.log("Deleted config with id: '" + id + "'");
	}

	/**
	 * Saves the given CalibrationQuestion in the database.
	 *
	 * @param calib the CalibrationQuestion
	 * @return the id of the calibrationQuestion
	 */
	@RequestMapping(value = "/calibrationquestion", method = RequestMethod.POST)
	public String saveCalibrationQuestion(@RequestBody CalibrationQuestionJson calib) {
		try {
			String jsonified = "{\"id\":\"";
			jsonified += getConnector().createCalibrationQuestion(calib);
			jsonified += "\"}";
			Logger.log("Created calibrationquestion: '" + jsonified + "'");
			return jsonified;
		} catch (ModelException e) {
			throw new InternalServerException(e.getMessage());
		}
	}
	
	/**
	 * Deletes the calibrationquestion with the id {@code id}.
	 *
	 * @param id the id
	 */
	@RequestMapping(value = "/calibrationquestion/{id}", method = RequestMethod.DELETE)
	public void deleteCalibrationQuestion(@PathVariable("id") String id) {
		try {
			CalibrationQuestion calib = getConnector().getCalibrationQuestion(id);
			if (!calib.getExperiments().isEmpty()) {
				throw new BadRequestException("Calibrationquestion '" + id 
						+ "' is still used by experiments, can't delete.");
			}
			getConnector().deleteCalibrationQuestion(id);
		} catch (ModelException e) {
			throw new ResourceNotFoundException(e.getMessage());
		} catch (DatabaseException e) {
			Logger.logException(e.getMessage());
			throw new InternalServerException(e.getMessage());
		}
		Logger.log("Deleted calibrationquestion with id: '" + id + "'");
	}

	/**
	 * Starts a new experiment
	 *
	 * @param conf the configfile to start the new experiment with
	 */
	@RequestMapping(value = "/experiment/{id}/start", method = RequestMethod.POST)
	//Not allow GET, since GET should be safe
	public ResponseEntity<List<String>> startExperiment(@PathVariable("id") String expid,
			@RequestBody ConfigModelJson conf) {
		List<String> ret;
		HttpStatus status = HttpStatus.OK;
		Logger.log("Attempting to start experiment '" + expid + "'.");
		try {
			ret = getConnector().createExperimentFromConfig(conf, expid);
		} catch (ModelException e) {
			throw new InternalServerException(e.getMessage());
		}
		if (!ret.isEmpty()) {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<List<String>>(ret, status);
	}

	/**
	 * Changes a running experiment
	 *
	 * @param expid the id of the running experiment
	 * @param restart sets whether the experiment stops and starts again,
	 *  or if it should try to change while running
	 * @param conf the new configuration for the experiment
	 */
	@RequestMapping(value = "/experiment/{id}/change", method = RequestMethod.POST)
	//Not allow GET, since GET should be safe
	public ResponseEntity<List<String>> changeExperiment(@PathVariable("id") String expid,
			@RequestParam(value = "restart", defaultValue = "true") boolean restart,
			@RequestBody ConfigModelJson conf) {
		Logger.log("Attempting to change experiment '" + expid + "'.");
		if (restart) {
			try {
				getConnector().stopCreativeTasks(expid);
			} catch (ModelException e) {
				if (e.getCause() instanceof DatabaseException) {
					throw new ResourceNotFoundException(e.getMessage());
				} else {
					throw new BadRequestException(e.getMessage());
				}
			}
			//Start new experiment
			String postfix = "_AMD";
			try {
				List<String> validation = getConnector().createExperimentFromConfig(conf, expid + postfix);
				if (!validation.isEmpty()) {
					if (validation.size() == 1 && validation.contains("expid")) { 
						//In case an experiment with the id 'expid + postfix' already exists. 
						int n = 0;
						 do {
							n++;
							postfix = "_AMD" + n;
							validation = getConnector().createExperimentFromConfig(conf, expid + postfix);
						} while (validation.contains("expid"));
					} else {
						return new ResponseEntity<List<String>>(validation, HttpStatus.BAD_REQUEST);
					}
				}
			} catch (ModelException e) {
				throw new InternalServerException(e.getMessage());
			}
			return new ResponseEntity<List<String>>(new LinkedList<String>(), HttpStatus.OK);
		} else {
			List<String> validation = new LinkedList<String>();
			try {
				validation = getConnector().amendExperiment(expid, conf);
			} catch (ModelException e) {
				throw new InternalServerException(e.getMessage());
			}
			if (validation.isEmpty()) {
				return new ResponseEntity<List<String>>(validation, HttpStatus.OK);
			} else {
				return new ResponseEntity<List<String>>(validation, HttpStatus.BAD_REQUEST);
			}
		}
	}

	/**
	 * The experiment {@code expid} stops to create new assignments with creative tasks.
	 * The body of the http-request should be empty.
	 *
	 * @param expid the id of the running experiment
	 */
	@RequestMapping(value = "/experiment/{id}/stopcreativetasks", method = RequestMethod.POST)
	//Not allow GET, since GET should be safe
	public void stopCreativeTasks(@PathVariable("id") String expid) {
		Logger.log("Attempting to stop creative tasks on experiment '" + expid + "'.");
		try {
			getConnector().stopCreativeTasks(expid);
		} catch (ModelException e) {
			if (e.getCause() instanceof DatabaseException) {
				throw new ResourceNotFoundException(e.getMessage());
			} else {
				throw new BadRequestException(e.getMessage());
			}
		}
	}

	/**
	 * Stops the experiment. The body of the http-request should be empty.
	 *
	 * @param expid the id of the running experiment
	 */
	@RequestMapping(value = "/experiment/{id}/end", method = RequestMethod.POST)
	//Not allow GET, since GET should be safe
	public void stopExperiment(@PathVariable("id") String expid) {
		Logger.log("Attempting to stop experiment '" + expid + "'.");
		try {
			getConnector().endExperiment(expid);
		} catch (ModelException e) {
			throw new ResourceNotFoundException(e.getMessage());
		}
	}

	/**
	 * Deletes the stopped experiment
	 *
	 * @param expid the id of the stopped experiment
	 */
	@RequestMapping(value = "/experiment/{id}", method = RequestMethod.DELETE)
	public void deleteExperiment(@PathVariable("id") String expid) {
		try {
			if (!getConnector().getExperiment(expid).isFinished()) {
				throw new BadRequestException("Experiment '" + expid + "' is still running, can't delete.");
			}
			getConnector().deleteExperiment(expid);
		} catch (DatabaseException e) {
			Logger.logException(e.getMessage());
			throw new InternalServerException(e.getMessage());
		} catch (ModelException e) {
			//Logging already done in Connector
			throw new ResourceNotFoundException(e.getMessage());
		}
		Logger.log("Deleted experiment '" + expid + "'.");
	}

	/**
	 * Anonymize the worker
	 *
	 * @param wrkid the id of the worker
	 */
	@RequestMapping(value = "/worker/{id}", method = RequestMethod.DELETE)
	public void anonymizeWorker(@PathVariable("id") String wrkid) {
		try {
			getConnector().anonymizeWorker(wrkid);
		} catch (ModelException e) {
			throw new ResourceNotFoundException(e.getMessage());
		}
	}

	/**
	 * Pays a single worker.
	 * 
	 * @param wrkid the id of the worker
	 */
	@RequestMapping(value = "/worker/{id}/pay", method = RequestMethod.POST)
	public void payWorker(@PathVariable("id") String wrkid) {
		try {
			if (!getConnector().payWorker(wrkid)) {
				String message = "Worker '" + wrkid + "' could not be payed.";
				Logger.logException(message);
				throw new BadRequestException(message);
			}
		} catch (ModelException e) {
			throw new ResourceNotFoundException(e.getMessage());
		}
	}
	
	/**
	 * Pays all workers.
	 */
	@RequestMapping(value = "/worker/pay", method = RequestMethod.POST)
	public void payAllWorkers() {
		try {
			if (!getConnector().payAllWorkers()) {
				String message = "At least one worker could not be payed.";
				Logger.logException(message);
				throw new BadRequestException(message);
			}
		} catch (ModelException e) {
			throw new ResourceNotFoundException(e.getMessage());
		}
	}
	
	/**
	 * Blocks or unblocks the worker from submitting assignments to all experiments.
	 * 
	 * @param wrkid the id of the worker
	 * @param block {@code true} blocks the worker, {@code false} unblocks him.
	 * @throws ModelException if there is no worker with the id {@code wrkid}.
	 */
	@RequestMapping(value = "/worker/{id}/block", method = RequestMethod.POST)
	public void blockWorker(@PathVariable("id") String wrkid, @RequestParam(value = "block", defaultValue = "true") boolean block) {
		try {
			getConnector().blockWorker(wrkid, block);
		} catch (ModelException e) {
			throw new ResourceNotFoundException(e.getMessage());
		}
	}
	
	/**
	 * Sets the payment threshold for the PyBossa workers. Must be at least 500ct.
	 * 
	 * @param threshold the new threshold
	 */
	@RequestMapping(value = "/config/payment", method = RequestMethod.POST)
	public void setPaymentThreshold(@RequestParam(value = "threshold", required = true) int threshold) {
		try {
			GlobalApplicationConfig.setPyBossaPaymentThreshold(threshold);
		} catch (IllegalArgumentException e) {
			throw new BadRequestException(e.getMessage());
		}
	}
	
	/**
	 * Adds amazon vouchers to the database given by a string
	 * 
	 * @param input a subsequent list of 'vouchercode:vouchervalue(in cent);'
	 * @return the number of successfully added vouchers
	 */
	@RequestMapping(value = "/worker/vouchers", method = RequestMethod.PUT)
	public int addAmazonVouchers(@RequestBody String input) {
		try {
			return getConnector().addAmazonVouchers(input);
		} catch (ModelException e) {
			throw new InternalServerException(e.getMessage());
		}
	}

	/**
	 * No need to create new instances for every method.
	 *
	 * @return a Connector instance
	 */
	private Connector getConnector() {
		if (this.connector == null) {
			this.connector = new Connector();
		}
		return this.connector;
	}
}

package edu.kit.ipd.chimpalot;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.kit.ipd.chimpalot.jsonclasses.CalibrationQuestionJson;
import edu.kit.ipd.chimpalot.jsonclasses.ConfigModelJson;
import edu.kit.ipd.chimpalot.util.Alert;
import edu.kit.ipd.chimpalot.util.GlobalApplicationConfig;
import edu.kit.ipd.chimpalot.util.Logger;
import edu.kit.ipd.creativecrowd.connector.Connector;
import edu.kit.ipd.creativecrowd.connector.ModelException;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.persistentmodel.ExperimentWatchdog;
import edu.kit.ipd.creativecrowd.persistentmodel.PersistentAmazonVoucherRepo;
import edu.kit.ipd.creativecrowd.persistentmodel.PersistentCalibrationQuestionRepo;
import edu.kit.ipd.creativecrowd.persistentmodel.PersistentConfigModelRepo;
import edu.kit.ipd.creativecrowd.persistentmodel.PersistentExperimentRepo;
import edu.kit.ipd.creativecrowd.persistentmodel.PersistentWorkerRepo;
import edu.kit.ipd.creativecrowd.readablemodel.Experiment;

/**
 * Holds the main method.
 * 
 * @author Thomas Friedel
 */
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		
		if (args.length >= 1) {
			Logger.setLoggerState(args[0]);
		}
		Logger.error("Starting up...");
		
		GlobalApplicationConfig.configure(); //Uses dev, if Spring profile is not "prod"
		try {
			new PersistentExperimentRepo(); //Build database at start to (hopefully) prevent database locks.
			new PersistentConfigModelRepo();
			new PersistentAmazonVoucherRepo();
			new PersistentCalibrationQuestionRepo();
			new PersistentWorkerRepo();
		} catch (DatabaseException e) {
			Logger.logException(e.getMessage());
			System.exit(1); //Database could not be build.
		}
		
		//If the database is empty, create some samples
		Connector conn = new Connector();
		ObjectMapper jacksonObjectMapper = new ObjectMapper();
		try {
			if (conn.getConfigList().isEmpty()) {
				ConfigModelJson config =
						jacksonObjectMapper.readValue(Application.class.getResource("/example-config.json"),
						ConfigModelJson.class);
				config.setID("example");
				conn.createConfigModel(config);
			}
		} catch (ModelException e) {
			//Logging already done in Connector.
		} catch (Exception e) {
			Logger.logException(e.getMessage());
			//Failed to load example config. While not being a good sign, this is not fatal.
			//Carrying on.
		}
		try {
			if (conn.getCalibList().isEmpty()) {
				conn.createCalibrationQuestion(
						jacksonObjectMapper.readValue(Application.class.getResource("/example-calibrationquestion.json"),
					CalibrationQuestionJson.class));
			}
		} catch (ModelException e) {
			//Logging already done in Connector.
		} catch (Exception e) {
			Logger.logException(e.getMessage());
			//Failed to load example calibrationquestion. While not being a good sign, this is not fatal.
			//Carrying on.
		}
		//make sure watchdogs are created for all running experiments
		try {
			List<Experiment> all = conn.getExperimentList();
			for (Experiment exp: all) {
				if (!exp.isFinished()) {
					new ExperimentWatchdog(exp.getID());
				}
			}
		} catch (ModelException e) {
			//logging done in Connector
		} catch (DatabaseException e) {
			Logger.logException(e.getMessage());
		}
		//add all the requesters to notify to the alert list
		List<String> allReq = GlobalApplicationConfig.getNotifiedRequesterMail();
		for(String s: allReq) {
			Alert.registerRequester(s);
		}
		
		
		SpringApplication.run(Application.class, args);		
	}
}

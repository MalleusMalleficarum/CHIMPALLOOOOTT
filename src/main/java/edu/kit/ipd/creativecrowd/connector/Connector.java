package edu.kit.ipd.creativecrowd.connector;


import edu.kit.ipd.chimpalot.jsonclasses.CalibrationAnswerJson;
import edu.kit.ipd.chimpalot.util.Logger;
import edu.kit.ipd.creativecrowd.crowdplatform.AssignmentId;
import edu.kit.ipd.creativecrowd.crowdplatform.ConnectionFailedException;
import edu.kit.ipd.creativecrowd.crowdplatform.IllegalInputException;
import edu.kit.ipd.creativecrowd.crowdplatform.PlatformIdentity;
import edu.kit.ipd.creativecrowd.crowdplatform.WorkerId;
import edu.kit.ipd.creativecrowd.database.DatabaseConnection;
import edu.kit.ipd.creativecrowd.database.SQLiteDatabaseConnection;
import edu.kit.ipd.creativecrowd.mutablemodel.CalibrationQuestionRepo;
import edu.kit.ipd.creativecrowd.mutablemodel.ConfigModelRepo;
import edu.kit.ipd.creativecrowd.mutablemodel.ExperimentRepo;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAmazonVoucherRepo;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAssignment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableCalibrationAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableCalibrationQuestion;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutablePossibleCalibrationAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRating;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRatingTask;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableStats;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableTaskConstellation;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableWorker;
import edu.kit.ipd.creativecrowd.mutablemodel.WorkerRepo;
import edu.kit.ipd.creativecrowd.operations.CreateExperimentTransaction;
import edu.kit.ipd.creativecrowd.operations.EndExperimentTransaction;
import edu.kit.ipd.creativecrowd.operations.NoValidTasksException;
import edu.kit.ipd.creativecrowd.operations.StrategyNotFoundException;
import edu.kit.ipd.creativecrowd.operations.SubmitAssignmentTransaction;
import edu.kit.ipd.creativecrowd.operations.UpdateTaskConstellationTransaction;
import edu.kit.ipd.creativecrowd.operations.VerifyConfigTransaction;
import edu.kit.ipd.creativecrowd.operations.WorkerPaymentChecker;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.persistentmodel.IDNotFoundException;
import edu.kit.ipd.creativecrowd.persistentmodel.PersistentAmazonVoucherRepo;
import edu.kit.ipd.creativecrowd.persistentmodel.PersistentCalibrationQuestionRepo;
import edu.kit.ipd.creativecrowd.persistentmodel.PersistentConfigModelRepo;
import edu.kit.ipd.creativecrowd.persistentmodel.PersistentExperimentRepo;
import edu.kit.ipd.creativecrowd.persistentmodel.PersistentWorkerRepo;
import edu.kit.ipd.creativecrowd.readablemodel.Assignment;
import edu.kit.ipd.creativecrowd.readablemodel.Button;
import edu.kit.ipd.creativecrowd.readablemodel.Experiment;
import edu.kit.ipd.creativecrowd.readablemodel.PossibleCalibrationAnswer;
import edu.kit.ipd.creativecrowd.readablemodel.Worker;
import edu.kit.ipd.creativecrowd.readablemodel.RatingOption;
import edu.kit.ipd.creativecrowd.readablemodel.CalibrationQuestion;
import edu.kit.ipd.creativecrowd.readablemodel.ConfigModel;
import edu.kit.ipd.creativecrowd.readablemodel.TaskConstellation;

import java.util.List;
import java.util.Map;
import java.util.LinkedList;
/**
 * the Connector class provides an interface for the web layer to interact with the model layer
 * @author simon
 *
 */
public class Connector {

	/**
	 * Create an experiment with the id {@code expid} with the configuration {@code conf}.
	 * If the creation was successful, an empty List will be returned, otherwise the names of all
	 * incorrect parameters will be returned. If an experiment with the given id already exists and
	 * if the configfile is suitable, the list will contain the single item "expid".
	 *
	 * @param conf the configfile
	 * @param expid the id of the experiment
	 * @return the list of all incorrect parameters
	 * @throws ModelException if something in the model went wrong
	 * @author Thomas Friedel
	 */
	public List<String> createExperimentFromConfig(ConfigModel conf, String expid) throws ModelException {
		List<String> result = new LinkedList<String>();
		VerifyConfigTransaction verify = new VerifyConfigTransaction();
		boolean alreadyExists = true;
		try {
			result = verify.run(conf);
			ExperimentRepo repo = new PersistentExperimentRepo();
			repo.loadExperiment(expid);
		} catch (IDNotFoundException e) {
			alreadyExists = false;
			//Thats what we want, carrying on.
		} catch (DatabaseException e) {
			Logger.logException(e.getMessage());
			throw new ModelException(e.getMessage());
		}
		if (alreadyExists) {
			result.add("expid");
		}
		if (result.isEmpty()) {
			CreateExperimentTransaction create = new CreateExperimentTransaction();
			try {
				create.run(new PersistentExperimentRepo(), conf, expid);
			} catch (DatabaseException | ConnectionFailedException e) {
				Logger.logException(e.getMessage());
				throw new ModelException(e.getMessage());
			} catch (StrategyNotFoundException e) {
				Logger.logException(e.getMessage());
				result.add("strategy");
			} catch (IllegalInputException e) {
				Logger.logException(e.getMessage());
				throw new ModelException(e.getMessage());
			}
		}
		return result;
	}

	/**
	 * get an experiment from the database
	 * @param name	the name of the experiment
	 * @return		the readableModel.Experiment object with the requested ID
	 * @throws ModelException if an experiment with the id {@code name} does not exist,
	 * or if the sql request fails.
	 */
	public Experiment getExperiment(String name) throws ModelException {
		try {
			PersistentExperimentRepo repo = new PersistentExperimentRepo();
			Experiment exp = (Experiment) repo.loadExperiment(name);
			return exp;
		} catch (DatabaseException ex) {
			Logger.logException(ex.getMessage());
			throw new ModelException(ex.getMessage());
		}

	}
	/**
	 * gets an updated TaskConstellation
	 * creates a new TaskConstellation if no TaskConstellation for an assignmentId exists so far
	 * @param expId the experiment Id
	 * @param assgId the user's assignment Id
	 * @param b the button last pressed by the user
	 * @throws ModelException if something in the model went wrong
	 * @return a new task constellation
	 */
	public TaskConstellation getNewTaskConstellation(String expId, String assgId, Button b) throws ModelException {
		try {
			PersistentExperimentRepo repo = new PersistentExperimentRepo();
			MutableExperiment ex = repo.loadExperiment(expId);
			MutableAssignment as;
			try{
				as = ex.getAssignment(assgId);
			}catch(DatabaseException e){
				as = ex.addAssignment();
			}


			UpdateTaskConstellationTransaction up = new UpdateTaskConstellationTransaction();
			return up.run(ex, as, b);
		} catch (DatabaseException | NoValidTasksException ex) {
			Logger.logException(ex.getMessage());
			ex.printStackTrace();
			throw new ModelException(ex.getMessage());
		}
	}

	/*-?|Test Repo-Review|Philipp|c5|?*/
	/**
	 * store a given answer in the database
	 * @param expId the Id of the experiment
	 * @param ans	the specification of the Answer
	 * @throws ModelException if something in the model went wrong
	 */
	public void store(String expId, AnswerSpec ans, String assignmentId) throws ModelException {

		try {
			ExperimentRepo exprep = new PersistentExperimentRepo();
			Experiment ex = exprep.loadExperiment(expId);
			MutableTaskConstellation tc = (MutableTaskConstellation) ex.getAssignment(assignmentId).getTaskConstellation();
			int currentind = tc.getCurrentTaskIndex();
			MutableAnswer mans = tc.answerCreativeTaskAt(currentind);
			mans.setText(ans.getText());
		} catch (DatabaseException e) {
			Logger.logException(e.getMessage());
			throw new ModelException(e.getMessage());
		}


	}

	/**
	 * store a given rating in the database
	 * @param expId the Id of the experiment
	 * @param ans	the specification of the rating
	 * @throws ModelException if something in the model went wrong
	 */
	public void store(String expId, RatingSpec ans, String assgId) throws ModelException {

		try {
			//get the experiment
			ExperimentRepo exprep = new PersistentExperimentRepo();
			MutableExperiment ex = exprep.loadExperiment(expId);
			//create a new rating
			MutableTaskConstellation t = (MutableTaskConstellation) ex.getAssignment(assgId).getTaskConstellation();
			MutableRatingTask rt =  ((MutableRatingTask)t.getCurrentTask());
			int currentind = t.getCurrentTaskIndex();
			MutableRating mRat = t.addRatingToRatingTaskAt(currentind);
			/*-?|Test Repo-Review|Philipp|c10|?*/
			//need the answer for adding the rating
			MutableAnswer mans = rt.getAnswerToBeRated(ans.getAnswerID());

			//find the right RatingOption
			RatingOption rOptFin = null;

			for (RatingOption rOpt : ex.getRatingOptions()){
				if(rOpt.getID().equals(ans.getRatingOptionId())){
					rOptFin =  rOpt;
					break;
				}
			}
			//set the right parameters
			mRat.setRatingOption(rOptFin);
			mRat.setText(ans.getText());
			//add rating
			mans.addRating(mRat);
		} catch (DatabaseException e) {
			Logger.logException(e.getMessage());
			e.printStackTrace();
			throw new ModelException(e.getMessage());
		}


	}

	/**
	 * submit an assignment
	 * @param expId the Id of the experiment
	 * @param assgId the Id of the assignment to submit
	 * @throws ModelException if something in the model went wrong
	 */
	public void submit(String expId, String assgId) throws ModelException {
		try {
			ExperimentRepo exprep = new PersistentExperimentRepo();
			MutableExperiment ex = exprep.loadExperiment(expId);
			Assignment as = ex.getAssignment(assgId);
			SubmitAssignmentTransaction sat = new SubmitAssignmentTransaction();
			sat.run((MutableAssignment) as, ex);
		} catch (DatabaseException | ConnectionFailedException | IllegalInputException ex) {
			Logger.logException(ex.getMessage());
			ex.printStackTrace();
			throw new ModelException(ex.getMessage());
		}

	}

	/**
	 * end an experiment(pay the workers, no more money is paid out)
	 * @param expId the Id of the experiment
	 * @throws ModelException if something in the model went wrong
	 */
	public void endExperiment(String expId) throws ModelException {
		try {
			ExperimentRepo exprep = new PersistentExperimentRepo();
			MutableExperiment ex = exprep.loadExperiment(expId);
			EndExperimentTransaction eat = new EndExperimentTransaction();
			try {
				eat.run(ex);
			} catch (ConnectionFailedException | IllegalInputException e) {
				Logger.logException(e.getMessage());
				throw new ModelException(e.getMessage());
			}
		} catch (DatabaseException ex) {
			Logger.logException(ex.getMessage());
			throw new ModelException(ex.getMessage());
		}

	}

	/**
	 * delete an experiment. If the experiment is not yet marked as finished, endExperiment will be called first
	 * @param expId		the Id of the experiment
	 * @throws ModelException if something in the model went wrong
	 */
	public void deleteExperiment(String expId) throws ModelException {
		try {
			ExperimentRepo exprep = new PersistentExperimentRepo();
			MutableExperiment ex = exprep.loadExperiment(expId);
			//workers must still be paid
			if (!ex.isFinished()) {
				endExperiment(expId);
			}
			exprep.deleteExperiment(expId);
		} catch (DatabaseException ex) {
			Logger.logException(ex.getMessage());
			ex.printStackTrace();
			throw new ModelException(ex.getMessage());
		}


	}

	public String findInternalAssignmentId(AssignmentId mtrkid, String expId) throws ModelException {
		try {
			ExperimentRepo exprep = new PersistentExperimentRepo();
			MutableExperiment ex = exprep.loadExperiment(expId);
			Assignment as = ex.getAssignmentWithMturkId(mtrkid);
			if(as != null) {
				return as.getID();
			} else {
				MutableAssignment newAs = ex.addAssignment();
				newAs.setAssignmentID(mtrkid);
				return newAs.getID();
			}
		} catch (DatabaseException ex) {
			Logger.logException(ex.getMessage());
			throw new ModelException(ex.getMessage());
		}
	}

	public void increasePreviewClickCount(String expId) throws ModelException{
		try {
			ExperimentRepo exprep = new PersistentExperimentRepo();
			MutableExperiment ex = exprep.loadExperiment(expId);
			MutableStats st = ex.getStats();
			st.addPreviewClick();
		} catch (DatabaseException ex) {
			Logger.logException(ex.getMessage());
			throw new ModelException(ex.getMessage());
		}
	}

	/**
	 * Assigns the given worker to the assignment
	 * 
	 * @param expId the experiment with the assignment
	 * @param assignmentId the assignment
	 * @param wrkrId
	 * @throws ModelException
	 */
	public void updateWorkerId(String expId,String assignmentId, String wrkrId) throws ModelException {
		try {
			ExperimentRepo exprep = new PersistentExperimentRepo();
			MutableExperiment ex = exprep.loadExperiment(expId);
			MutableAssignment ass = ex.getAssignment(assignmentId);
			
			ass.setWorker(new WorkerId(wrkrId));
			WorkerRepo repo = new PersistentWorkerRepo();
			try {
				repo.loadWorker(wrkrId);
			} catch (DatabaseException e) {
				//Worker seems to be missing. So we create a new one!
				Logger.log("A new Worker enters the house. ID: " + wrkrId);
				MutableWorker wrk = repo.createWorker(wrkrId);
				if (PlatformIdentity.getIdentityFromPrefix(ass.getMTurkAssignmentID()
						.getId()).equals(PlatformIdentity.MTurk)) {
					wrk.setName("MTurk Worker");
					wrk.setPlatform(PlatformIdentity.MTurk);
				}
				//TODO Pybossa?
			}

		} catch (DatabaseException ex) {
			Logger.logException(ex.getMessage());
			throw new ModelException(ex.getMessage());
		}

	}

	/**
	 *
	 * @return	List of all Experiments in the database
	 * @throws ModelException if something went wrong in the model
	 * @author Pascal
	 */
	public List<Experiment> getExperimentList() throws ModelException {
		List<Experiment> ret = new LinkedList<Experiment>();
		try {
			PersistentExperimentRepo exprep = new PersistentExperimentRepo();
			List<MutableExperiment> mutExpList = exprep.loadAllExperiments();
			for (MutableExperiment exp : mutExpList) {
				ret.add(exp);
			}
		} catch (DatabaseException e) {
			Logger.logException(e.getMessage());
			throw new ModelException(e.getMessage());
		}
		return ret;
	}

	/**
	 *
	 * @return A list with all the Workers
	 * @throws ModelException if something went wrong
	 * @author Pascal Gabriel
	 * ROBIN: we now use the email as recognizetool
	 */
	public List<Worker> getWorkerList() throws ModelException {
		List<Worker> result = new LinkedList<Worker>();
		try {
			WorkerRepo wrkrep = new PersistentWorkerRepo();
			for (Worker wrk : wrkrep.loadAllWorkers()) {
				if (!wrk.getEmail().equals(PersistentWorkerRepo.ANONYM_FLAG)) {
					result.add(wrk);
				}
			}
		} catch (DatabaseException e) {
			Logger.logException(e.getMessage());
			throw new ModelException(e.getMessage());
		}
		return result;
	}

	/**
	 *
	 * @return a list with all ConfigModels
	 * @throws ModelException if something went wrong
	 * @author Thomas Friedel
	 */
	public List<ConfigModel> getConfigList() throws ModelException {
		List<ConfigModel> result = new LinkedList<ConfigModel>();
		try {
			ConfigModelRepo confrep = new PersistentConfigModelRepo();
			for (ConfigModel conf : confrep.loadAllConfigModels()) {
				if (!conf.getID().startsWith(PersistentConfigModelRepo.IDPREFIX)) {
					result.add(conf);
				}
			}
		} catch (DatabaseException e) {
			Logger.logException(e.getMessage());
			throw new ModelException(e.getMessage());
		}
		return result;
	}

	/**
	 *
	 * @return a list of all CalibrationQuestions
	 * @throws ModelException if something went wrong
	 * @author Thomas Friedel
	 */
	public List<CalibrationQuestion> getCalibList() throws ModelException {
		List<CalibrationQuestion> ret = new LinkedList<CalibrationQuestion>();
		try {
			CalibrationQuestionRepo calibrep = new PersistentCalibrationQuestionRepo();
			List<MutableCalibrationQuestion> mutCalibList = calibrep.loadAllCalibrationQuestions();
			for (MutableCalibrationQuestion mutCalib : mutCalibList) {
				ret.add(mutCalib);
			}
		} catch (DatabaseException e) {
			Logger.logException(e.getMessage());
			throw new ModelException(e.getMessage());
		}
		return ret;
	}

	/**
	 * Stops the experiment from handing out creative tasks. The worker will only rate already given answers.
	 * 
	 * @param expId the experiments id
	 * @throws ModelException if the experiment doesn't exist or if it is already finished.
	 * @author Thomas Friedel
	 */
	public void stopCreativeTasks(String expId) throws ModelException {
		MutableExperiment exp;
		try {
			PersistentExperimentRepo repo = new PersistentExperimentRepo();
			exp = repo.loadExperiment(expId);
			if (exp.isFinished()) {
				String message = "Experiment '" + expId + "' already finished. Cannot stop creative tasks";
				Logger.logException(message);
				throw new ModelException(message);
			}
			EndExperimentTransaction trans = new EndExperimentTransaction();
			trans.stopCreativeTasks(exp);
		} catch (DatabaseException | ConnectionFailedException | IllegalInputException e) {
			Logger.logException(e.getMessage());
			throw new ModelException(e.getMessage(), e);
		}
	}

	/**
	 * Anonymizes a worker and all tasks he has done
	 * @param workerId the worker 
	 * @throws ModelException if there is no worker with the id in the database
	 * @author Pascal Gabriel
	 */
	public void anonymizeWorker (String workerId) throws ModelException{
		try {
			WorkerRepo repo = new PersistentWorkerRepo();
			repo.anonymizeWorker(workerId);
		} catch (DatabaseException e) {
			Logger.logException(e.getMessage());
			throw new ModelException(e.getMessage());
		}
	}

	/**
	 * Creates a new CalibrationQuestion in the database
	 * @param calib a readable Calibrationquestion
	 * @return the id of the calibrationquestion
	 * @throws ModelException if the id is already taken
	 * @author Pascal Gabriel, Thomas Friedel
	 */
	public String createCalibrationQuestion(CalibrationQuestion calib) throws ModelException{
		try {
			CalibrationQuestionRepo repo = new PersistentCalibrationQuestionRepo();
			DatabaseConnection connection = SQLiteDatabaseConnection.getInstance();
			MutableCalibrationQuestion persistentCalib
				= repo.createCalibrationQuestion(connection.generateID("calibrationquestion"));
			persistentCalib.setQuestion(calib.getQuestion());
			for (PossibleCalibrationAnswer ans : calib.getPossibleAnswers()) {
				MutablePossibleCalibrationAnswer persistentAns
					= persistentCalib.addPossibleAnswer();
				persistentAns.setIsTrue(ans.getIsTrue());
				persistentAns.setText(ans.getText());
				//CalibQuestion is already set
			}
			return persistentCalib.getID();
		} catch(DatabaseException e) {
			Logger.logException(e.getMessage());
			throw new ModelException(e.getMessage());
		}
	}

	/**
	 * Creates a new ConfigModel in the database.
	 *
	 * @param conf a readable ConfigModel
	 * @throws ModelException if the id of the config is already taken
	 * @author Thomas Friedel
	 */
	public void createConfigModel(ConfigModel conf) throws ModelException {
		try { //TODO create ControlQuestions in conf
			if (conf.getID().startsWith(PersistentConfigModelRepo.IDPREFIX)) {
				throw new ModelException("Invalid id: the prefix '" + PersistentConfigModelRepo.IDPREFIX
						+ "' is reserved.");
			}
			ConfigModelRepo repo = new PersistentConfigModelRepo();
			repo.createConfigModel(conf, conf.getID(), null);
		} catch (DatabaseException e) {
			Logger.logException(e.getMessage());
			throw new ModelException(e.getMessage());
		}
	}

	/**
	 *
	 * @param id the id of the CalibrationQuestion
	 * @return the CalibrationQuestion with the id {@code id}
	 * @throws ModelException if there is no CalibrationQuestion with the id {@code id} in the database
	 * @author Thomas Friedel
	 */
	public CalibrationQuestion getCalibrationQuestion(String id) throws ModelException {
		try {
			CalibrationQuestionRepo calibrep = new PersistentCalibrationQuestionRepo();
			return calibrep.loadCalibrationQuestion(id);
		} catch (DatabaseException e) {
			Logger.logException(e.getMessage());
			throw new ModelException(e.getMessage());
		}

	}

	/**
	 *
	 * @param id the id or name of the config
	 * @return the config with the id {@code id}
	 * @throws ModelException if there is no ConfigModel with the id {@code id} in the database
	 */
	public ConfigModel getConfigModel(String id) throws ModelException {
		try {
			ConfigModelRepo confrep = new PersistentConfigModelRepo();
			return confrep.loadConfigModel(id);
		} catch (DatabaseException e) {
			Logger.logException(e.getMessage());
			throw new ModelException(e.getMessage());
		}
	}

	/**
	 * returns a worker specified by the id
	 * @param id the id of the worker
	 * @return the worker
	 * @throws ModelException if there is no worker with the id in the database
	 * @author Pascal Gabriel
	 */
	public Worker getWorker(String id) throws ModelException {
		try {
			PersistentWorkerRepo workrep = new PersistentWorkerRepo();
			return workrep.loadWorker(id);
		} catch (DatabaseException e) {
			Logger.logException(e.getMessage());
			throw new ModelException(e.getMessage());
		}
	}

	public void deleteConfigModel(String id) throws ModelException {
		if (id.startsWith(PersistentConfigModelRepo.IDPREFIX)) {
			String message = "This config is used by an experiment; cannot delete";
			Logger.logException(message);
			throw new ModelException(message);
		}
		try {
			ConfigModelRepo repo = new PersistentConfigModelRepo();
			repo.deleteConfigModel(id);
		} catch (DatabaseException e) {
			Logger.logException(e.getMessage());
			throw new ModelException(e.getMessage());
		}
	}

	/**
	 * deletes a calibrationquestion from the database
	 * @param id the id of the calibrationquestion
	 * @throws ModelException if there is no calibrationquestion with the id in the database
	 * @author Pascal Gabriel
	 */
	public void deleteCalibrationQuestion(String id) throws ModelException{
		try {
			CalibrationQuestionRepo repo = new PersistentCalibrationQuestionRepo();
			repo.deleteCalibrationQuestion(id);
		} catch (DatabaseException e){
			Logger.logException(e.getMessage());
			throw new ModelException(e.getMessage());
		}
	}

	public boolean saveCalibanswers(CalibrationAnswerJson calans, String workerid, String experimentid, String assignmentId) throws DatabaseException {
		boolean val = true;
		try {
			PersistentExperimentRepo erepo = new PersistentExperimentRepo();
			MutableExperiment exp = erepo.loadExperiment(experimentid);
			
			MutableTaskConstellation tc = exp.getAssignment(assignmentId).getTaskConstellation();
			
			
			
			MutableCalibrationAnswer mca;
			Map<String, String> map = calans.getAnswers();
			for (Map.Entry<String, String> entry : map.entrySet()) {
				mca = tc.answerCalibrationQuestionAt(entry.getKey());
				 mca.setAnswer(entry.getValue());
				 if (!mca.isCorrect())
					 val = false;
			}
		}  catch (DatabaseException e) {
			throw new DatabaseException("Error in saving calibanswer for worker in connector!");
		}
		return val;
	}
	
	/**
	 * Amends an running experiment. If the returning list is empty, then the amendment was successful
	 * 
	 * @see CreateExperimentTransaction#amendExperiment(ExperimentRepo, ConfigModel, String)
	 * 
	 * @param expid the experiment to be amended
	 * @param config the new configuration for the experiment
	 * @return a list of incorrect parameters
	 * @throws ModelException if something in the model went wrong
	 * @author Thomas Friedel
	 */
	public List<String> amendExperiment(String expid, ConfigModel config) throws ModelException {
		List<String> result = new LinkedList<String>();
		VerifyConfigTransaction verify = new VerifyConfigTransaction();
		try {			
			ExperimentRepo repo = new PersistentExperimentRepo();
			Experiment experiment = repo.loadExperiment(expid);
			if (experiment.isFinished()) {
				result.add("expid");
				return result;
			}
			result = verify.compare(config, experiment.getConfig());
		} catch (IDNotFoundException e) {
			result.add("expid");
			return result;
		} catch (DatabaseException e) {
			Logger.logException(e.getMessage());
			throw new ModelException(e.getMessage());
		}
		if (result.isEmpty()) {
			CreateExperimentTransaction create = new CreateExperimentTransaction();
			try {
				create.amendExperiment(new PersistentExperimentRepo(), config, expid);
			} catch (ModelException e) {
				//Only budget can cause this now, everything else is checked.
				Logger.logException(e.getMessage());
				result.add("budget");
			} catch (DatabaseException | ConnectionFailedException e) {
				Logger.logException(e.getMessage());
				throw new ModelException(e.getMessage());
			} catch (StrategyNotFoundException e) {
				Logger.logException(e.getMessage());
				result.add("strategy");
			} catch (IllegalInputException e) {
				Logger.logException(e.getMessage());
				throw new ModelException(e.getMessage());
			}
		}
		return result;
	}
	
	/**
	 * Pays a single worker.
	 * 
	 * @param wrkid the id of the worker
	 * @return {@code true} if the worker could be payed, {@code false} if not.
	 * @throws ModelException if there is no worker with that id
	 */
	public boolean payWorker(String wrkid) throws ModelException {
		try {
			WorkerPaymentChecker payer = new WorkerPaymentChecker();
			return payer.payWorker(wrkid);
		} catch (DatabaseException e) {
			Logger.logException(e.getMessage());
			throw new ModelException(e.getMessage());
		}
	}
	
	/**
	 * Pays all workers.
	 * 
	 * @return {@code true} if all worker could be payed, {@code false} if not.
	 * @throws ModelException if something went wrong in the model
	 */
	public boolean payAllWorkers() throws ModelException {
		try {
			WorkerPaymentChecker payer = new WorkerPaymentChecker();
			return payer.payAllWorkers();
		} catch (DatabaseException e) {
			Logger.logException(e.getMessage());
			throw new ModelException(e.getMessage());
		}
	}
	
	/**
	 * Blocks or unblocks the worker from submitting assignments to all experiments.
	 * 
	 * @param wrkid the id of the worker
	 * @param block {@code true} blocks the worker, {@code false} unblocks him.
	 * @throws ModelException if there is no worker with the id {@code wrkid}.
	 */
	public void blockWorker(String wrkid, boolean block) throws ModelException {		
		try {
			WorkerRepo repo = new PersistentWorkerRepo();
			MutableWorker wrk = repo.loadWorker(wrkid);
			wrk.setBlocked(block);
		} catch (DatabaseException e) {
			Logger.logException(e.getMessage());
			throw new ModelException(e.getMessage());
		}
	}
	
	/**
	 * Adds amazon vouchers to the database given by a string
	 * 
	 * @param input a subsequent list of 'vouchercode:vouchervalue(in cent);'
	 * @return the number of successfully added vouchers
	 * @throws ModelException if something in the database failed.
	 */
	public int addAmazonVouchers(String input) throws ModelException {
		try {
			MutableAmazonVoucherRepo repo = new PersistentAmazonVoucherRepo();
			int before = repo.getAllVouchers().size();
			repo.addVouchersAsString(input);
			return repo.getAllVouchers().size() - before;
		} catch (DatabaseException e) {
			Logger.logException(e.getMessage());
			throw new ModelException(e.getMessage());
		}
	}
}
package edu.kit.ipd.creativecrowd.connector;


import edu.kit.ipd.creativecrowd.mturk.AssignmentId;
import edu.kit.ipd.creativecrowd.mturk.ConnectionFailedException;
import edu.kit.ipd.creativecrowd.mturk.IllegalInputException;
import edu.kit.ipd.creativecrowd.mturk.WorkerId;
import edu.kit.ipd.creativecrowd.mutablemodel.ExperimentRepo;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAssignment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableCreativeTask;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRating;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRatingOption;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRatingTask;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableStats;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableTaskConstellation;
import edu.kit.ipd.creativecrowd.operations.CreateExperimentTransaction;
import edu.kit.ipd.creativecrowd.operations.EndExperimentTransaction;
import edu.kit.ipd.creativecrowd.operations.NoValidTasksException;
import edu.kit.ipd.creativecrowd.operations.StrategyNotFoundException;
import edu.kit.ipd.creativecrowd.operations.SubmitAssignmentTransaction;
import edu.kit.ipd.creativecrowd.operations.UpdateTaskConstellationTransaction;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.persistentmodel.PersistentExperimentRepo;
import edu.kit.ipd.creativecrowd.readablemodel.Assignment;
import edu.kit.ipd.creativecrowd.readablemodel.Button;
import edu.kit.ipd.creativecrowd.readablemodel.Experiment;
import edu.kit.ipd.creativecrowd.readablemodel.ExperimentSpec;
import edu.kit.ipd.creativecrowd.readablemodel.RatingOption;
import edu.kit.ipd.creativecrowd.readablemodel.RatingTask;
import edu.kit.ipd.creativecrowd.readablemodel.TaskConstellation;
import edu.kit.ipd.creativecrowd.util.Logger;
/**
 * the Connector class provides an interface for the web layer to interact with the model layer
 * @author simon
 *
 */
public class Connector implements IModel {

	/**
	 * create an experiment according to the options specified in the ExperimentSpec
	 * @param specs		the specifications of the experiment to be created
	 * @throws ModelException if something in the model went wrong
	 */
	public void createExperimentFromSpecs(ExperimentSpec specs) throws ModelException {
		try {
			PersistentExperimentRepo repo = new PersistentExperimentRepo();
			CreateExperimentTransaction cr = new CreateExperimentTransaction();
			cr.run(repo, specs);
		} catch (StrategyNotFoundException | DatabaseException | ConnectionFailedException | IllegalInputException e) {
			Logger.logException(e.getMessage());
			e.printStackTrace();
			throw new ModelException(e.getMessage());
			
		}
	}
	
	/**
	 * get an experiment from the database
	 * @param name	the name of the experiment
	 * @return		the readableModel.Experiment object with the requested ID
	 * @throws ModelException if something in the model went wrong
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
	public void store(String expId, AnswerSpec ans,String assignmentId) throws ModelException {
		
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
			//get the answer
			MutableCreativeTask ct = (MutableCreativeTask) ex.getCreativeTask();
			//create a new rating
			MutableTaskConstellation t = (MutableTaskConstellation) ex.getAssignment(assgId).getTaskConstellation();
			MutableRatingTask rt =  ((MutableRatingTask)t.getCurrentTask());
			int currentind = t.getCurrentTaskIndex();
			MutableRating mRat = t.addRatingToRatingTaskAt(currentind);
			/*-?|Test Repo-Review|Philipp|c10|?*/
			//need the answer for adding the rating
			MutableAnswer mans = rt.getAnswerToBeRated(ans.getAnswerID());	
			
			//find the right RatingOption
			MutableRatingOption rOptFin = null;
			
			for (MutableRatingOption rOpt : ex.getRatingOptions()){
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

	@Override
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

	public void updateWorkerId(String expId,String assignmentId, String wrkrId)throws ModelException {
		try {
			ExperimentRepo exprep = new PersistentExperimentRepo();
			MutableExperiment ex = exprep.loadExperiment(expId);
			MutableAssignment ass = ex.getAssignment(assignmentId);
			ass.setWorker(new WorkerId(wrkrId));
			
		} catch (DatabaseException ex) {
			Logger.logException(ex.getMessage());
			throw new ModelException(ex.getMessage());
		}
		
	}

}

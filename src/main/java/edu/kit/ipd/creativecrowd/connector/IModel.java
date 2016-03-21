package edu.kit.ipd.creativecrowd.connector;

import edu.kit.ipd.creativecrowd.mturk.AssignmentId;
import edu.kit.ipd.creativecrowd.readablemodel.Button;
import edu.kit.ipd.creativecrowd.readablemodel.Experiment;
import edu.kit.ipd.creativecrowd.readablemodel.ExperimentSpec;
import edu.kit.ipd.creativecrowd.readablemodel.TaskConstellation;

/**
 * the interface of the Model layer
 * @author simon
 *
 */
public interface IModel {
	/**
	 * submit an assignment
	 * @param expId the Id of the experiment
	 * @param assgId the Id of the assignment to submit
	 * @throws ModelException if something in the model went wrong
	 */
	void submit(String expId, String assgId) throws ModelException;
	/**
	 * create a new TaskConstellation for a user working on the assignment specified by assgId
	 * @param expId the experiment Id
	 * @param assgId the user's assignment Id
	 * @param b the button last pressed by the user
	 * @throws ModelException if something in the model went wrong
	 * @return a new task constellation
	 */
	TaskConstellation getNewTaskConstellation(String expId, String assgId, Button b) throws ModelException;
	/**
	 * store a given rating in the database
	 * @param expId the Id of the experiment
	 * @param ratSp	the specification of the rating
	 * @throws ModelException if something in the model went wrong
	 */
	void store(String expId, RatingSpec ratSp, String assgId) throws ModelException;
	/**
	 * store a given answer in the database
	 * @param expId the Id of the experiment
	 * @param ansSp	the specification of the Answer
	 * @throws ModelException if something in the model went wrong
	 */
	void store(String expId, AnswerSpec ansSp,String assgId) throws ModelException;
	/**
	 * create an experiment according to the specifications given by the requester
	 * @param exSp		the specifications of the experiment to be created
	 * @throws ModelException if something in the model went wrong
	 */
	void  createExperimentFromSpecs(ExperimentSpec exSp) throws ModelException;
	/**
	 * end an experiment(pay the workers, no more money is paid out)
	 * @param expId the Id of the experiment
	 * @throws ModelException if something in the model went wrong
	 */
	void endExperiment(String expId) throws ModelException;
	/**
	 * get an experiment from the database
	 * @param expId	the name of the experiment
	 * @return		a readableModel.Experiment object with the requested ID
	 * @throws ModelException if something in the model went wrong
	 */
	Experiment getExperiment(String expId) throws ModelException;
	
	public String findInternalAssignmentId(AssignmentId mtrkid, String expId) throws ModelException;

	/**
	 * sets the workerId of given assignment in given experiment
	 * @param expId experiment where the assignment is located
	 * @param assignmentId id of assignment, which belongs to worker
	 * @param workerIdToSet workerId to set
	 * @throws ModelException
	 */
	/*-?|Anika|Philipp|c0|?*/
	public void updateWorkerId(String expId, String assignmentId, String workerIdToSet) throws ModelException;
}

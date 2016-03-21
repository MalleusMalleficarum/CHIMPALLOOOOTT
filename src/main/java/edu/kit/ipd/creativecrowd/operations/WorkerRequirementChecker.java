package edu.kit.ipd.creativecrowd.operations;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import edu.kit.ipd.creativecrowd.crowdplatform.PlatformIdentity;
import edu.kit.ipd.creativecrowd.database.DatabaseConnection;
import edu.kit.ipd.creativecrowd.database.Value;
import edu.kit.ipd.creativecrowd.mutablemodel.ConfigModelRepo;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRating;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.persistentmodel.PersistentAnswer;
import edu.kit.ipd.creativecrowd.persistentmodel.PersistentConfigModelRepo;
import edu.kit.ipd.creativecrowd.persistentmodel.PersistentWorker;
import edu.kit.ipd.creativecrowd.readablemodel.ConfigModel;
import edu.kit.ipd.creativecrowd.readablemodel.Worker;
/**
 * Provides methods to check if a worker fulfills set qualifications.
 * @author Pascal Gabriel
 *
 */
public class WorkerRequirementChecker {//TODO actually use this

	private DatabaseConnection connection;
	
	/**
	 * Constructs a new WorkerRequirementChecker with the given database connection.
	 * @param dbConnection the database connection
	 */
	public WorkerRequirementChecker(DatabaseConnection dbConnection) {
		this.connection = dbConnection;
	}


	/**
	 * Returns a list of all qualified workers for the qualifications set in the config model.
	 * @param configId the config model id
	 * @return list of workers
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public List<Worker> getQualifiedWorkerList(String configId) throws DatabaseException{
		List<String> allSql = new LinkedList<String>();
		ConfigModelRepo repo = new PersistentConfigModelRepo();
		ConfigModel config = repo.loadConfigModel(configId);
		Iterator<String> it = config.getQualifications(PlatformIdentity.PyBossa).iterator();
		while (it.hasNext()) {
			allSql.add(it.next());
		}
		return getQualifiedWorkerListAllSQL(allSql);
	}
	
	/**
	 * Returns a list of all qualified workers for the given set of partial sql statements
	 * @param allSql partial sql queries
	 * @return list of workers
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public List<Worker> getQualifiedWorkerListAllSQL(List<String> allSql) throws DatabaseException{
		//first get the lists for each sql statement
		Iterator<String> sqlIterator = allSql.iterator();
		List<List<Worker>> allWorkerLists = new LinkedList<List<Worker>>();
		while (sqlIterator.hasNext()) {
			allWorkerLists.add(getQualifiedWorkerListSQL(sqlIterator.next()));
		}
		//then get the intersection of the lists
		return getIntersection(allWorkerLists);
	}
	
	/**
	 * Gets all workers that are qualified by the "SELECT workerid FROM " + partialSql sql query.
	 * @param partialSql the partial sql query
	 * @return a list of workers 
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public List<Worker> getQualifiedWorkerListSQL(String partialSql) throws DatabaseException{
		try {
			List<Worker> result = new LinkedList<Worker>();
			String sql = "SELECT workerid FROM " + partialSql;
			Iterator<Iterable<Value>> qualifiedWorkers = connection.query(sql).iterator();
			while (qualifiedWorkers.hasNext()) {
				String currentWorkerId = qualifiedWorkers.next().iterator().next().asString();
				Worker currentWorker = new PersistentWorker(this.connection, currentWorkerId);
				result.add(currentWorker);
			}
			return result;
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}
	
	/**
	 * Checks if a worker is qualified by the qualifications specified in a config model.
	 * @param configId the config model id
	 * @param workerId the worker id
	 * @return {@code true} if the worker fulfills it, {@code false} otherwise
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public boolean checkWorkerQualification(String configId, String workerId) throws DatabaseException{
		Worker worker = new PersistentWorker(this.connection, workerId);
		return isPartOf(worker, getQualifiedWorkerList(configId));
	}
	
	/**
	 * Checks if a worker is qualified by the given partial sql statements.
	 * @param allSql the sql statements
	 * @param workerId the worker id
	 * @return {@code true} if the worker fulfills it, {@code false} otherwise
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public boolean checkWorkerQualificationAllSQL(List<String> allSql, String workerId) throws DatabaseException{
		Worker worker = new PersistentWorker(this.connection, workerId);
		return isPartOf(worker, getQualifiedWorkerListAllSQL(allSql));
	}
	
	/**
	 * Checks if a worker fits the given partial sql statement
	 * @param sql sql statement, "SELECT workerid FROM " will be but at the start of it 
	 * @param workerId the id of the worker
	 * @return {@code true} if the worker fulfills it, {@code false} otherwise
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public boolean checkWorkerQualificationSQL(String partialSql, String workerId) throws DatabaseException{
		Worker worker = new PersistentWorker(this.connection, workerId);
		return isPartOf(worker, getQualifiedWorkerListSQL(partialSql));
	}
	
	/**
	 * Checks whether a worker has an average Rating >= the threshold specified in the config model with the id configId
	 * @param configId the id of the configmodel
	 * @param workerId the id of the worker
	 * @return {@code true} if the workers rating is >=, {@code false} otherwise
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public boolean checkWorkerRating(String configId, String workerId) throws DatabaseException{
		float ratingThreshold;
		try {
			String sql = connection.formatString("SELECT averageratingthreshold FROM configmodel WHERE id = {?};", Value.fromString(configId));
			Iterable<Iterable<Value>> values = connection.query(sql);
			ratingThreshold = values.iterator().next().iterator().next().asFloat();
			return checkWorkerRatingFloat(ratingThreshold, workerId);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}
	
	/**
	 * Checks whether a worker has an average Rating >= averageRating.
	 * @param averageRating the rating to check for
	 * @param workerId the worker to check on
	 * @return {@code true} if the workers rating is >=, {@code false} otherwise
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public boolean checkWorkerRatingFloat(float averageRating, String workerId) throws DatabaseException{
		float workerAverageRating = getAverageRatingWorker(workerId);
		if(averageRating <= workerAverageRating) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Checks wether the total count of creative and rating tasks of a worker is > the margin specified in 
	 * the config model with the id configId
	 * @param configId the id of the configmodel
	 * @param workerId the id of the worker
	 * @return {@code true} if the worker has more tasks, {@code false} otherwise
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public boolean checkWorkerTaskCount(String configId, String workerId) throws DatabaseException{
		int countThreshold;
		try {
			String sql = connection.formatString("SELECT totaltaskcountthreshold FROM configmodel WHERE id = {?};", Value.fromString(configId));
			Iterable<Iterable<Value>> values = connection.query(sql);
			countThreshold = values.iterator().next().iterator().next().asInt();
			return checkWorkerTaskCountInt(countThreshold, workerId);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}
	
	/**
	 * Checks whether the total count of creative and rating tasks of a worker is >= totalCount
	 * @param totalCount the count on which to check
	 * @param workerId the worker for which to check
	 * @return {@code true} if the worker has more tasks, {@code false} otherwise
	 * @throws DatabaseException if the SQL request fails (e.g. wrong SQL syntax or the column/table does not exist).
	 */
	public boolean checkWorkerTaskCountInt(int totalCount, String workerId) throws DatabaseException{
		try {
			String sql = connection.formatString("SELECT * FROM rating WHERE workerid = {?};", Value.fromString(workerId));
			Iterator<Iterable<Value>> ratings = connection.query(sql).iterator();
			int total = 0;
			while(ratings.hasNext()) {
				ratings.next();
				total++;
			}
			sql = connection.formatString("SELECT * FROM answer WHERE workerid = {?};", Value.fromString(workerId));
			Iterator<Iterable<Value>> creatives = connection.query(sql).iterator();
			while(creatives.hasNext()) {
				creatives.next();
				total++;
			}
			if (totalCount <= total) {
				return true;
			} else {
				return false;
			}
			
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}
	
	/*
	 * Returns the average Rating for all answers given by the worker.
	 */
	private float getAverageRatingWorker(String workerId) throws DatabaseException{
		try {
			String sql = connection.formatString("SELECT id FROM answer WHERE workerid = {?};", Value.fromString(workerId));
			Iterator<Iterable<Value>> answers = connection.query(sql).iterator();
			List<MutableAnswer> answerList = new LinkedList<MutableAnswer>();
			while (answers.hasNext()) {
				String currentAnswerId = answers.next().iterator().next().asString();
				answerList.add(new PersistentAnswer(currentAnswerId, this.connection));
			}
			Iterator<MutableAnswer> answerIterator = answerList.iterator();
			float totalRating = 0;
			int ratingCount = 0;
			while (answerIterator.hasNext()) {
				totalRating = totalRating + getAverageRating(answerIterator.next());
				ratingCount++;
			}
			return totalRating / ratingCount;
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage());
		}
	}
	
	/*
	 * Returns the average Rating for a given answer
	 */
	private float getAverageRating(MutableAnswer answer) throws DatabaseException{
		float sumOfWeights = 0;
		float numericRatings = 0;
		for (MutableRating rat : answer.getRatings()) {
			sumOfWeights += rat.getFinalQualityIndex();
			numericRatings += rat.getSelectedRatingOption().getValue() * rat.getFinalQualityIndex();
		}
		float qualityIndex;
		if (sumOfWeights == 0) {
			qualityIndex = 0;
		}
		else {
			qualityIndex = numericRatings / sumOfWeights;
		}		
		return qualityIndex;
	}
	
	/*
	 * Returns the intersection of all worker lists.
	 */
	private List<Worker> getIntersection (List<List<Worker>> workerLists) throws DatabaseException{
		if (workerLists.size() == 1) {
			return workerLists.get(0);
		} else if (workerLists.size() > 2) {
			//getIntersection of first to lists and then add it back
			List<List<Worker>> ret = workerLists.subList(0,2);
			List<List<Worker>> result = workerLists.subList(2, workerLists.size());
			result.add(0, getIntersection(ret));
			return getIntersection(result);
		} else if (workerLists.size() == 2) {
			List<Worker> firstList = workerLists.get(0);
			List<Worker> secondList = workerLists.get(1);
			Iterator<Worker> firstListIterator = firstList.iterator();
			List<Worker> returnList = new LinkedList<Worker>();
			while (firstListIterator.hasNext()) {
				Worker currentWorker = firstListIterator.next();
				if (isPartOf(currentWorker, secondList)) {
					returnList.add(currentWorker);
				}
			}
			return returnList;
		}
		return new LinkedList<Worker>();
	}
	
	/*
	 * Checks if a worker is contained in a list of workers.
	 */
	private boolean isPartOf (Worker worker, List<Worker> workerList) throws DatabaseException{
		Iterator<Worker> listIterator = workerList.iterator();
		while (listIterator.hasNext()) {
			if (worker.getID().equals(listIterator.next().getID())) {
				return true;
			}
		}
		return false;
	}
}

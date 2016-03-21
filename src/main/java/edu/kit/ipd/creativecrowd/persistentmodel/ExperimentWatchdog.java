package edu.kit.ipd.creativecrowd.persistentmodel;



import java.util.HashMap;

import edu.kit.ipd.chimpalot.util.Alert;
import edu.kit.ipd.chimpalot.util.Logger;


/**
 * This Class is meant to monitor an experiment throughout its existence.
 * 
 * @author Robin
 *
 */
public class ExperimentWatchdog extends Thread {
	
	/**
	 * Used to receive the watchdog of an experiment.
	 */
	private static HashMap<String, ExperimentWatchdog> watchdogs = new HashMap<String, ExperimentWatchdog>();
	

	private String expId;	
	private int minimumSubmissions;
	private int submissionCountResetting;
	private int submissionCountPermanent;
	private int submissionCountDuplicate;
	private int firstThreshold;
	private double secondThreshold;
	private int checkRate;
	
	/**
	 * Constructs the watchdog, the watchdog needs to know which experiment it is monitoring
	 * in order to terminate correctly.
	 * @param ex the experiment.
	 */
	public ExperimentWatchdog(String experimentID) {
		this.expId = experimentID;
		this.secondThreshold = 0.2;
		this.firstThreshold = 5;
		this.minimumSubmissions = 1;
		this.checkRate = 1000 * 60 * 60 * 4; // 4 Hours
		
		watchdogs.put(experimentID, this);		
	}
	/**
	 * this method checks regularly whether the experiment has had submissions 
	 * and if they contain duplicates
	 */
	public void run(){
		if(expId == null) {
			return;
		}
		PersistentExperimentRepo repo = null;
		try {
			repo = new PersistentExperimentRepo();
		} catch (DatabaseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			Logger.error("WATCHDOG for: "+expId +" failed.");
			return;
		}
		try {
			
			while(!repo.loadExperiment(expId).isFinished()) {
				Thread.sleep(checkRate);
				checkForSubmissions();
				checkForDuplicates();
			}
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		watchdogs.remove(expId);
	}
	/**
	 * This checks whether a sufficient amount of submissions has been submitted
	 * in the specified time interval, if not an alert is sent.
	 * @throws DatabaseException if the experiment is unacessible
	 */
	private void checkForSubmissions() throws DatabaseException {
		if(expId == null) {
			return;
		}
		PersistentExperimentRepo repo = new PersistentExperimentRepo();
		if(repo.loadExperiment(expId).isFinished()) {
			return;
		}
		if(submissionCountResetting < minimumSubmissions) {
			Alert.notifyForNoTasks(expId);
			// In order to prevent alert spam the amount of submissions needed is reduced.
			minimumSubmissions = minimumSubmissions / 2;
			if (minimumSubmissions == 0) {
				minimumSubmissions = 1;
				checkRate *= 2;
			}
		}
		submissionCountResetting = 0;
	}
	/**
	 * Increases the count of submissions. (This variable will be reset after each check)
	 * @return the value it was increased to
	 */
	public int increaseSubmitted() {
		submissionCountResetting += 1;
		return submissionCountResetting;
	}
	/**
	 * This method checks whether there have been notably many duplicates. if so a n alert is sent
	 * @throws DatabaseException if the experiment is unaccessible
	 */
	private void checkForDuplicates() throws DatabaseException  {
		if(expId == null) {
			return;
		}
		PersistentExperimentRepo repo = new PersistentExperimentRepo();
		if(repo.loadExperiment(expId).isFinished()) {
			return;
		}
		if((submissionCountPermanent > firstThreshold) && 
				(double) submissionCountDuplicate / (double) submissionCountPermanent > secondThreshold) {
			Alert.notifyForDuplicates(expId);
			// Again to prevent unnecessary email flooding  the limits are increased .
			firstThreshold *= 2;
			secondThreshold += 0.05;
		}
	}
	/**
	 * increases the count of submissions. (This will not be reset)
	 * @return the value it was increased to
	 */
	public int increasePermanent() {
		submissionCountPermanent += 1;
		return submissionCountPermanent;
	}
	/**
	 * increases the count of submissions which are duplicate (This will not be reset)
	 * @return the value it was increased to
	 */
	public int increaseDuplicate() {
		submissionCountDuplicate += 1;
		return submissionCountDuplicate;
	}
	
	/**
	 * Returns the watchdog of the given experiment. If an experiment with such an id does not exist, or if for some reason
	 * that experiment does not have watchdog, this returns {@code null}.
	 * 
	 * @param expid the id of the experiment
	 * @return the watchdog
	 */
	public static ExperimentWatchdog getWatchdog(String expid) {
		return watchdogs.get(expid);
	}


}

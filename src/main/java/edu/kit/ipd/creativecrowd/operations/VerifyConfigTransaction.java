package edu.kit.ipd.creativecrowd.operations;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.ConfigModel;
import edu.kit.ipd.creativecrowd.readablemodel.ExperimentType;
import edu.kit.ipd.creativecrowd.crowdplatform.PlatformIdentity;
import edu.kit.ipd.creativecrowd.readablemodel.TypeOfTask;

/**
 * This transaction checks if a ConfigModel is suitable for an experiment.
 * 
 * @author Thomas Friedel & Pascal Gabriel
 */
public class VerifyConfigTransaction extends Transaction {
	
	/**
	 * Creates a list of all invalid parameters, if there are any.
	 * An empty list signals that this ConfigModel is suitable for an experiment.
	 * 
	 * @param config the ConfigModel to verify
	 * @return a list of invalid parameters, is empty if ConfigModel is suitable
	 * @throws DatabaseException if this fails to read from the database
	 */
	public List<String> run(ConfigModel config) throws DatabaseException {
		Set<String> result = new HashSet<String>();
		
		if (config.getTaskQuestion() == null || config.getTaskQuestion().length() == 0) {
			result.add("taskQuestion");
		}
		
		if (config.getRatingTaskQuestion() == null || config.getRatingTaskQuestion().length() == 0) {
			result.add("ratingTaskQuestion");
		}
		
		if (config.getTaskDescription() == null || config.getTaskDescription().length() == 0) {
			result.add("taskDescription");
		}
		
		if (config.getTaskTitle() == null || config.getTaskTitle().isEmpty()) {
			result.add("taskTitle");
		}
		
		if (config.getTaskTags() == null) {
			result.add("taskTags");
		}
		
		if (config.getTaskSourceURL() != null && !config.getTaskSourceURL().isEmpty() &&
				(config.getPictureURL() == null || config.getPictureURL().isEmpty())) {
			result.add("pictureURL");
		} else {
			if (config.getPictureURL() != null && !config.getPictureURL().isEmpty()) {
				try {
					new URL(config.getPictureURL());
				} catch (MalformedURLException e) {
					result.add("pictureURL");
				}
			}
		}
		
		if (config.getPictureURL() != null && !config.getPictureURL().isEmpty() &&
				(config.getTaskSourceURL() == null || config.getTaskSourceURL().isEmpty())) {
			result.add("taskSourceURL");
		} else {
			if (config.getTaskSourceURL() != null && !config.getTaskSourceURL().isEmpty()) {
				try {
					new URL(config.getTaskSourceURL());
				} catch (MalformedURLException e) {
					result.add("taskSourceURL");
				}
			}
		}
		
		if (config.getCalibQuestions() == null) {
			result.add("calibQuestions");
		}
		
		if (config.getControlQuestions() == null) {
			result.add("controlQuestions");
		}
		
		if (config.getMaxCreativeTask() <= 0) {
			result.add("maxCreativeTask");
		}
		
		if (config.getMaxRatingTask() <= 0) {
			result.add("maxRatingTask");
		}
		
		if (config.getBudget() <= 0) {
			result.add("budget");
		}
		
		if (!config.getSendCreativeTo(PlatformIdentity.MTurk) && !config.getSendCreativeTo(PlatformIdentity.PyBossa)) {
			result.add("sendCreativeToMTurk");
			result.add("sendCreativeToPyBossa");
		}
		
		if (!config.getSendRatingTo(PlatformIdentity.MTurk) && !config.getSendRatingTo(PlatformIdentity.PyBossa)) {
			result.add("sendRatingToMTurk");
			result.add("sendRatingToPyBossa");
		}
		
		if (config.getBasicPayment(PlatformIdentity.MTurk) <= 0
				&& (config.getSendCreativeTo(PlatformIdentity.MTurk) || config.getSendRatingTo(PlatformIdentity.MTurk))) {
			result.add("basicPaymentMTurk");
		} else if (config.getBasicPayment(PlatformIdentity.MTurk) > config.getBudget()) {
			result.add("basicPaymentMTurk");
		}
				
		if (config.getBasicPayment(PlatformIdentity.PyBossa) > config.getBudget()) {
			result.add("basicPaymentPyBossa");
		}
		
		if (config.getPaymentPerTask(PlatformIdentity.MTurk, TypeOfTask.Creative) <= 0 && config.getSendCreativeTo(PlatformIdentity.MTurk)) {
			result.add("paymentPerTaskCrMTurk");
		} else if (config.getPaymentPerTask(PlatformIdentity.MTurk, TypeOfTask.Creative) > config.getBudget()) {
			result.add("paymentPerTaskCrMTurk");
		}
		
		if (config.getPaymentPerTask(PlatformIdentity.MTurk, TypeOfTask.Rating) <= 0 && config.getSendRatingTo(PlatformIdentity.MTurk)) {
			result.add("paymentPerTaskRaMTurk");
		} else if (config.getPaymentPerTask(PlatformIdentity.MTurk, TypeOfTask.Rating) > config.getBudget()) {
			result.add("paymentPerTaskCrMTurk");
		}
		
		if (config.getPaymentPerTask(PlatformIdentity.PyBossa, TypeOfTask.Creative) > config.getBudget()) {
			result.add("paymentPerTaskCrPyBossa");
		}
		
		if (config.getPaymentPerTask(PlatformIdentity.PyBossa, TypeOfTask.Rating) > config.getBudget()) {
			result.add("paymentPerTaskRaPyBossa");
		}

		if (config.getEvaluationType() == null) {
			result.add("evaluationType");
		} else {
			String temp = config.getEvaluationType();
			if (!temp.equals("edu.kit.ipd.creativecrowd.view.ThumbRatingView")
					&& !temp.equals("edu.kit.ipd.creativecrowd.view.StarRatingView")
					&& !temp.equals("edu.kit.ipd.creativecrowd.view.GenericRatingView")) {
				result.add("evaluationType");
			}
		}
		
		if (config.getExperimentType() == null || config.getExperimentType().equals(ExperimentType.Undefined)) {
			result.add("experimentType");
		}
		
		if (config.getBlockedWorkers() == null) {
			result.add("blockedWorkers");
		}
				
		return new LinkedList<String>(result);
	}
	
	/**
	 * Compares two ConfigModels and checks if {@code amendment} can replace {@code oldConf}, without disrupting a running
	 * experiment which uses {@code oldConf}. If {@code amendment} would not cause any problems, an empty list is returned.
	 * If {@code amendment} does cause problems, a list containing the names of all parameters, which would cause the problems.
	 * The budget is NOT checked. Ids are not compared.
	 * 
	 * @param amendment the potential replacement
	 * @param oldConf the config file of the running experiment
	 * @return a list of invalid parameters which is empty if {@code amendment} is suitable
	 * @throws DatabaseException if this fails to read from the database
	 */
	public List<String> compare(ConfigModel amendment, ConfigModel oldConf) throws DatabaseException {
		Set<String> result = new HashSet<String>();
		
		List<String> verification = this.run(amendment);
		if (!verification.isEmpty()) {
			result.addAll(verification);
		}
		verification = this.run(oldConf);
		if (!verification.isEmpty()) {
			return verification;
		}
		
		//HIT specification
		if (!amendment.getTaskTitle().equals(oldConf.getTaskTitle())) {
			result.add("taskTitle");
		}
		if (amendment.getTaskTags().length != oldConf.getTaskTags().length) {
			result.add("taskTags");
		} else {
			for (String amdTag : amendment.getTaskTags()) {
				boolean exists = false;
				for (String oldTag : oldConf.getTaskTags()) {
					if (oldTag.equals(amdTag)) {
						exists = true;
						break;
					}
				}
				if (!exists) {
					result.add("taskTags");
					break;
				}
			}
		}
		
		//MTurk qualifications
		int countAmendment = 0;
		for (@SuppressWarnings("unused") String amdQuali : amendment.getQualifications(PlatformIdentity.MTurk)) {
			countAmendment++;
		}
		int countOld = 0;
		for (@SuppressWarnings("unused") String oldQuali : oldConf.getQualifications(PlatformIdentity.MTurk)) {
			countOld++;
		}
		if (countOld != countAmendment) {
			result.add("qualificationsMTurk");
		} else {
			for (String amdQuali : amendment.getQualifications(PlatformIdentity.MTurk)) {
				boolean exists = false;
				for (String oldQuali : oldConf.getQualifications(PlatformIdentity.MTurk)) {
					if (oldQuali.equals(amdQuali)) {
						exists = true;
						break;
					}
				}
				if (!exists) {
					result.add("qualificationsMTurk");
					break;
				}
			}
		}
		
		//Payment
		if (amendment.getBasicPayment(PlatformIdentity.MTurk) != oldConf.getBasicPayment(PlatformIdentity.MTurk)) {
			result.add("basicPaymentMTurk");
		}
		if (amendment.getBasicPayment(PlatformIdentity.PyBossa) != oldConf.getBasicPayment(PlatformIdentity.PyBossa)) {
			result.add("basicPaymentPyBossa");
		}
		if (amendment.getPaymentPerTask(PlatformIdentity.MTurk, TypeOfTask.Creative)
				!= oldConf.getPaymentPerTask(PlatformIdentity.MTurk, TypeOfTask.Creative)) {
			result.add("paymentPerTaskCrMTurk");
		}
		if (amendment.getPaymentPerTask(PlatformIdentity.PyBossa, TypeOfTask.Creative)
				!= oldConf.getPaymentPerTask(PlatformIdentity.PyBossa, TypeOfTask.Creative)) {
			result.add("paymentPerTaskCrPyBossa");
		}
		if (amendment.getPaymentPerTask(PlatformIdentity.MTurk, TypeOfTask.Rating)
				!= oldConf.getPaymentPerTask(PlatformIdentity.MTurk, TypeOfTask.Rating)) {
			result.add("paymentPerTaskRaMTurk");
		}
		if (amendment.getPaymentPerTask(PlatformIdentity.PyBossa, TypeOfTask.Rating) !=
				oldConf.getPaymentPerTask(PlatformIdentity.PyBossa, TypeOfTask.Rating)) {
			result.add("paymentPerTaskRaPyBossa");
		}
		

		//Strategies
		for (String amdStrat : amendment.getStrategy().keySet()) {
			if (amdStrat.startsWith("apoc_") || amdStrat.startsWith("tacc_")) {
				String oldStrat = oldConf.getStrategy().get(amdStrat);
				if (oldStrat == null || !oldStrat.equals(amendment.getStrategy().get(amdStrat))) {
					result.add("strategy");
					return new LinkedList<String>(result);
				}
			}
		}		
		for (String oldStrat : oldConf.getStrategy().keySet()) {
			if (oldStrat.startsWith("apoc_") || oldStrat.startsWith("tacc_")) {
				String amdStrat = amendment.getStrategy().get(oldStrat);
				if (amdStrat == null || !amdStrat.equals(oldConf.getStrategy().get(oldStrat))) {
					result.add("strategy");
					return new LinkedList<String>(result);
				}
			}
		}
		
		return new LinkedList<String>(result);
	}

}

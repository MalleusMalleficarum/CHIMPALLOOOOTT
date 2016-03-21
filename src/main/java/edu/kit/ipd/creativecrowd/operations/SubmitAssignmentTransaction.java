package edu.kit.ipd.creativecrowd.operations;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

import edu.kit.ipd.chimpalot.util.StringSimilarityMeasure;
import edu.kit.ipd.creativecrowd.crowdplatform.ConnectionFailedException;
import edu.kit.ipd.creativecrowd.crowdplatform.IllegalInputException;
import edu.kit.ipd.creativecrowd.crowdplatform.WorkerId;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAssignment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.persistentmodel.ExperimentWatchdog;
import edu.kit.ipd.creativecrowd.readablemodel.Answer;
import edu.kit.ipd.creativecrowd.readablemodel.ExperimentType;
import edu.kit.ipd.creativecrowd.readablemodel.Rating;
import edu.kit.ipd.creativecrowd.transformer.ConfigFileInterpreter;
import edu.kit.ipd.creativecrowd.transformer.Transformer;

/**
 * This class organises the order of submitting an assignment marks assignment
 * submitted recalls the ccalculation of the totalAssignmentCount tests whether
 * answers are sufficiently rated now in order to not use them again in a
 * ratingTask
 * 
 * @author Anika
 */
public class SubmitAssignmentTransaction extends Transaction {
	/**
	 * performs the submit
	 * 
	 * @param ass
	 * @param experiment
	 * @throws DatabaseException
	 *         -
	 * @throws ConnectionFailedException
	 * @throws IllegalInputException
	 */
	public void run(MutableAssignment ass, MutableExperiment experiment)
			throws DatabaseException, ConnectionFailedException,
			IllegalInputException {
		ass.setSubmitted();
		TotalAssignmentCountController tacc;
		try {
			tacc = this.loadTACCStrategy(experiment.getStrategyParams());
		} catch (StrategyNotFoundException e) {
			// If this happens now, it's bad. We validate the strategy existence
			// while creating the experiment.
			throw new Error("Strategy class disappeared! " + e.getMessage());
		}
		RatingSufficiencyDecider rsd;
		try {
			rsd = this.loadRSDStrategy(experiment.getStrategyParams());
		} catch (StrategyNotFoundException e) {
			// If this happens now, it's bad. We validate the strategy existence
			// while creating the experiment.
			throw new Error("Strategy class disappeared! " + e.getMessage());
		}
		// look if a answer is now sufficiently rated
		for (MutableAnswer answer : experiment.getCreativeTask().getAnswers()) {
			for (Rating rating : ass.getTaskConstellation().getRatings()) {
				if (rating.getAnswer().equals(answer)) {
					if (rsd.hasEnoughRatings(answer, experiment)) {
						answer.markAsRated();
					}
				}
			}
		}
		this.validate(ass, experiment);
		
		ExperimentWatchdog watchdog = ExperimentWatchdog.getWatchdog(experiment.getID());		
		for(Answer ans : ass.getTaskConstellation().getAnswers()) {
			watchdog.increasePermanent();
			watchdog.increaseSubmitted();
			if(ans.isInvalid()) {
				watchdog.increaseDuplicate();
			}
		}

		
		
		
		
		AssignmentPaymentOutcomeCalculator apoc;
		try {
			apoc = this.loadAPOCStrategy(experiment.getStrategyParams());
		} catch (StrategyNotFoundException e) {
			// If this happens now, it's bad. We validate the strategy existence
			// while creating the experiment.
			throw new Error("Strategy class disappeared! " + e.getMessage());
		}
		int nrofA = tacc.run(experiment,
				apoc.worstCasePayoutPerAssignment(experiment),
				apoc.getBonusPool());

		boolean sufficient = true;
		/*-?|Anika|Anika|c0|?*/
		// look if everything is sufficiently rated
		// or all available assignments are submitted and the experiment needs to be ended
		for (MutableAnswer ans : experiment.getCreativeTask().getAnswers()) {
			if (!ans.isSufficientlyRated()) {
				sufficient = false;
			}
		}
		int submitted = 0;
		for (MutableAssignment as : experiment.getAssignments()) {
			if (as.isSubmitted()) {
				submitted++;
			}
		}
		if (sufficient || nrofA <= submitted) {
			EndExperimentTransaction end = new EndExperimentTransaction();
			end.run(experiment);
		}
		else {
			/*
			 * Originally this was directed to mturk only, now it runs over the transformer
			 * @Robin 
			 */
			Transformer transformer = ConfigFileInterpreter.getTransformer(experiment);
			transformer.extendAssignmentNumber(nrofA, experiment.getHitID());
			
		}
	}
	
	private void validate(MutableAssignment assignment,
			MutableExperiment experiment) throws DatabaseException {
		// check if worker was allowed
		for (WorkerId id : experiment.getBlockedWorkers()) {
			if (assignment.getWorkerID().equals(id)) {
				for (MutableAnswer ans : assignment.getTaskConstellation()
						.getAnswers()) {
					ans.markAsInvalid();
					return;
				}
			}
		}
		if (assignment.getWorker().isBlocked()) {
			for (MutableAnswer ans : assignment.getTaskConstellation()
					.getAnswers()) {
				ans.markAsInvalid();
				return;
			}
		}
		// check if is duplicate
		for (MutableAnswer answer : assignment.getTaskConstellation()
				.getAnswers()) {
			if (answer.getText().equals("") || answer.getText().equals(null)) {
				answer.markAsInvalid();
				return;
			}
			for (MutableAnswer a : assignment.getTaskConstellation()
					.getAnswers()) {
				if (answer.getText().equals(a.getText())) {
					if (answer.getID().equals(a.getID())) {
						continue;
					} else {
						answer.markAsInvalid();
						return;
					}
				}
			}
		}
		if (experiment.getType().equals(ExperimentType.Bild)) {
			HashMap<BufferedImage, MutableAnswer> pictures
			= new HashMap<BufferedImage, MutableAnswer>();
			for (MutableAnswer a : experiment.getCreativeTask().getAnswers()) {
				// we do not want to compare answers with each other
				if (a.getMturkAssignmentId() == assignment
						.getMTurkAssignmentID().getId()) {
					continue;
				} else {
					// load answers in hashmap
					URL url = null;
					try {
						url = new URL(a.getText());
					} catch (MalformedURLException e) {
						a.markAsInvalid();
					}
					try {
						BufferedImage image = ImageIO.read(url);
						pictures.put(image, a);
					} catch (IOException e) {
						a.markAsInvalid();
					}
				}
			}
			for(MutableAnswer a: assignment.getTaskConstellation().getAnswers()) {
				URL url = null;
				try {
					url = new URL(a.getText());
				} catch (MalformedURLException e) {
					a.markAsInvalid();
				}
				try {
					BufferedImage image = ImageIO.read(url);
					int[] pixelOfB = new int[image.getWidth() * image.getHeight()];
					image.getData().getPixel(0, 0, pixelOfB);
					for(BufferedImage i: pictures.keySet()) {

						int[] pixelOfA = new int[i.getWidth() * i.getHeight()];
						i.getData().getPixels(0, 0, 0, 0, pixelOfA);

					        if(Arrays.equals(pixelOfA, pixelOfB)) {
					        	pictures.get(image).markAsInvalid();
					        }
					}
				} catch (IOException e) {
					a.markAsInvalid();
				}
		}
		
			
		} else {
			StringSimilarityMeasure ssm = new StringSimilarityMeasure();
			List<String> allAnswers = new ArrayList<String>();
			for (MutableAnswer answer : experiment.getCreativeTask()
					.getAnswers()) {
				allAnswers.add(answer.getText());
			}
			Set<List<String>> allNGrams = ssm.getNGrams(allAnswers);
			for (MutableAnswer answer : assignment.getTaskConstellation()
					.getAnswers()) {
				Set<List<String>> newAnswer = new HashSet<List<String>>();
				List<String> newText = new ArrayList<String>();
				newText.add(answer.getText());
				newAnswer.add(newText);
				double similarity = ssm.getNormalizedSimilarity(newAnswer,
						allNGrams);
				if (similarity > 0.70) {
					answer.markAsInvalid();
				}
			}
		}
		for(MutableAnswer a: assignment.getTaskConstellation().getAnswers()) {
			if (a.isInvalid()) {
				for(MutableAnswer as: assignment.getTaskConstellation().getAnswers()) {
					as.markAsInvalid();
				}
			}
		}
	}
}

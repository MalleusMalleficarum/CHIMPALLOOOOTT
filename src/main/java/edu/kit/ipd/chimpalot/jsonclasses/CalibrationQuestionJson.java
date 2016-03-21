package edu.kit.ipd.chimpalot.jsonclasses;

import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.kit.ipd.chimpalot.util.Logger;
import edu.kit.ipd.creativecrowd.connector.ModelException;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutablePossibleCalibrationAnswer;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.CalibrationAnswer;
import edu.kit.ipd.creativecrowd.readablemodel.CalibrationQuestion;
import edu.kit.ipd.creativecrowd.readablemodel.PossibleCalibrationAnswer;

/**
 * Used to get a json-representation of a calibration question
 * 
 * @author Thomas Friedel
 */
public class CalibrationQuestionJson implements CalibrationQuestion { 
	private LinkedList<MutableExperiment> experiments = new LinkedList<MutableExperiment>();	
	private LinkedList<PossibleCalibrationAnswerJson> possibleAnswers = new LinkedList<PossibleCalibrationAnswerJson>();
	private LinkedList<CalibrationAnswer> calibrationAnswers = new LinkedList<CalibrationAnswer>();
	private String question;
	private String id;
	
	/**
	 * Empty constructor
	 */
	public CalibrationQuestionJson() {
		//Empty constructor.
	}

	/**
	 * Initializes a CalibrationQuestionJson from an already existing calibration question.
	 *
	 * @param calib the CalibrationQuestion to initialize from
	 * @throws ModelException if reading of {@code calib} fails
	 */
	public CalibrationQuestionJson(CalibrationQuestion calib) throws ModelException {
		try {
			this.setExperiments(calib.getExperiments());
			if (calib.getPossibleAnswers() != null) {
				for (PossibleCalibrationAnswer ans : calib.getPossibleAnswers()) {
					this.possibleAnswers.add(new PossibleCalibrationAnswerJson(ans));
				}
			}
			this.setCalibrationAnswers(calib.getCalibrationAnswers());
			this.setQuestion(calib.getQuestion());
			this.setId(calib.getID());
		} catch (DatabaseException e) {
			Logger.logException(e.getMessage());
			throw new ModelException(e.getMessage());
		}
	}

	@Override
	@JsonIgnore
	public List<? extends MutableExperiment> getExperiments() throws DatabaseException {
		return this.experiments;
	}

	@Override
	@JsonIgnore
	public Iterable<? extends MutablePossibleCalibrationAnswer> getPossibleAnswers() throws DatabaseException {
		return this.possibleAnswers;
	}
	
	@JsonProperty("possibleAnswers")
	public Iterable<PossibleCalibrationAnswerJson> getPossibleAnswersJson() {
		return this.possibleAnswers;
	}

	@Override
	@JsonIgnore
	public Iterable<? extends CalibrationAnswer> getCalibrationAnswers() throws DatabaseException {
		return this.calibrationAnswers;
	}

	@Override
	public String getQuestion() throws DatabaseException {
		return this.question;
	}

	@Override
	public String getID() {
		return this.id;
	}

	/**
	 * @param possibleAnswers the possibleAnswers to set
	 */
	@JsonProperty("possibleAnswers")
	public void setPossibleAnswers(Iterable<PossibleCalibrationAnswerJson> possibleAnswers) throws ModelException {
		this.possibleAnswers = new LinkedList<PossibleCalibrationAnswerJson>();
		if (possibleAnswers != null) {
			for (PossibleCalibrationAnswer ans : possibleAnswers) {
				this.possibleAnswers.add(new PossibleCalibrationAnswerJson(ans));
			}
		}
	}

	/**
	 * @param question the question to set
	 */
	public void setQuestion(String question) {
		this.question = (question == null)? new String() : question;
	}

	/**
	 * Cannot be set with json
	 * 
	 * @param iterable the experiments to set
	 */
	@JsonIgnore
	public void setExperiments(Iterable<? extends MutableExperiment> iterable) {
		this.experiments = new LinkedList<MutableExperiment>();
		for (MutableExperiment exp : iterable) {
			this.experiments.add(exp);
		}
	}

	/**
	 * @param iterable the calibrationAnswers to set
	 */
	@JsonIgnore
	public void setCalibrationAnswers(Iterable<? extends CalibrationAnswer> iterable) {
		this.calibrationAnswers = new LinkedList<CalibrationAnswer>();
	}

	/**
	 * Cannot be set with json
	 * 
	 * @param id the id to set
	 */
	@JsonIgnore
	public void setId(String id) {
		this.id = id;
	}
}

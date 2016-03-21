package edu.kit.ipd.chimpalot.jsonclasses;

import com.fasterxml.jackson.annotation.JsonIgnore;

import edu.kit.ipd.chimpalot.util.Logger;
import edu.kit.ipd.creativecrowd.connector.ModelException;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableCalibrationQuestion;
import edu.kit.ipd.creativecrowd.mutablemodel.MutablePossibleCalibrationAnswer;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.PossibleCalibrationAnswer;

/**
 * Used to get a json-representation of a calibration question
 * 
 * @author Thomas Friedel
 */
public class PossibleCalibrationAnswerJson implements MutablePossibleCalibrationAnswer {
	
	private String id;
	private String text;
	private boolean isTrue;
	
	/**
	 * Empty constructor
	 */
	public PossibleCalibrationAnswerJson() {
		//Empty constructor.
	}
	
	/**
	 * Initializes this from an existing possible answer
	 * @param ans the existing possible answer
	 * @throws ModelException if this fails to read from {@code ans}
	 */
	public PossibleCalibrationAnswerJson(PossibleCalibrationAnswer ans) throws ModelException {
		this.setID(ans.getID());
		try {
			this.setText(ans.getText());
			this.setIsTrue(ans.getIsTrue());
		} catch (DatabaseException e) {
			Logger.logException(e.getMessage());
			throw new ModelException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.PossibleCalibrationAnswer#getID()
	 */
	@Override
	public String getID() {
		return this.id;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.PossibleCalibrationAnswer#getText()
	 */
	@Override
	public String getText() throws DatabaseException {
		return this.text;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.PossibleCalibrationAnswer#getIsTrue()
	 */
	@Override
	public boolean getIsTrue() throws DatabaseException {
		return this.isTrue;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.PossibleCalibrationAnswer#getCalibrationQuestion()
	 */
	@Override
	@JsonIgnore //Otherwise we will create an endless loop... we do not want that
	public MutableCalibrationQuestion getCalibrationQuestion() throws DatabaseException {
		return null;
		//Not assigned here.
	}
	
	/**
	 * Sets the id.
	 * 
	 * @param id the id to set
	 */
	@JsonIgnore
	public void setID(String id) {
		this.id = (id == null)? new String() : id;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutablePossibleCalibrationAnswer#setText(java.lang.String)
	 */
	@Override
	public void setText(String answer) throws DatabaseException {
		this.text = (answer == null)? new String() : answer;

	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutablePossibleCalibrationAnswer#setIsTrue(boolean)
	 */
	@Override
	public void setIsTrue(boolean isTrue) throws DatabaseException {
		this.isTrue = isTrue;

	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutablePossibleCalibrationAnswer#setCalibrationQuestion(java.lang.String)
	 */
	@Override
	@JsonIgnore
	public void setCalibrationQuestion(String calibquestID) throws DatabaseException {
		//Empty, not needed
	}

}

package edu.kit.ipd.chimpalot.jsonclasses;

import com.fasterxml.jackson.annotation.JsonIgnore;

import edu.kit.ipd.creativecrowd.mutablemodel.MutableControlQuestion;
import edu.kit.ipd.creativecrowd.mutablemodel.MutablePossibleControlAnswer;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.PossibleControlAnswer;

/**
 * POJO for PossibleControlAnswer.
 * Mutable because ControlQuestion says so.
 * 
 * @author Thomas Friedel
 */
public class PossibleControlAnswerJson implements MutablePossibleControlAnswer {
	
	private String iD;
	private String text;
	private boolean isTrue;
	
	/**
	 * Empty constructor.
	 */
	public PossibleControlAnswerJson() {
	}
	
	/**
	 * Create PossibleControlAnswerJson from existing PossibleControlAnswer
	 * @param ans the existing answer
	 * @throws DatabaseException if reading from database fails
	 */
	public PossibleControlAnswerJson(PossibleControlAnswer ans) throws DatabaseException {
		this.setID(ans.getID());
		this.setText(ans.getText());
		this.setIsTrue(ans.getIsTrue());
	}

	
	
	/**
	 * @param iD the iD to set
	 */
	public void setID(String iD) {
		this.iD = iD;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutablePossibleControlAnswer#setText(java.lang.String)
	 */
	@Override
	public void setText(String text) {
		this.text = text;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutablePossibleControlAnswer#setIsTrue(boolean)
	 */
	@Override
	public void setIsTrue(boolean isTrue) {
		this.isTrue = isTrue;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.PossibleControlAnswer#getID()
	 */
	@Override
	public String getID() {
		return iD;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.PossibleControlAnswer#getText()
	 */
	@Override
	public String getText() throws DatabaseException {
		return text;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.PossibleControlAnswer#getIsTrue()
	 */
	@Override
	public boolean getIsTrue() throws DatabaseException {
		return isTrue;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.PossibleControlAnswer#getControlQuestion()
	 */
	@Override
	@JsonIgnore
	public MutableControlQuestion getControlQuestion() throws DatabaseException {
		return null;
		//Not used here
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.mutablemodel.MutablePossibleControlAnswer#setControlQuestion(java.lang.String)
	 */
	@Override
	@JsonIgnore
	public void setControlQuestion(String controlquestID) throws DatabaseException {
		//Nope
	}

}

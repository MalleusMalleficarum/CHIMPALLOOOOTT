package edu.kit.ipd.chimpalot.jsonclasses;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.kit.ipd.creativecrowd.mutablemodel.MutablePossibleControlAnswer;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.ControlQuestion;
import edu.kit.ipd.creativecrowd.readablemodel.PossibleControlAnswer;

/**
 * POJO of ControlQuestion
 * 
 * @author Thomas Friedel
 */
public class ControlQuestionJson implements ControlQuestion {
	
	private String question;
	private List<PossibleControlAnswerJson> possibleAnswers = new ArrayList<PossibleControlAnswerJson>();
	private String id;
	
	/**
	 * Empty constructor. POJO!
	 */
	public ControlQuestionJson(){
	}
	
	/**
	 * Create ControlQuestionJson from existing ControlQuestion
	 * 
	 * @param question the existing ControlQuestion 
	 * @throws DatabaseException if reading the database fails
	 */
	public ControlQuestionJson(ControlQuestion question) throws DatabaseException {
		this.setId(question.getID());
		this.possibleAnswers = new ArrayList<PossibleControlAnswerJson>();
		if (question.getPossibleAnswers() != null) {
			for (PossibleControlAnswer ans : question.getPossibleAnswers()) {
				this.possibleAnswers.add(new PossibleControlAnswerJson(ans));
			}
		}
		this.setQuestion(question.getQuestion());
	}

	/**
	 * @param question the question to set
	 */
	public void setQuestion(String question) {
		this.question = question;
	}

	/**
	 * @param possibleAnswers the possibleAnswers to set
	 */
	@JsonProperty("possibleAnswers")
	public void setPossibleAnswers(Iterable<PossibleControlAnswerJson> possibleAnswers) {
		this.possibleAnswers = new ArrayList<PossibleControlAnswerJson>();
		if (possibleAnswers != null) {
			for (PossibleControlAnswerJson ans : possibleAnswers) {
				this.possibleAnswers.add(ans);
			}
		}
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.ControlQuestion#getQuestion()
	 */
	@Override
	public String getQuestion() throws DatabaseException {
		return question;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.ControlQuestion#getPossibleAnswers()
	 */
	@Override
	@JsonIgnore //Jackson does not like interfaces
	public Iterable<? extends MutablePossibleControlAnswer> getPossibleAnswers() throws DatabaseException {
		return possibleAnswers;
	}
	
	@JsonProperty("possibleAnswers")
	public Iterable<PossibleControlAnswerJson> getPossibleAnswersJson() {
		return possibleAnswers;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.kit.ipd.creativecrowd.readablemodel.ControlQuestion#getID()
	 */
	@Override
	public String getID() {
		return id;
	}

}

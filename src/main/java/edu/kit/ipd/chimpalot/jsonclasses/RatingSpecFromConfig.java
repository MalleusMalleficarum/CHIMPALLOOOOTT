package edu.kit.ipd.chimpalot.jsonclasses;

import edu.kit.ipd.creativecrowd.connector.RatingSpec;

/**
 * (originally creativecrowd.controller.RatingSpecImp)
 * 
 * @see edu.kit.ipd.creativecrowd.connector.RatingSpec 
 * @author unknown
 */
public class RatingSpecFromConfig implements RatingSpec {
	
	String answerId;
	String ratingOptionId;
	String text;

	public RatingSpecFromConfig(String answerId, String ratingOptionId, String text) {
		this.answerId = answerId;
		this.ratingOptionId = ratingOptionId;
		this.text = text;
	}

	public String getAnswerID() {
		return answerId;
	}

	public String getRatingOptionId() {
		return ratingOptionId;

	}

	public String getText() {
		return text;
	}
}

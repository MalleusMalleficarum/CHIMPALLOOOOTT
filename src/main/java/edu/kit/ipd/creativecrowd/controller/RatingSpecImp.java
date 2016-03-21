package edu.kit.ipd.creativecrowd.controller;

import edu.kit.ipd.creativecrowd.connector.RatingSpec;

public class RatingSpecImp implements RatingSpec {
	String answerId;
	String ratingOptionId;
	String text;

	public RatingSpecImp(String answerId, String ratingOptionId, String text) {
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

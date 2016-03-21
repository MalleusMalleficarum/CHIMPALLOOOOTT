package edu.kit.ipd.creativecrowd.controller;

import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.RatingOption;

public class RatingOptionSpec implements RatingOption{

	private String text;
	private float value;
	public RatingOptionSpec(String text, float value){
		this.text = text;
		this.value = value;
	}
	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getValue() throws DatabaseException {
		// TODO Auto-generated method stub
		return value;
	}

	@Override
	public String getText() throws DatabaseException {
		// TODO Auto-generated method stub
		return text;
	}

}

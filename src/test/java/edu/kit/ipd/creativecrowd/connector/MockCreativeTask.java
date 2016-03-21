package edu.kit.ipd.creativecrowd.connector;

import edu.kit.ipd.creativecrowd.mutablemodel.MutableAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableCreativeTask;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;

public class MockCreativeTask implements MutableCreativeTask{

	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPictureURL() throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPictureLicenseURL() throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDescription(String description) throws DatabaseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPicture(String urlImage, String urlLicense)
			throws DatabaseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Iterable<? extends MutableAnswer> getAnswers()
			throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAccordingRatingTaskDescription() throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAccordingRatingTaskDescription(String description)
			throws DatabaseException {
		// TODO Auto-generated method stub
		
	}

}

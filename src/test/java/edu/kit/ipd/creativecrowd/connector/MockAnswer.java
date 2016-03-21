package edu.kit.ipd.creativecrowd.connector;

import edu.kit.ipd.creativecrowd.mutablemodel.MutableAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableCreativeTask;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRating;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;

public class MockAnswer implements MutableAnswer{

	@Override
	public String getTimestampBegin() throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getText() throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSufficientlyRated() throws DatabaseException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSubmitted() throws DatabaseException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public float getFinalQualityIndex() throws DatabaseException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setText(String answer) throws DatabaseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addRating(MutableRating rating) throws DatabaseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Iterable<? extends MutableRating> getRatings()
			throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void markAsRated() throws DatabaseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFinalQualityIndex(float idx) throws DatabaseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MutableCreativeTask getCreativeTask() throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

}

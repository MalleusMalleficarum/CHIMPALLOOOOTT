package edu.kit.ipd.creativecrowd.connector;

import edu.kit.ipd.creativecrowd.mutablemodel.MutableRatingOption;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;

public class MockRatingOption implements MutableRatingOption{

	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return "Answer";
	}

	@Override
	public float getValue() throws DatabaseException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getText() throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setText(String name) throws DatabaseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(float rating) throws DatabaseException {
		// TODO Auto-generated method stub
		
	}

}

package edu.kit.ipd.creativecrowd.connector;

import edu.kit.ipd.creativecrowd.mutablemodel.MutableAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRating;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRatingOption;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.Answer;

public class MockRating implements MutableRating{

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
	public MutableAnswer getAnswer() throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getFinalQualityIndex() throws DatabaseException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setText(String text) throws DatabaseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRatingOption(MutableRatingOption option)
			throws DatabaseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MutableRatingOption getSelectedRatingOption()
			throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFinalQualityIndex(float idx) throws DatabaseException {
		// TODO Auto-generated method stub
		
	}

}

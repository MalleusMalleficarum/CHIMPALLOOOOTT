package edu.kit.ipd.creativecrowd.connector;

import edu.kit.ipd.creativecrowd.mutablemodel.MutableAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRating;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRatingTask;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.Answer;

public class MockRatingTask implements MutableRatingTask{

	@Override
	public Iterable<? extends MutableAnswer> getAnswersToBeRated()
			throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addAnswerToBeRated(MutableAnswer as) throws DatabaseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MutableAnswer getAnswerToBeRated(String ansId)
			throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<? extends MutableRating> getRatings()
			throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

}

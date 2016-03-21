package edu.kit.ipd.creativecrowd.connector;

import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableStats;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;

public class MockStats implements MutableStats{

	@Override
	public int getCancelledCount() throws DatabaseException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getPreviewClicksCount() throws DatabaseException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getAcceptedHitCount() throws DatabaseException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSubmissionCount() throws DatabaseException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getRatingCount() throws DatabaseException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getTimestampBegin() throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addPreviewClick() throws DatabaseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MutableExperiment getExperiment() throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

}

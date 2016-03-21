package edu.kit.ipd.creativecrowd.connector;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAssignment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableCreativeTask;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRating;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRatingTask;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableTaskConstellation;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.Task;
public class MockTaskConstellation implements MutableTaskConstellation{

	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<? extends Task> getTasks() throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean nextButtonExists() throws DatabaseException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean againButtonExists() throws DatabaseException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean submitButtonExists() throws DatabaseException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Task getCurrentTask() throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getCurrentTaskIndex() throws DatabaseException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setNextButton(boolean next) throws DatabaseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAgainButton(boolean again) throws DatabaseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSubmitButton(boolean submit) throws DatabaseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCurrentTask(int taskPosition) throws DatabaseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addCreativeTask(MutableCreativeTask ct)
			throws DatabaseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MutableCreativeTask getMutableCreativeTask(int taskPosition)
			throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addRatingTask(MutableRatingTask task) throws DatabaseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MutableRatingTask getMutableRatingTask(int taskPosition)
			throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTaskCount() throws DatabaseException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public MutableAnswer answerCreativeTaskAt(int index)
			throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MutableRating addRatingToRatingTaskAt(int index)
			throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<MutableAnswer> getAnswers() throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<MutableRating> getRatings() throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MutableAssignment getAssignment() throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

}

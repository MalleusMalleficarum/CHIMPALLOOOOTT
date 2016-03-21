package edu.kit.ipd.creativecrowd.connector;
import java.util.Map;

import edu.kit.ipd.creativecrowd.mturk.AssignmentId;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAssignment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableCreativeTask;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableExperiment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRatingOption;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRatingTask;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableStats;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.Assignment;
public class MockExperiment implements MutableExperiment{

	@Override
	public boolean isFinished() throws DatabaseException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Assignment getAssignmentWithMturkId(AssignmentId mtrkid)
			throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHitID() throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<String> getTags() throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<String> getQualifications() throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getStrategyParams() throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getBudgetCents() throws DatabaseException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getBasicPaymentHITCents() throws DatabaseException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getBasicPaymentAnswerCents() throws DatabaseException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getBasicPaymentRatingCents() throws DatabaseException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getBonusPaymentCents() throws DatabaseException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getHITTitle() throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHITDescription() throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getMaxNumberOfAnswersPerAssignment() throws DatabaseException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaxNumberOfRatingsPerAssignment() throws DatabaseException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public MutableStats getStats() throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MutableAssignment addAssignment() throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<? extends MutableAssignment> getAssignments()
			throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MutableAssignment getAssignment(String assignmentid)
			throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MutableCreativeTask addCreativeTask() throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MutableCreativeTask getCreativeTask() throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MutableRatingTask addRatingTask() throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<? extends MutableRatingTask> getRatingTasks()
			throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MutableRatingOption addRatingOption() throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<? extends MutableRatingOption> getRatingOptions()
			throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void markAsFinished() throws DatabaseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void markExperimentAsSoftFinished() throws DatabaseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDescription(String description) throws DatabaseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBudget(int cents) throws DatabaseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBasicPaymentHIT(int cents) throws DatabaseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBasicPaymentAnswer(int cents) throws DatabaseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBasicPaymentRating(int cents) throws DatabaseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHITTitle(String title) throws DatabaseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHITDescription(String description) throws DatabaseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTags(Iterable<String> tags) throws DatabaseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setQualifications(Iterable<String> qualifications)
			throws DatabaseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBonusPayment(int cents) throws DatabaseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHitID(String id) throws DatabaseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setStrategyParams(Map<String, String> params)
			throws DatabaseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMaxNumberOfRatingsPerAssignment(int maxRatings)
			throws DatabaseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMaxNumberOfAnswersPerAssignment(int maxAnswers)
			throws DatabaseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getRatingTaskViewClass() throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setRatingTaskViewClass(String ratingTaskViewClassName)
			throws DatabaseException {
		// TODO Auto-generated method stub
		
	}

}

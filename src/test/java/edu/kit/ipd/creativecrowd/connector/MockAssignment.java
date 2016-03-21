package edu.kit.ipd.creativecrowd.connector;
import edu.kit.ipd.creativecrowd.mturk.AssignmentId;
import edu.kit.ipd.creativecrowd.mturk.WorkerId;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAssignment;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableTaskConstellation;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.Experiment;
import edu.kit.ipd.creativecrowd.readablemodel.PaymentOutcome;
public class MockAssignment implements MutableAssignment{

	@Override
	public boolean isSubmitted() throws DatabaseException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPaid() throws DatabaseException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public AssignmentId getMTurkAssignmentID() throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WorkerId getWorkerID() throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PaymentOutcome getPaymentOutcome() throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Experiment getExperiment() throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSubmitted() throws DatabaseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPaymentOutcome(boolean receives_base_payment,
			int bonusPaymentAmountInCents) throws DatabaseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setWorker(WorkerId id) throws DatabaseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void markAsPaid() throws DatabaseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MutableTaskConstellation getTaskConstellation()
			throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAssignmentID(AssignmentId id) throws DatabaseException {
		// TODO Auto-generated method stub
		
	}

}

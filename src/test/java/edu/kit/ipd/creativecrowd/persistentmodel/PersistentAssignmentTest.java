package edu.kit.ipd.creativecrowd.persistentmodel;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

import edu.kit.ipd.creativecrowd.crowdplatform.AssignmentId;
import edu.kit.ipd.creativecrowd.crowdplatform.WorkerId;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableTaskConstellation;
import edu.kit.ipd.creativecrowd.readablemodel.PaymentOutcome;

public class PersistentAssignmentTest extends PersistentAssignmentBefore {

	/**
	 * setzte testAss auf submitted und überprüfe, ob es auf submitted gesetzt wurde
	 */
	@Test
	public void submittingTest() {
		try {
			testAss.setSubmitted();
			assertTrue(testAss.isSubmitted());
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	/**
	 * setzte eine MTurkAssignmentId und überprüfe, ob sie korrekt gespeichert wurde
	 */
	@Test
	public void mTurkAssignmentIDTest() {
		try {
			AssignmentId assignmentId = new AssignmentId("123M567");
			testAss.setAssignmentID(assignmentId);
			assertTrue(testAss.getMTurkAssignmentID().getId().contentEquals("123M567"));
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	/**
	 * setzte eine MTurkWorkerId und überprüfe, ob sie korrekt gespeichert wurde
	 */
	@Test
	public void mTurkWorkerIDTest() {
		try {
			WorkerId workerId = new edu.kit.ipd.creativecrowd.crowdplatform.WorkerId("123MW567");
			testAss.setWorker(workerId);
			assertTrue(testAss.getWorkerID().getId().contentEquals("123MW567"));
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}
	
	/**
	 * überprüfe die TaskConstellation des testAssignments auf (nicht) null
	 */
	@Test
	public void getTaskConstellationTest() {
		try {
			MutableTaskConstellation mtc = testAss.getTaskConstellation();
			assertTrue(mtc != null);
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}
	
	/**
	 * überprüfe ein zuvor gesetztes PaymentOutcome dieses Assignments
	 */
	@Test
	public void paymentOutcomeTest() {
		try {
			testAss.setPaymentOutcome(true, 5);
			PaymentOutcome testOutcome = testAss.getPaymentOutcome();
			assertTrue(testOutcome.getBonusPaidCents() == 5);
			assertTrue(testOutcome.isApproved());
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}
	/**
	 * setze dieses Assignment auf paid und überprüfe, ob es richtig gesetzt wurde
	 */
	@Test
	public void isPaidTest() {
		try {
			testAss.markAsPaid();
			assertTrue(testAss.isPaid());
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}

	/**
	 * überprüfe, ob getExperiment() das richtige Experiment (testExp) zurückgibt
	 */
	public void getExperiment() {
		try {
			assertTrue(testAss.getExperiment().equals(testExp));
		} catch (DatabaseException e) {
			fail(e.getClass().getName() + ": " + e.getMessage());
		}
	}
}

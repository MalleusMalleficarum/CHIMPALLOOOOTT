package edu.kit.ipd.creativecrowd.operations;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;






import edu.kit.ipd.chimpalot.util.GlobalApplicationConfig;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableWorker;
import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.persistentmodel.PersistentAmazonVoucherRepo;
import edu.kit.ipd.creativecrowd.persistentmodel.PersistentWorkerRepo;
import edu.kit.ipd.creativecrowd.readablemodel.AmazonVoucher;
/**
 * 
 * @author Robin
 *
 */
public class WorkerPaymentCheckerTest {
	static PersistentWorkerRepo rep;
	static WorkerPaymentChecker check;
	static PersistentAmazonVoucherRepo vou;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		GlobalApplicationConfig.configure(true);
		rep = new PersistentWorkerRepo();
		check = new WorkerPaymentChecker();
		vou = new PersistentAmazonVoucherRepo();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test (expected=DatabaseException.class)
	public void nothingIsNotPayable() throws DatabaseException {
		check.isPayable(null);
	}
	@Test (expected=DatabaseException.class)
	public void nothingWillNotBePaid() throws DatabaseException {
		check.payWorker(null);
	}
	@Test
	public void noValidMailNoMoney() {
		try {
			MutableWorker worker = rep.findWorker("fleiß", "ähre");
			assertFalse(check.isPayable(worker.getID()));
			rep.anonymizeWorker(worker.getID());
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void validMailButNoWorkDone() {
		try {
			MutableWorker worker = rep.findWorker("fleiß", "unze@unze.de");
			assertTrue(check.isPayable(worker.getID()));
			vou.addVoucher("XXXX", 10);
			check.payWorker(worker.getID());
			AmazonVoucher u = vou.getVoucherWorth(10);
			vou.useVoucher(u.getID());
			rep.anonymizeWorker(worker.getID());
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	/**
	 * THis test will fail if the database contains a worker with positive creditr
	 */
//	@Test
//	public void payEverything() {
//		try {
//			assertTrue(check.payAllWorkers());
//		} catch (DatabaseException e) {
//			fail(e.getMessage());
//		}
//	}
	/**
	 * Rest assured, i know when you run this test.
	 */
	@Test
	public void sendActualTestMail() {
		try {
			MutableWorker worker = rep.findWorker("fleiß", "dahonk1995@web.de");
			assertFalse(check.hasWorkerEarnedEnough(worker.getID()));
			worker.increaseCredit(9001);
			assertTrue(check.hasWorkerEarnedEnough(worker.getID()));
			assertTrue(check.isPayable(worker.getID()));
			vou.addVoucher("XXXX", 101);
			vou.addVoucher("XXXX", 101);
			check.payWorker(worker.getID());
			rep.anonymizeWorker(worker.getID());
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void sendAnotherTestMail() {
		try {
			MutableWorker worker = rep.findWorker("fleiß", "dahonk1995@web.de");
			MutableWorker worker2 = rep.findWorker("nichtfleiß", "u@u.de");
			worker2.increaseCredit(1);
			assertFalse(check.hasWorkerEarnedEnough(worker.getID()));
			worker.increaseCredit(9001);
			assertTrue(check.hasWorkerEarnedEnough(worker.getID()));
			assertTrue(check.isPayable(worker.getID()));
			vou.addVoucher("XXXX", 101);
			vou.addVoucher("XXXX", 101);
			assertTrue(check.payAllWorkersOverThreshold());
			rep.anonymizeWorker(worker.getID());
			rep.anonymizeWorker(worker2.getID());
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}

}

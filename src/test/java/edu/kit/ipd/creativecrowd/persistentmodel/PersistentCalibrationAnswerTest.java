package edu.kit.ipd.creativecrowd.persistentmodel;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.kit.ipd.chimpalot.util.GlobalApplicationConfig;
import edu.kit.ipd.chimpalot.util.Logger;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableCalibrationAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableCalibrationQuestion;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableWorker;

public class PersistentCalibrationAnswerTest {
	static MutableCalibrationAnswer ans;
	static PersistentCalibrationQuestionRepo rep;
	MutableCalibrationQuestion quest;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		GlobalApplicationConfig.configure(true);
		rep = new PersistentCalibrationQuestionRepo();
		
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		quest = rep.createCalibrationQuestion("Nackte Seemänner verärgern Poseidon");
		ans = quest.addAnswer();
	}

	@After
	public void tearDown() throws Exception {
		rep.deleteCalibrationQuestion("Nackte Seemänner verärgern Poseidon");
		
	}

	@Test
	public void readFromScratch() {
		try {
			assertEquals(ans.getAnswer(), null);
			Logger.debug(ans.getID());
			MutableWorker worker = ans.getWorker();
			Logger.debug(worker.getID());
//			Logger.debug(worker.getName());
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}

}

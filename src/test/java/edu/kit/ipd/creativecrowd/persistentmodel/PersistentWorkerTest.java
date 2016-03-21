package edu.kit.ipd.creativecrowd.persistentmodel;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.kit.ipd.chimpalot.util.GlobalApplicationConfig;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableCalibrationAnswer;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableRating;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableWorker;
import edu.kit.ipd.creativecrowd.readablemodel.CalibrationAnswer;
import edu.kit.ipd.creativecrowd.readablemodel.Rating;

/**
 * I cannot delete workers, this testclass will junk the database.
 * @author Robin 
 *
 */
public class PersistentWorkerTest {
	static PersistentWorkerRepo rep;
	@BeforeClass
	public static void setUpBeforeClass() {
		GlobalApplicationConfig.configure(true);
		try {
			rep = new PersistentWorkerRepo();
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
	public void createStuffFromNull() throws DatabaseException {
		rep.createWorker(null);
	}
	@Test (expected=DatabaseException.class)
	public void loadStuffFromNull() throws DatabaseException {
		rep.loadWorker(null);
	}
	@Test (expected=DatabaseException.class)
	public void loadNameMailNull() throws DatabaseException {
		rep.findWorker(null, null);
	}
	@Test (expected=DatabaseException.class)
	public void loadNameNull() throws DatabaseException {
		rep.findWorker(null, "HALLO");
	}
	@Test (expected=DatabaseException.class)
	public void loadMailNull() throws DatabaseException {
		rep.findWorker("Ich bin Worker", null);
	}
	
	private int length(@SuppressWarnings("rawtypes") Iterable it) {
		int i = 0;
		for(@SuppressWarnings("unused") Object o : it) {
			i++;
		}
		return i;
	}
	/**
	 * NOTE: Every test creates a new worker, strictly speaking this is a database leak.
	 */
	@Test
	public void createWorker() {
		try {
			MutableWorker worker = rep.createWorker(rep.connection.generateID("worker"));
			assertNotNull(worker.getName());
			assertEquals(worker.getName(), "");
			assertEquals(worker.getEmail(), "");
			assertEquals(worker.getCredit(), 0);
			assertEquals(worker.isBlocked(), false);
			assertEquals(length(worker.getAnswers()),0);
			assertEquals(length(worker.getCalibrationAnswers()), 0);
			assertEquals(length(worker.getDoneCalibQuestWorker()), 0);
			assertEquals(length(worker.getRatings()), 0);
			rep.anonymizeWorker(worker.getID());
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void setBasicAttributes() {
		try {
			
			MutableWorker worker = rep.findWorker("hallo", "welt");
			assertEquals(worker.getName(), "hallo");
			assertEquals(worker.getEmail(), "welt");
			worker.setBlocked(true);
			
			assertEquals(worker.isBlocked(), true);
			rep.anonymizeWorker(worker.getID());
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void anonymizeWorker() {
		try {
			MutableWorker worker = rep.findWorker("Please delete my name", "Please delete my mail");
			String id = worker.getID();
			rep.anonymizeWorker(worker.getID());
			assertEquals(worker.getID(), id);
			assertEquals(worker.getName(), "!ANONYM");
			assertEquals(worker.getEmail(), "!ANONYM");
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void setComplexAttributes() {
		try {
			MutableWorker worker = rep.findWorker("Im Complex", "and you cannot change this email afterwards");
			MutableAnswer ans = worker.addAnswer();
			ans.setText("TEST");
			assertEquals(ans.getWorker().getID(), worker.getID());
			assertEquals(ans.getWorker().getEmail(), worker.getEmail());
			assertEquals(ans.getWorker().getName(), worker.getName());
			for(MutableAnswer a : worker.getAnswers()) {
				assertEquals(a.getWorker().getName(), "Im Complex");
				assertEquals(a.getText(), "TEST");
			}
			for(MutableAnswer a : ans.getWorker().getAnswers()) {
				assertEquals(a.getID(), ans.getID());
			}
			rep.anonymizeWorker(worker.getID());
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void doesFindWorkProperly() {
		try {
			MutableWorker worker1 = rep.findWorker("Shared", "Shared");
			MutableWorker worker2 = rep.findWorker("Unique", "Shared");
			MutableWorker worker3 = rep.findWorker("Shared", "Unique");
			MutableWorker worker4 = rep.findWorker("Shared", "Shared");
			assertNotEquals(worker1.getID(), worker2.getID());
			assertNotEquals(worker3.getID(), worker2.getID());
			assertNotEquals(worker1.getID(), worker3.getID());
			assertEquals(worker1.getID(), worker4.getID());
			rep.anonymizeWorker(worker1.getID());
			rep.anonymizeWorker(worker2.getID());
			rep.anonymizeWorker(worker3.getID());
			rep.anonymizeWorker(worker4.getID());
			
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}	
	}
	@Test
	public void setMoreComplexAttributes() {
		try {
			MutableWorker worker = rep.findWorker("toller Name", "tolle Email");
			MutableCalibrationAnswer ans = worker.addCalibrationAnswer();
			ans.setAnswer("MYANSWER");
			assertEquals(ans.getWorker().getID(), worker.getID());
			for(CalibrationAnswer a : worker.getCalibrationAnswers()) {
				assertEquals(a.getAnswer(), "MYANSWER");
			}
			rep.anonymizeWorker(worker.getID());
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void setEvenMoreComplexAttributes() {
		try {
			MutableWorker worker = rep.findWorker("cooler Name", "doppelt benannt");
			MutableRating r = worker.addRating();
			r.setText("HODOR");
			for(Rating rat : worker.getRatings()) {
				assertEquals(rat.getID(), r.getID());
				assertEquals(rat.getText(), r.getText());
			}
			rep.anonymizeWorker(worker.getID());
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void creditWorksProperly() {
		try {
			MutableWorker worker = rep.findWorker("colnam", "doppelt benannt");
			assertEquals(worker.getCredit(), 0);
			assertFalse(worker.decreaseCredit(-1));
			assertEquals(worker.getCredit(), 0);
			assertFalse(worker.decreaseCredit(1));
			assertEquals(worker.getCredit(), 0);
			assertFalse(worker.increaseCredit(-2));
			assertEquals(worker.getCredit(), 0);
			assertTrue(worker.increaseCredit(50));
			assertEquals(worker.getCredit(), 50);
			assertFalse(worker.decreaseCredit(60));
			assertEquals(worker.getCredit(), 50);
			assertTrue(worker.decreaseCredit(40));
			assertEquals(worker.getCredit(), 10);
			rep.anonymizeWorker(worker.getID());
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	//TODO this
	@Test
	public void loadProperly() {
		try {
			File f = new File("CreativeCrowd.db");
			f.delete();
			MutableWorker worker = rep.findWorker("Acchalemas", "Hallebad");
			worker.setBlocked(true);
			MutableWorker worker2 = rep.loadWorker(worker.getID());
			assertTrue(worker2.isBlocked());
			rep.findWorker("ich bin neu hier", "witzlos");
			List<MutableWorker> list = rep.loadAllWorkers();
			for(MutableWorker x : list) {
				String s = x.getName();
				if(!s.equals("!ANONYM")) {
//					if(s.equals("Acchalemas")) {
//						assertTrue(s.equals("Hallebad"));
//					}
					
				}
			}
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
}

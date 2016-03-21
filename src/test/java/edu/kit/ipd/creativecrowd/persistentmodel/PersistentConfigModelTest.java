package edu.kit.ipd.creativecrowd.persistentmodel;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.kit.ipd.chimpalot.jsonclasses.ConfigModelJson;
import edu.kit.ipd.chimpalot.util.GlobalApplicationConfig;
import edu.kit.ipd.chimpalot.util.Logger;
import edu.kit.ipd.creativecrowd.crowdplatform.PlatformIdentity;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableCalibrationQuestion;
import edu.kit.ipd.creativecrowd.mutablemodel.MutableWorker;
import edu.kit.ipd.creativecrowd.readablemodel.CalibrationQuestion;
import edu.kit.ipd.creativecrowd.readablemodel.ConfigModel;
import edu.kit.ipd.creativecrowd.readablemodel.ExperimentType;
import edu.kit.ipd.creativecrowd.readablemodel.TypeOfTask;
import edu.kit.ipd.creativecrowd.readablemodel.Worker;
/**
 * 
 * @author Robin
 *
 */
public class PersistentConfigModelTest {
	static PersistentConfigModel model;
	static PersistentConfigModelRepo rep;
	static PersistentExperimentRepo exprep;
	static PersistentWorkerRepo workerrep;
	static String id;
	static ConfigModelJson js;
	static Worker theLoneWorker;
	
	@BeforeClass
	public static void setUpBeforeClass()  {
		File file = new File("CreativeCrowd.db");
		file.delete();
		GlobalApplicationConfig.configure(true);
		try {
			rep = new PersistentConfigModelRepo();
			workerrep = new PersistentWorkerRepo();
			//theLoneWorker = workerrep.createWorker("Ich bin einsam");
			//exprep = new PersistentExperimentRepo();
			
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
		
	}

	@AfterClass
	public static void tearDownAfterClass() {

	}

	@Before
	public void setUp() throws Exception {
		ConfigModelJson setup = new ConfigModelJson();
		setup.setID("hallo Welt");
		ConfigModel workWiththis = null;
		try {
			workWiththis = rep.createConfigModel(setup, setup.getID(), null);
		} catch (IDAlreadyUsedException e1) {
			// TODO Auto-generated catch block
			fail(e1.getMessage());
		} catch (DatabaseException e1) {
			// TODO Auto-generated catch block
			fail(e1.getMessage());
		}
		model = (PersistentConfigModel) workWiththis;
	

		
	}

	@After
	public void tearDown() throws Exception {
		rep.deleteConfigModel("hallo Welt");
	}
	/*
	 * I know Object Typecast is ugly, but this is only intended to count whatever elements
	 * were used, so no harm.
	 */
	private int length(@SuppressWarnings("rawtypes") Iterable iter) {
		int i = 0;
		for(@SuppressWarnings("unused") Object o : iter) {
			i++;
		}
		return i;
	}
	/**
	 * All fields should return expected base values if never initialised.
	 */
	
	@Test
	public void readBeforeAnythingIsWritten() {
		ConfigModelJson blank = new ConfigModelJson();
		blank.setID("This is the blank test");
		ConfigModel workWiththis = null;
		try {
			workWiththis = rep.createConfigModel(blank, blank.getID(), null);
		} catch (IDAlreadyUsedException e1) {

			fail(e1.getMessage());
		} catch (DatabaseException e1) {

			fail(e1.getMessage());
		}
		ConfigModel model2 = (PersistentConfigModel) workWiththis;
		try {
			assertEquals(model.getAverageRatingThreshold(), 0.0, 0.001);
			assertEquals(model2.getBasicPayment(PlatformIdentity.PyBossa), 0);
			assertEquals(model2.getBasicPayment(PlatformIdentity.MTurk), 0);
			assertEquals(length(model2.getBlockedWorkers()), 0);
			assertEquals(model2.getBudget(), 0);
			assertEquals(model2.getCalibQuestions().length, 0);
			assertEquals(model2.getControlQuestions().length, 0);
			assertEquals(model2.getEvaluationType(), null);
			assertEquals(model2.getExperimentID(), null);
			assertEquals(model2.getExperimentType(), ExperimentType.Undefined);
			assertEquals(model2.getID(), "This is the blank test");
			assertEquals(model2.getMaxCreativeTask(), 0);
			assertEquals(model2.getMaxRatingTask(), 0);
			assertEquals(model2.getPaymentPerTask(PlatformIdentity.PyBossa, TypeOfTask.Creative), 0);
			assertEquals(model2.getPaymentPerTask(PlatformIdentity.MTurk, TypeOfTask.Rating), 0);
			assertEquals(model2.getPaymentPerTask(PlatformIdentity.PyBossa, TypeOfTask.Creative), 0);
			assertEquals(model2.getPaymentPerTask(PlatformIdentity.MTurk, TypeOfTask.Rating), 0);
			assertEquals(model2.getPictureURL(), null);
			assertEquals(length(model2.getQualifications(PlatformIdentity.PyBossa)),0);
			assertEquals(length(model2.getQualifications(PlatformIdentity.MTurk)),0);
			//FIXME
			//assertEquals(length(model2.getRatingOptions()), 0);
			assertEquals(model2.getRatingTaskQuestion(), null);
			assertEquals(model2.getSendCreativeTo(PlatformIdentity.PyBossa), false);
			assertEquals(model2.getSendCreativeTo(PlatformIdentity.MTurk), false);
			assertEquals(model2.getSendRatingTo(PlatformIdentity.PyBossa), false);
			assertEquals(model2.getSendRatingTo(PlatformIdentity.MTurk), false);
			assertEquals(length(model2.getStrategy().entrySet()),0);
			assertEquals(model2.getTaskDescription(), null);
			assertEquals(model2.getTaskQuestion(), null);
			assertEquals(model2.getTaskSourceURL(), null);
			assertEquals(model2.getTaskTags().length, 0);
			assertEquals(model2.getTaskTitle(), null);
			assertEquals(model.getTotalTaskCountThreshold(), 0);
			rep.deleteConfigModel("This is the blank test");
			Logger.debug("Test Case State: DATABASE CLEAN");
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
		
	}
	/**
	 * Trying to get basic payment from an unexpected source shall return 0
	 */
	@Test
	public void invalidGetBasicPayment() {
		try {
			assertEquals(model.getBasicPayment(null), 0);
			assertEquals(model.getBasicPayment(PlatformIdentity.Unspecified), 0);
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	/**
	 * This class reduced write effort.
	 * @author Robin
	 *
	 */
	private class NoChange {
		private ConfigModel model;
		private int oldValuePyBossa;
		private int oldValueMTurk;
		protected NoChange(ConfigModel mod, int ovpb, int ovmt) {
			this.model = mod;
			this.oldValuePyBossa = ovpb;
			this.oldValueMTurk = ovmt;
		}
		protected boolean noChange() {
			try {
				return ((model.getBasicPayment(PlatformIdentity.PyBossa) == oldValuePyBossa)
						&& (model.getBasicPayment(PlatformIdentity.MTurk) == oldValueMTurk));
			} catch (DatabaseException e) {
				fail(e.getMessage());
			}
			return false;
		}
	}
	/**
	 * All invalid calls to setbasicpayment shall be false and not change a thing at the current values.
	 */
	@Test 
	public void invalidSetBasicPayment() {
		try {
			int ovPy = model.getBasicPayment(PlatformIdentity.PyBossa);
			int ovMt = model.getBasicPayment(PlatformIdentity.MTurk);
			NoChange no = new NoChange(model, ovPy, ovMt);
			assertEquals(model.setBasicPayment(null, 2), false);
			assertTrue(no.noChange());
			assertEquals(model.setBasicPayment(null, -21), false);
			assertTrue(no.noChange());
			assertEquals(model.setBasicPayment(PlatformIdentity.PyBossa, -1), false);
			assertTrue(no.noChange());
			assertEquals(model.setBasicPayment(PlatformIdentity.MTurk, -33), false);
			assertTrue(no.noChange());
			assertEquals(model.setBasicPayment(PlatformIdentity.Unspecified, 123), false);
			assertTrue(no.noChange());
			assertEquals(model.setBasicPayment(PlatformIdentity.Unspecified, -1234), false);
			assertTrue(no.noChange());
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
		
	}
	/**
	 * A payment once written should be stored.
	 */
	@Test
	public void sameBasicPayment() {
		try {
			assertTrue(model.setBasicPayment(PlatformIdentity.PyBossa, 120));
			assertEquals(model.getBasicPayment(PlatformIdentity.PyBossa), 120);
			assertTrue(model.setBasicPayment(PlatformIdentity.MTurk, 9001));
			assertEquals(model.getBasicPayment(PlatformIdentity.MTurk), 9001);
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
		
	}
	/**
	 * A budget smaller than zero is no budget at all.
	 * dont store it
	 */
	@Test
	public void invalidBudget() {
		try {
			int oldValue = model.getBudget();
			assertEquals(model.setBudget(-420), false);
			assertEquals(oldValue, model.getBudget());
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	/**
	 * A valid budget shall be stored.
	 */
	@Test
	public void validBudget() {
		
		try {
			assertTrue(model.setBudget(1337));
			assertEquals(model.getBudget(), 1337);
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
		
	}
	/**
	 * Null is not an evaluation type
	 */
	@Test
	public void evaluationTypeNull() {
		try {
			assertFalse(model.setEvaluationType(null));
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	/**
	 * Something valid should be stored. I do not know whether 
	 * bongobongodingodongolongo is valid or not.
	 */
	@Test
	public void validEvaluationType() {
		try {
			assertTrue(model.setEvaluationType("BONGOBONGODINGODONGOLONGO"));
			assertEquals(model.getEvaluationType(), "BONGOBONGODINGODONGOLONGO");
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
		
	}
	/**
	 * Null is not a valid Id
	 */
	@Test
	public void experimentidNull() {
		try {
			assertFalse(model.setExperimentID(null));
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	/**
	 * A valid id should be stored.
	 */
	@Test
	public void experimentidValid() {
		try {
			assertTrue(model.setExperimentID("WillsteEinenApfel"));
			assertEquals(model.getExperimentID(), "WillsteEinenApfel");
			
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
		
	}
	/**
	 * Null is no experiment Type
	 */
	@Test 
	public void invalidExperimentType() {
		try {
			assertFalse(model.setExperimentType(null));
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	/**
	 * I think the pattern of the tests is recognizable
	 */
	@Test 
	public void invalidExperimentTypeNotNull() {
		try {
			assertFalse(model.setExperimentType("Is this the real life"));
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void validExperimentType() {
		try {
			assertTrue(model.setExperimentType(String.valueOf(ExperimentType.Bild)));
			assertEquals(model.getExperimentType(), ExperimentType.Bild);
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test 
	public void getId() {
		try {
			assertEquals(model.getID(), "hallo Welt");
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	/** 
	 * Maximum limits below 0 are useless
	 */
	@Test
	public void invalidMaxTasks() {
		try {
			int oldCT = model.getMaxCreativeTask();
			int oldRT = model.getMaxRatingTask();
			assertFalse(model.setMaxCreativeTask(-911));
			assertFalse(model.setMaxRatingTask(-600000));
			assertEquals(oldCT, model.getMaxCreativeTask());
			assertEquals(oldRT, model.getMaxRatingTask());
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
		
	}
	/**
	 * Valid limits should be stored
	 */
	@Test
	public void validMaxTasks() {
		try {
			assertTrue(model.setMaxCreativeTask(42));
			assertTrue(model.setMaxRatingTask(4256723)); // is a prime number, believe it or not
			assertEquals(model.getMaxCreativeTask(), 42);
			assertEquals(model.getMaxRatingTask(), 4256723);
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
		
	}

	@Test
	public void invalidSetPaymentPerTask() {
		try {
			int oldPyCr = model.getPaymentPerTask(PlatformIdentity.PyBossa, TypeOfTask.Creative);
			int oldPyRt = model.getPaymentPerTask(PlatformIdentity.PyBossa, TypeOfTask.Rating);
			int oldMtCr = model.getPaymentPerTask(PlatformIdentity.MTurk, TypeOfTask.Creative);
			int oldMtRt = model.getPaymentPerTask(PlatformIdentity.MTurk, TypeOfTask.Rating);
			assertFalse(model.setPaymentPerTask(null, null, -1));
			assertFalse(model.setPaymentPerTask(null, null, 2));
			assertFalse(model.setPaymentPerTask(null, TypeOfTask.Creative, -1));
			assertFalse(model.setPaymentPerTask(null, TypeOfTask.Creative, 2));
			assertFalse(model.setPaymentPerTask(null, TypeOfTask.Rating, -231));
			assertFalse(model.setPaymentPerTask(null, TypeOfTask.Rating, 23));
			assertFalse(model.setPaymentPerTask(PlatformIdentity.Unspecified, null, -1));
			assertFalse(model.setPaymentPerTask(PlatformIdentity.Unspecified, TypeOfTask.Creative, -1));
			assertFalse(model.setPaymentPerTask(PlatformIdentity.Unspecified, TypeOfTask.Rating, -123));
			assertFalse(model.setPaymentPerTask(PlatformIdentity.Unspecified, null, 4040));
			assertFalse(model.setPaymentPerTask(PlatformIdentity.Unspecified, TypeOfTask.Creative, 9001));
			assertFalse(model.setPaymentPerTask(PlatformIdentity.Unspecified, TypeOfTask.Rating, 40));
			assertFalse(model.setPaymentPerTask(PlatformIdentity.MTurk, null, -1));
			assertFalse(model.setPaymentPerTask(PlatformIdentity.MTurk, null, 708));
			assertFalse(model.setPaymentPerTask(PlatformIdentity.MTurk, TypeOfTask.Creative, -12));
			assertFalse(model.setPaymentPerTask(PlatformIdentity.MTurk, TypeOfTask.Rating, -123));
			assertFalse(model.setPaymentPerTask(PlatformIdentity.PyBossa, null, -333));
			assertFalse(model.setPaymentPerTask(PlatformIdentity.PyBossa, null, 70000));
			assertFalse(model.setPaymentPerTask(PlatformIdentity.PyBossa, TypeOfTask.Creative, -1232313));
			assertFalse(model.setPaymentPerTask(PlatformIdentity.PyBossa, TypeOfTask.Rating, -3245));
			assertEquals(oldPyCr, model.getPaymentPerTask(PlatformIdentity.PyBossa, TypeOfTask.Creative));
			assertEquals(oldPyRt, model.getPaymentPerTask(PlatformIdentity.PyBossa, TypeOfTask.Rating));
			assertEquals(oldMtCr, model.getPaymentPerTask(PlatformIdentity.MTurk, TypeOfTask.Creative));
			assertEquals(oldMtRt, model.getPaymentPerTask(PlatformIdentity.MTurk, TypeOfTask.Rating));
			
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void validSetPaymentPerTask() {
		try {
			assertTrue(model.setPaymentPerTask(PlatformIdentity.MTurk, TypeOfTask.Creative, 12));
			assertTrue(model.setPaymentPerTask(PlatformIdentity.MTurk, TypeOfTask.Rating, 13));
			assertTrue(model.setPaymentPerTask(PlatformIdentity.PyBossa, TypeOfTask.Creative, 14));
			assertTrue(model.setPaymentPerTask(PlatformIdentity.PyBossa, TypeOfTask.Rating, 15));
			assertEquals(model.getPaymentPerTask(PlatformIdentity.MTurk, TypeOfTask.Creative), 12);
			assertEquals(model.getPaymentPerTask(PlatformIdentity.MTurk, TypeOfTask.Rating), 13);
			assertEquals(model.getPaymentPerTask(PlatformIdentity.PyBossa, TypeOfTask.Creative), 14);
			assertEquals(model.getPaymentPerTask(PlatformIdentity.PyBossa, TypeOfTask.Rating), 15);
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void nullAsPicture() {
		try {
			assertFalse(model.setPictureURL(null));
			assertEquals(model.getPictureURL(), null);
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void invalidurlAsPicture() {
		String url = "Is this just fantasy";
		
		try {
			assertFalse(model.setPictureURL(url));
			assertEquals(model.getPictureURL(), null);
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void validUrlAsPicture() {
		String url = "http://www.crowdcrafting.org";
		try {
			assertTrue(model.setPictureURL(url));
			assertEquals(model.getPictureURL(), "http://www.crowdcrafting.org");
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void invalidQualifications() {
		try {
			ArrayList<String> templateQualifications = new ArrayList<String>();
			templateQualifications.add("This Qualification should not be present");
			assertFalse(model.setQualifications(null, null));
			assertFalse(model.setQualifications(null, PlatformIdentity.PyBossa));
			assertFalse(model.setQualifications(null, PlatformIdentity.MTurk));
			assertFalse(model.setQualifications(null, PlatformIdentity.Unspecified));
			assertFalse(model.setQualifications(templateQualifications, null));
			assertFalse(model.setQualifications(templateQualifications, PlatformIdentity.Unspecified));
			assertEquals(length(model.getQualifications(PlatformIdentity.PyBossa)), 0);
			assertEquals(length(model.getQualifications(PlatformIdentity.MTurk)), 0);
			
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
		
	}
	@Test (expected = DatabaseException.class)
	public void qualificationsFromUnspecified() throws DatabaseException {
		model.getQualifications(PlatformIdentity.Unspecified);
	}
	@Test
	public void validQualifications() {
		ArrayList<String> qual = new ArrayList<>();
		qual.add("mullewapp");
		try {
			
			assertTrue(model.setQualifications(qual, PlatformIdentity.MTurk));
			assertTrue(model.setQualifications(qual, PlatformIdentity.PyBossa));

			Iterable<String> py = model.getQualifications(PlatformIdentity.PyBossa);
			Iterable<String> mt = model.getQualifications(PlatformIdentity.MTurk);
			assertEquals(length(py), 1);
			assertEquals(length(mt), 1);
			for(String s : py) {
				assertEquals(s, "mullewapp");
			}
			for(String s : mt) {
				assertEquals(s, "mullewapp");
			}
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	
	//FIXME
//	@Test
//	public void noCrashWhenRatingOptions() {
//		try {
//			Iterable<RatingOption> iter = model.getRatingOptions();
//			assertEquals(length(iter), 0);
//			for(RatingOption r : iter) {
//				r.getValue();
//			}
//		} catch (DatabaseException e) {
//			fail(e.getMessage());
//		}
//		
//	}
	
	//FIXME
//	@Test
//	public void validRatingOptionAdded() {
//		Map<String, Float> map = new HashMap<>();
//		map.put("Hallo", (float) 1.0);
//		
//		try {
//			assertTrue(model.setRatingOptions(map));
//			Iterable<RatingOption> iter = model.getRatingOptions();
//			for(RatingOption r :iter) {
//				System.out.println(r.getText());
//				assertEquals(r.getValue(), 1.0, 0.0001);
//				assertEquals(r.getText(), "Hallo");
//			}
//		} catch (DatabaseException e) {
//			fail(e.getMessage());
//		}
//		
//	}
	@Test
	public void putRatingQuestion() {
		try {
			assertTrue(model.setRatingTaskQuestion("Wie viele Unzen bist du alt?"));
			assertEquals(model.getRatingTaskQuestion(), "Wie viele Unzen bist du alt?");
			
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
		
	}
	@Test
	public void noInvalidSends() {
		try {
			assertFalse(model.getSendCreativeTo(null));
			assertFalse(model.getSendRatingTo(null));
			assertFalse(model.getSendCreativeTo(PlatformIdentity.Unspecified));
			assertFalse(model.getSendRatingTo(PlatformIdentity.Unspecified));
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
		
	}
	@Test 
	public void invalidSetSends() {
		try {
			boolean ctp = model.getSendCreativeTo(PlatformIdentity.PyBossa);
			boolean rtp = model.getSendRatingTo(PlatformIdentity.PyBossa);
			boolean ctm = model.getSendCreativeTo(PlatformIdentity.MTurk);
			boolean rtm = model.getSendRatingTo(PlatformIdentity.MTurk);
			assertFalse(model.setSendCreativeTo(null, true));
			assertFalse(model.setSendRatingTo(null, false));
			assertFalse(model.setSendCreativeTo(PlatformIdentity.Unspecified, false));
			assertFalse(model.setSendRatingTo(PlatformIdentity.Unspecified, true));
			assertEquals(ctp, model.getSendCreativeTo(PlatformIdentity.PyBossa));
			assertEquals(rtp, model.getSendRatingTo(PlatformIdentity.PyBossa));
			assertEquals(ctm, model.getSendCreativeTo(PlatformIdentity.MTurk));
			assertEquals(rtm, model.getSendRatingTo(PlatformIdentity.MTurk));
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void validSetSends() {
		try {
			assertTrue(model.setSendCreativeTo(PlatformIdentity.PyBossa, true));
			assertTrue(model.setSendRatingTo(PlatformIdentity.PyBossa, true));
			assertTrue(model.setSendCreativeTo(PlatformIdentity.MTurk, true));
			assertTrue(model.setSendRatingTo(PlatformIdentity.MTurk, true));
			assertTrue(model.getSendCreativeTo(PlatformIdentity.PyBossa));
			assertTrue(model.getSendRatingTo(PlatformIdentity.PyBossa));
			assertTrue(model.getSendCreativeTo(PlatformIdentity.MTurk));
			assertTrue(model.getSendRatingTo(PlatformIdentity.MTurk));
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void emtpyStrategy() {
		try {
			assertEquals(length(model.getStrategy().entrySet()),0);
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test 
	public void nullStrategy() {
		try {
			assertFalse(model.setStrategy(null));
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test 
	public void validStrategyStupidInputs() {
		Map<String, String> map = new HashMap<>();
		map.put(null, null);
		map.put(null, "hallo");
		map.put("halloow", null);
		try {
			assertTrue(model.setStrategy(map)); // Invalid entries should be ignored, still the rest might be good, so no error termination.
			assertEquals(length(model.getStrategy().entrySet()), 0);
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void validStrategy() {
		Map<String, String> map = new HashMap<>();
		map.put("mugule", "mugule2");
		map.put("aber", "hatschi");
		try {
			assertTrue(model.setStrategy(map));
			Map<String, String> testMap = model.getStrategy();
			for(Map.Entry<String, String> entry : testMap.entrySet()) {
				if(entry.getKey().equals("mugule")) {
					assertEquals(entry.getValue(), "mugule2");

				} else if (entry.getKey().equals("aber")) {
					assertEquals(entry.getValue(), "hatschi");

				}
				else {
					fail("Not the map i'm looking for");
				}
			}
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void invalidDescriptions() {
		try {
			assertFalse(model.setTaskDescription(null));
			assertFalse(model.setTaskQuestion(null));
			assertFalse(model.setTaskSourceURL(null));
			assertFalse(model.setTaskTitle(null));
			assertFalse(model.setTaskSourceURL("Ich bin keine URL"));
			assertEquals(model.getTaskDescription(), null);
			assertEquals(model.getTaskQuestion(), null);
			assertEquals(model.getTaskSourceURL(), null);
			assertEquals(model.getTaskTitle(), null);
			
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void validDescriptions() {
		try {
			assertTrue(model.setTaskDescription("Alles sind Unzen"));
			assertTrue(model.setTaskQuestion("Wie viele Unzen bist du groß?"));
			assertTrue(model.setTaskSourceURL("http://www.wikipedia.de/warum-unzen-die-beste-einheit-sind"));
			assertTrue(model.setTaskTitle("Rechne mit Unzen statt mit Meilen"));
			
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void invalidTaskTag() {
		try {
			assertFalse(model.addTaskTag(null));
			assertEquals(model.getTaskTags().length, 0);
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void validTaskTag() {
		try {
			model.addTaskTag("ICH bin ein Tasktag, 3x so lecker wie Menschen");
			model.addTaskTag("AHHHH, Second Gnome");
			String[] tags = model.getTaskTags();
			assertEquals(tags[0],"ICH bin ein Tasktag, 3x so lecker wie Menschen");
			assertEquals(tags[1], "AHHHH, Second Gnome");
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void noObjectNoLength() {
		try {
			assertEquals(length(model.getBlockedWorkers()), 0);
			assertEquals(model.getCalibQuestions().length,0);
			assertEquals(model.getControlQuestions().length,0);
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
		
	}
	@Test
	public void addCalibQuestion() {
		try {
			PersistentCalibrationQuestionRepo quest = new PersistentCalibrationQuestionRepo();
			MutableCalibrationQuestion question = quest.createCalibrationQuestion("Diese Frage, ich darf eigenständig IDS vergeben");
			model.addCalibQuestion(question);
			CalibrationQuestion[] myQ = model.getCalibQuestions();
			assertEquals(myQ.length, 1);
			assertEquals(myQ[0].getID(), question.getID());
			quest.deleteCalibrationQuestion(question.getID());
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	//TODO i need a validated experiment test
//	@Test
//	public void addControlQuestion() {
//		try {
//			MutableExperiment e = exprep.createExperiment("HALLO");
//			
//			model.addControlQuestion(question);
//		} catch (DatabaseException e) {
//			fail(e.getMessage());
//		}
//	}
	@Test
	public void MaxTask() {
		try {
			model.setAverageRatingThreshold(123.0F);
			model.setTotalTaskCountThreshold(90);
			assertEquals(model.getAverageRatingThreshold(), 123.0F, 0.001);
			assertEquals(model.getTotalTaskCountThreshold(), 90);
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	@Test
	public void addBlockedWorker() {
		try {
			MutableWorker worker = workerrep.findWorker("tutti", "frutti");
			model.addBlockedWorker(worker);
			assertEquals(length(model.getBlockedWorkers()), 1);
			for(Worker w : model.getBlockedWorkers()) {
				Logger.debug(w.getID() + " " + worker.getID());
				assertEquals(w.getID(), worker.getID());
			}
			workerrep.anonymizeWorker(worker.getID());
		} catch (DatabaseException e) {
			fail(e.getMessage());
		}
	}
	

	
	

}

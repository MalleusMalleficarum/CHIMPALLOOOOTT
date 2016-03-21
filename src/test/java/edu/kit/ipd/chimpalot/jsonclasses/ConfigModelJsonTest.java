package edu.kit.ipd.chimpalot.jsonclasses;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.kit.ipd.creativecrowd.crowdplatform.PlatformIdentity;
import edu.kit.ipd.creativecrowd.readablemodel.ConfigModelMock;
import edu.kit.ipd.creativecrowd.readablemodel.ExperimentType;
import edu.kit.ipd.creativecrowd.readablemodel.TypeOfTask;

/**
 * 
 * @author Thomas Friedel
 */
public class ConfigModelJsonTest {
	private ObjectMapper jacksonObjectMapper = new ObjectMapper();
	
	/**
	 * Tests if deserialization from json works as expected (tests setters, jackson uses those) 
	 * @throws Exception
	 */
	@Test
	public void jsonDeserializationTest() throws Exception {
		ConfigModelJson config =
				jacksonObjectMapper.readValue(getClass().getResourceAsStream("/validconfig.json"), ConfigModelJson.class);	
		assertIsEqualToValid(config);
	}
	
	/**
	 * Tests if serialization to json works as expected (tests getters, which are not annotated with JsonIgnore)
	 * @throws Exception
	 */
	@Test
	public void jsonSerializeTest() throws Exception {
		ConfigModelJson config =
				jacksonObjectMapper.readValue(getClass().getResourceAsStream("/validconfig.json"), ConfigModelJson.class);
		String serialized = jacksonObjectMapper.writeValueAsString(config);
		assertTrue(serialized.contains("taskQuestion"));
		assertTrue(serialized.contains("controlQuestions"));
		ConfigModelJson after = jacksonObjectMapper.readValue(serialized, ConfigModelJson.class);
		assertIsEqualToValid(after);
	}
	
	/**
	 * Tests the constructor which creates a ConfigModelJson from an existing ConfigModel
	 * @throws Exception
	 */
	@Test
	public void contructorTest() throws Exception {
		ConfigModelJson config = new ConfigModelJson(ConfigModelMock.validConfig());
		assertIsEqualToValid(config);
	}
	
	private void assertIsEqualToValid(ConfigModelJson config) throws Exception {
		//Id is ignored
		assertEquals("What does Chimpalot stand for?", config.getTaskQuestion());
		assertEquals("The question was: What does Chimpalot stand for?", config.getRatingTaskQuestion());
		assertEquals("The description", config.getTaskDescription());
		assertEquals("Chimpalot", config.getTaskTitle());
		assertEquals(2, config.getTaskTags().length);
		assertEquals("tag", config.getTaskTags()[0]);
		assertEquals("chimpalot", config.getTaskTags()[1]);
		assertEquals("http://imgs.xkcd.com/comics/it_begins.png", config.getPictureURL());
		assertEquals("http://xkcd.com/1656/", config.getTaskSourceURL());
		assertEquals(1, config.getCalibQuestions().length);
		assertEquals( "idofcalibquestion", config.getCalibQuestions()[0].getID());
		assertEquals(1, config.getCalibQuestionsJson().length);
		assertEquals("idofcalibquestion", config.getCalibQuestionsJson()[0]);
		assertEquals(1, config.getControlQuestions().length);
		//ControlQuestionJson will be tested in ControlQuestionJsonTest
		assertEquals(1, config.getMaxCreativeTask());
		assertEquals(1, config.getMaxRatingTask());
		assertEquals(500, config.getBudget());
		assertEquals(true, config.getSendCreativeTo(PlatformIdentity.MTurk));
		assertEquals(true, config.getSendCreativeTo(PlatformIdentity.PyBossa));
		assertEquals(true, config.getSendRatingTo(PlatformIdentity.MTurk));
		assertEquals(true, config.getSendRatingTo(PlatformIdentity.PyBossa));
		assertEquals(1, config.getBasicPayment(PlatformIdentity.MTurk));
		assertEquals(config.getBasicPayment(PlatformIdentity.MTurk), config.getBasicPaymentMTurk());
		assertEquals(1, config.getPaymentPerTask(PlatformIdentity.MTurk, TypeOfTask.Creative));
		assertEquals(2, config.getPaymentPerTask(PlatformIdentity.MTurk, TypeOfTask.Rating));
		assertEquals(3, config.getPaymentPerTask(PlatformIdentity.PyBossa, TypeOfTask.Creative));
		assertEquals(config.getPaymentPerTask(PlatformIdentity.PyBossa, TypeOfTask.Rating), config.getPaymentPerTaskRaPyBossa());
		assertEquals("genericRatingView", config.getEvaluationType());
		assertEquals(2, config.getStrategy().size());
		assertEquals("edu.kit.ipd.creativecrowd.operations.strategies.BonusAPOC", config.getStrategy().get("apoc_class"));
		assertEquals("5", config.getStrategy().get("apoc_bonusPool"));
		assertEquals(false, config.getQualificationsPyBossa().iterator().hasNext());
		assertEquals("notdrunk", config.getQualificationsMTurk().iterator().next());
		//RatingOption will be tested in RatingOptionJsonTest
		assertEquals(ExperimentType.Text, config.getExperimentType());
		assertEquals("badworkersid", config.getBlockedWorkers().iterator().next().getID());
		assertEquals("badworkersid", config.getBlockedWorkersJson().iterator().next());
		assertEquals(0.0, config.getTotalTaskCountThreshold(), 0.0);
		assertEquals(0.0, config.getAverageRatingThreshold(), 0.0);
	}

}

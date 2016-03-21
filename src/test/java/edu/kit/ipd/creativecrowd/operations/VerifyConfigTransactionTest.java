package edu.kit.ipd.creativecrowd.operations;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import edu.kit.ipd.chimpalot.jsonclasses.ConfigModelJson;
import edu.kit.ipd.chimpalot.util.Logger;
import edu.kit.ipd.creativecrowd.crowdplatform.PlatformIdentity;
import edu.kit.ipd.creativecrowd.readablemodel.ConfigModel;
import edu.kit.ipd.creativecrowd.readablemodel.ConfigModelMock;
import edu.kit.ipd.creativecrowd.readablemodel.TypeOfTask;

/**
 * Tests VerifyConfigTransaction
 *
 * @author Thomas Friedel
 */
public class VerifyConfigTransactionTest {
	
	/**
	 * Tests run(...)
	 * @throws Exception
	 */
	@Test
	public void validConfigTest() throws Exception {
		ConfigModel config = ConfigModelMock.validConfig();
		VerifyConfigTransaction transaction = new VerifyConfigTransaction();
		List<String> result = transaction.run(config);
		assertEquals(true, result.isEmpty());
	}
	
	/**
	 * Tests run(...)
	 * @throws Exception
	 */
	@Test
	public void inValidConfigTest() throws Exception {
		ConfigModelJson config = ConfigModelMock.validConfig();
		config.setTaskQuestion("");
		config.setTaskDescription("");
		config.setTaskTitle("");
		config.setPictureURL("thisisnotanurl");
		config.setMaxRatingTask(-5);
		config.setMaxCreativeTask(0);
		config.setSendCreativeToMTurk(false);
		config.setSendCreativeToPyBossa(false);
		config.setSendRatingToPyBossa(false);
		config.setBasicPaymentMTurk(0);
		config.setPaymentPerTaskCrMTurk(0);
		config.setPaymentPerTaskCrPyBossa(1000000);
		config.setPaymentPerTaskRaMTurk(0);
		config.setEvaluationType("randomstring");
		config.setExperimentType(null);
		VerifyConfigTransaction transaction = new VerifyConfigTransaction();
		List<String> result = transaction.run(config);
		
		assertEquals(true, result.contains("taskQuestion"));
		assertEquals(true, result.contains("taskDescription"));
		assertEquals(true, result.contains("taskTitle"));
		assertEquals(true, result.contains("pictureURL"));
		assertEquals(true, result.contains("maxRatingTask"));
		assertEquals(true, result.contains("maxCreativeTask"));
		assertEquals(true, result.contains("sendCreativeToMTurk"));
		assertEquals(true, result.contains("sendCreativeToPyBossa"));
		assertEquals(true, result.contains("basicPaymentMTurk"));
		assertEquals(true, result.contains("paymentPerTaskRaMTurk"));
		assertEquals(true, result.contains("paymentPerTaskCrPyBossa"));
		assertEquals(true, result.contains("evaluationType"));
		assertEquals(true, result.contains("experimentType"));
		assertEquals(13, result.size());
	}
	
	/**
	 * Tests the compare method
	 * @throws Exception
	 */
	@Test
	public void validReplacementTest() throws Exception {
		ConfigModel old = ConfigModelMock.validConfig();
		ConfigModelJson replacement = ConfigModelMock.validConfig();
		
		replacement.setBudget(old.getBudget() + 1);
		replacement.setRatingTaskQuestion("You probably don't care.");
		replacement.setPictureURL("");
		replacement.setTaskSourceURL("");
		replacement.setAverageRatingThreshold(0);
		replacement.setSendRatingToMTurk(false);
		Map<String, String> strategies = new HashMap<String, String>(old.getStrategy());
		strategies.put("aqic_class",
				"edu.kit.ipd.creativecrowd.operations.strategies.AverageRating");
		replacement.setStrategy(strategies);
		
		VerifyConfigTransaction transaction = new VerifyConfigTransaction();
		List<String> result = transaction.compare(replacement, old);
		assertEquals(true, result.isEmpty());
	}
	
	/**
	 * Tests compare(..)
	 * @throws Exception
	 */
	@Test
	public void invalidReplacementTest() throws Exception {
		Logger.debug("invalidReplacementTest:");
		ConfigModel old = ConfigModelMock.validConfig();
		ConfigModelJson replacement = ConfigModelMock.validConfig();
		
		replacement.setSendRatingToMTurk(false);
		replacement.setSendRatingToPyBossa(false);
		replacement.setTaskSourceURL("");
		replacement.setTaskDescription("This description can change.");
		replacement.setTaskTitle("Stop changing the HIT spec!");
		replacement.setTaskTags(new String[old.getTaskTags().length]);
		replacement.setBasicPaymentMTurk(old.getBasicPayment(PlatformIdentity.MTurk) + 2);
		replacement.setPaymentPerTaskRaPyBossa(
				old.getPaymentPerTask(PlatformIdentity.PyBossa, TypeOfTask.Rating) + 1);
		Map<String, String> strategies = new HashMap<String, String>();
		strategies.put("tacc_n", "1");
		replacement.setStrategy(strategies);

		VerifyConfigTransaction transaction = new VerifyConfigTransaction();
		List<String> result = transaction.compare(replacement, old);
		for (String s : result) {
			Logger.debug(" " + s);
		}
		Logger.debug("---------");
		
		assertEquals(true, result.contains("sendRatingToMTurk"));
		assertEquals(true, result.contains("sendRatingToPyBossa"));
		assertEquals(true, result.contains("taskSourceURL"));
		assertEquals(true, result.contains("taskTitle"));
		assertEquals(true, result.contains("taskTags"));
		assertEquals(true, result.contains("basicPaymentMTurk"));
		assertEquals(true, result.contains("paymentPerTaskRaPyBossa"));
		assertEquals(true, result.contains("strategy"));
		
		assertEquals(8, result.size());
	}

}

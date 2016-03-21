package edu.kit.ipd.chimpalot.jsonclasses;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.kit.ipd.creativecrowd.mutablemodel.MutablePossibleCalibrationAnswer;
import edu.kit.ipd.creativecrowd.readablemodel.CalibrationQuestion;

/**
 * 
 * @author Thomas Friedel
 */
public class CalibrationQuestionJsonTest {
	private final ObjectMapper jacksonObjectMapper = new ObjectMapper();
	
	private final String jsonText = 
		              "{"
		+ "				  \"id\": \"calibrationqid\","
		+ "				  \"question\": \"How old are you?\","
		+ "				  \"possibleAnswers\": ["
		+ "				    {"
		+ "				      \"id\": \"answer1\","
		+ "				      \"text\": \"Younger than 18\","
		+ "				      \"istrue\": false"
		+ "				    },"
		+ "				    {"
		+ "				      \"id\": \"answer2\","
		+ "				      \"text\": \"18 or older\","
		+ "				      \"istrue\": true"
		+ "				    }"
		+ "				  ]"
		+ "				}";
	
	@Test
	public void deserializationTest() throws Exception {
		CalibrationQuestionJson question = this.jacksonObjectMapper.readValue(jsonText, CalibrationQuestionJson.class);
		assertEqualToJsonText(question);
	}
	
	@Test
	public void serializationTest() throws Exception {
		CalibrationQuestionJson question = this.jacksonObjectMapper.readValue(jsonText, CalibrationQuestionJson.class);
		String json = this.jacksonObjectMapper.writeValueAsString(question);
		CalibrationQuestionJson after = this.jacksonObjectMapper.readValue(json, CalibrationQuestionJson.class);
		assertEqualToJsonText(after);
	}
	
	@Test
	public void contructorTest() throws Exception {
		CalibrationQuestionJson question = this.jacksonObjectMapper.readValue(jsonText, CalibrationQuestionJson.class);
		CalibrationQuestion qAsInterface = question;
		CalibrationQuestionJson after = new CalibrationQuestionJson(qAsInterface);
		assertEqualToJsonText(after);
	}
	
	
	private static void assertEqualToJsonText(CalibrationQuestionJson question) throws Exception {
		assertEquals("calibrationqid", question.getID());
		assertEquals("How old are you?", question.getQuestion());
		int n = 0;
		Iterator<? extends MutablePossibleCalibrationAnswer> it = question.getPossibleAnswers().iterator();
		while (it.hasNext()) {
			n++;
			MutablePossibleCalibrationAnswer ans = it.next();
			if (n == 1) {
				assertEquals("answer1", ans.getID());
				assertEquals("Younger than 18", ans.getText());
				assertEquals(false, ans.getIsTrue());
			}
			if (n == 2) {
				assertEquals("answer2", ans.getID());
				assertEquals("18 or older", ans.getText());
				assertEquals(true, ans.getIsTrue());
			}
		}
		assertEquals(2, n);
		assertEquals(false, question.getCalibrationAnswers().iterator().hasNext());
	}

}

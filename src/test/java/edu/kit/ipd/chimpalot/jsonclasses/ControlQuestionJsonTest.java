package edu.kit.ipd.chimpalot.jsonclasses;


import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.kit.ipd.creativecrowd.mutablemodel.MutablePossibleControlAnswer;
import edu.kit.ipd.creativecrowd.readablemodel.ControlQuestion;

public class ControlQuestionJsonTest {
	
	private final ObjectMapper jacksonObjectMapper = new ObjectMapper();
	
	private final String jsonText =
			  "{"
			+ " \"id\": \"ancontrolquestion\","
			+ " \"question\": \"What is the answer to life, the universe and everything?\","
			+ " \"possibleAnswers\": [  {"
			+ "          \"id\": \"thetrueanswer\","
			+ "          \"text\": \"42\","
			+ "          \"isTrue\": true"
			+ "        },"
			+ "        {"
			+ "          \"id\": \"whatisthat\","
			+ "          \"text\": \"41\","
			+ "          \"isTrue\": false"
			+ "        }"
			+ "      ]"
			+ "}";
	
	
	@Test
	public void DeserializationTest() throws Exception {
		ControlQuestionJson question = this.jacksonObjectMapper.readValue(jsonText, ControlQuestionJson.class);
		assertEqualToJsonText(question);
	}
	
	@Test
	public void SerializationTest() throws Exception {
		ControlQuestionJson question = this.jacksonObjectMapper.readValue(jsonText, ControlQuestionJson.class);
		String json = this.jacksonObjectMapper.writeValueAsString(question);
		ControlQuestionJson after = this.jacksonObjectMapper.readValue(json, ControlQuestionJson.class);
		assertEqualToJsonText(after);
	}
	
	@Test
	public void contructorTest() throws Exception {
		ControlQuestionJson question = this.jacksonObjectMapper.readValue(jsonText, ControlQuestionJson.class);
		ControlQuestion qAsInterface = question;
		ControlQuestionJson after = new ControlQuestionJson(qAsInterface);
		assertEqualToJsonText(after);
	}
	
	private void assertEqualToJsonText(ControlQuestionJson question) throws Exception {
		assertEquals("ancontrolquestion", question.getID());
		assertEquals("What is the answer to life, the universe and everything?", question.getQuestion());
		int n = 0;
		Iterator<? extends MutablePossibleControlAnswer> it = question.getPossibleAnswers().iterator();
		while (it.hasNext()) {
			n++;
			MutablePossibleControlAnswer ans = it.next();
			if (n == 1) {
				assertEquals("thetrueanswer", ans.getID());
				assertEquals("42", ans.getText());
				assertEquals(true, ans.getIsTrue());
			}
			if (n == 2) {
				assertEquals("whatisthat", ans.getID());
				assertEquals("41", ans.getText());
				assertEquals(false, ans.getIsTrue());
			}
		}
		assertEquals(2, n);
	}

}

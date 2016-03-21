package edu.kit.ipd.creativecrowd.controller;

import static org.junit.Assert.*;

import org.junit.*;
public class AnswerSpecFromConfigTest {
	@Test
	public void testCreation(){
		AnswerSpecFromConfig asfc = new AnswerSpecFromConfig("id", "answer");
		assertEquals("id", asfc.getCreativeTaskID());
		assertEquals("answer", asfc.getText());
	}
}

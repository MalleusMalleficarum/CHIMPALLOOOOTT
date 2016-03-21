package edu.kit.ipd.chimpalot.jsonclasses;
import edu.kit.ipd.creativecrowd.connector.AnswerSpec;

/**
 * (originally creativecrowd.controller.AnswerSpecFromConfig)
 * 
 * @see edu.kit.ipd.creativecrowd.connector.AnswerSpec 
 * @author unknown
 */
public class AnswerSpecFromConfig implements AnswerSpec {
	
	private String creativeTaskID;
	private String text;
	
	public AnswerSpecFromConfig(String id, String answertext) {
		creativeTaskID = id;
		text = answertext;
	}

	@Override
	public String getCreativeTaskID() {
		return creativeTaskID;
	}

	@Override
	public String getText() {
		return text;
	}

}

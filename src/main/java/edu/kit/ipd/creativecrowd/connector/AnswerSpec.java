package edu.kit.ipd.creativecrowd.connector;

/**
 * the specification of an answer. Contains text and the ID of the corresponding CreativeTask
 * 
 * @author simon
 */
public interface AnswerSpec {
	/**
	 * @return the ID of the corresponding CreativeTask
	 */
	String getCreativeTaskID();

	/**
	 * @return the text of the answer
	 */
	String getText();
}

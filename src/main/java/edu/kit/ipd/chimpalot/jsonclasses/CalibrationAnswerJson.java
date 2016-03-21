/**
 * 
 */
package edu.kit.ipd.chimpalot.jsonclasses;

import java.util.Map;

/**
 * class for spring json handling
 * @author basti
 *
 */
public class CalibrationAnswerJson {

	private String workerid;
	
	private Map<String, String> answers;

	/**
	 * @param workerid
	 * @param answers
	 */
	public CalibrationAnswerJson(String workerid, Map<String, String> answers) {
		super();
		this.workerid = workerid;
		this.answers = answers;
	}

	/**
	 * @return the workerid
	 */
	public String getWorkerid() {
		return workerid;
	}

	/**
	 * @param workerid the workerid to set
	 */
	public void setWorkerid(String workerid) {
		this.workerid = workerid;
	}

	/**
	 * @return the answers
	 */
	public Map<String, String> getAnswers() {
		return answers;
	}

	/**
	 * @param answers the answers to set
	 */
	public void setAnswers(Map<String, String> answers) {
		this.answers = answers;
	}
	
}

/**
 * 
 */
package edu.kit.ipd.creativecrowd.view;

/**
 * @author basti
 *
 */
public class AnsData {

	private String answer;
	
	private boolean value;

	/**
	 * @param answer
	 * @param value
	 */
	public AnsData(String answer, boolean value) {
		super();
		this.answer = answer;
		this.value = value;
	}

	/**
	 * @return the answer
	 */
	public String getAnswer() {
		return answer;
	}

	/**
	 * @param answer the answer to set
	 */
	public void setAnswer(String answer) {
		this.answer = answer;
	}

	/**
	 * @return the value
	 */
	public boolean getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(boolean value) {
		this.value = value;
	}
}

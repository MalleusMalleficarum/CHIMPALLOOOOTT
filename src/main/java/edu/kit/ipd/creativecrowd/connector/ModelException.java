package edu.kit.ipd.creativecrowd.connector;

/**
 * thrown if there is any exception thrown in the model layer
 * 
 * @author simon
 */
public class ModelException extends Throwable {
	/**
	 * constructor
	 * 
	 * @param message the exception message
	 */
	public ModelException(String message) {
		super(message);
	}
}

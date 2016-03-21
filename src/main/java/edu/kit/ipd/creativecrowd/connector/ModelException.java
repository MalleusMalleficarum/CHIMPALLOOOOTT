package edu.kit.ipd.creativecrowd.connector;

/**
 * thrown if there is any exception thrown in the model layer
 * 
 * @author simon
 */
public class ModelException extends Exception {
	
	/**
	 * serial.
	 */
	private static final long serialVersionUID = -9203208883786027415L;

	/**
	 * constructor
	 * 
	 * @param message the exception message
	 */
	public ModelException(String message) {
		super(message);
	}
	
	/**
	 * 
	 * @param message the exception message
	 * @param cause the exception that caused this exception. Can be {@code null}.
	 */
	public ModelException(String message, Throwable cause) {
		super(message, cause);
	}
}

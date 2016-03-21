package edu.kit.ipd.creativecrowd.crowdplatform;

/**
 * @author Tobias H
 *An Exception which gets thrown if there
 *are any problems connecting to MTurk
 */
public class ConnectionFailedException extends Exception{

	
	private static final long serialVersionUID = 840634760490236541L;
	/**
	 * Default constructor does nothing.
	 */
	public ConnectionFailedException(){
	}

	/**
	 * @param message The error message why the connection failed
	 */
	public ConnectionFailedException(String message) {
		super(message);
	}

}

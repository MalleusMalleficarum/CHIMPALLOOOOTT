package edu.kit.ipd.creativecrowd.operations;

/**
 * This exception is thrown when trying to load a strategy class which doesn't exist
 * 
 * @author Anika
 */
public class StrategyNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public StrategyNotFoundException(String msg, Exception e) {
		super(msg, e);
	}

}

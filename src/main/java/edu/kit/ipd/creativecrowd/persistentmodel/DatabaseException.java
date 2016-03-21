package edu.kit.ipd.creativecrowd.persistentmodel;

// TODO: Auto-generated Javadoc

/**
 * An Exception which can be raised when an internal Database/Persistent Storage/SQL Syntax Error occurs
 *
 * @author Philipp + Alexis
 */
public class DatabaseException extends Exception {

	/**
	 * The serial version id of the class database exception
	 */
	private static final long serialVersionUID = 8772746855912400537L;

	/**
	 * Instantiates a new database exception with a specific message
	 *
	 * @param message the specific error message of the exception
	 */
	public DatabaseException(String message) {
		super(message);
	}

}

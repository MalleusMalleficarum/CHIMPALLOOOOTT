/**
 * 
 */
package edu.kit.ipd.creativecrowd.persistentmodel;

//TODO Handle this without just inheriting from DatabaseException (or remove it if it appears unnecessary)
/**
 * Thrown if a expected or requested ID or name is not found in the database.
 * 
 * @author Thomas Friedel
 *
 */
public class IDNotFoundException extends DatabaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6421082298577819130L;
	
	public IDNotFoundException(String message) {
		super(message);
	}

}

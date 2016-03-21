package edu.kit.ipd.creativecrowd.persistentmodel;

//TODO Handle this without just inheriting from DatabaseException (or remove it if it appears unnecessary)
/**
 * Thrown if an already existing ID would be used for a new entry
 * 
 * @author Thomas Friedel
 *
 */
public class IDAlreadyUsedException extends DatabaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4533946279443925664L;
	
	public IDAlreadyUsedException(String message) {
		super(message);
	}
	

}

package edu.kit.ipd.chimpalot.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Wraps the http error status 404 (Not Found).
 * Used to signal the client that the requested file is not on this server
 * or could not be found for some other reason.
 * 
 * @author Thomas Friedel
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

	/**
	 * serial.
	 */
	private static final long serialVersionUID = -4054964907170595803L;
	
	/**
	 * @param message the error message. Is included in the response
	 */
	public ResourceNotFoundException(String message) {
		super(message);
	}
}

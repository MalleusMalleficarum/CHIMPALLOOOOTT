package edu.kit.ipd.chimpalot.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Wraps the http error status 409 (Conflict).
 * Used to signal the client that hit attempt to save something on the server would
 * cause an override.
 * 
 * @author Thomas Friedel
 */
@ResponseStatus(value = HttpStatus.CONFLICT)
public class ConflictException extends RuntimeException {
	
	/**
	 * serial.
	 */
	private static final long serialVersionUID = -4698215569941286703L;

	/**
	 * @param message the error message. Is included in the response
	 */
	public ConflictException(String message) {
		super(message);
	}
}

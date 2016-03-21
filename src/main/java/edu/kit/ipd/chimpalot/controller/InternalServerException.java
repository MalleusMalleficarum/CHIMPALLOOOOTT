package edu.kit.ipd.chimpalot.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Wraps the http error status 500 (Internal Server Error).
 * Used to signal the client that something went wrong on the server,
 * despite a correct request.
 * 
 * @author Thomas Friedel
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerException extends RuntimeException {

	/**
	 * serial.
	 */
	private static final long serialVersionUID = 5604146212709070216L;
	
	/**
	 * @param message the error message. Is included in the response
	 */
	public InternalServerException(String message) {
		super(message);
	}

}

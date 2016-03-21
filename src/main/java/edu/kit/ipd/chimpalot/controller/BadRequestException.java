package edu.kit.ipd.chimpalot.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Wraps the http error status 400 (Bad Request).
 * Signals the client that his request was malformed.
 * 
 * @author Thomas Friedel
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

	/**
	 * serial.
	 */
	private static final long serialVersionUID = -7503844411534153635L;
	
	/**
	 * @param message the error message. Is included in the response
	 */
	public BadRequestException(String message) {
		super(message);
	}

}

package edu.kit.ipd.creativecrowd.util;

public class Logger {
	private Logger() {

	}

	public static void log(String message) {
		System.err.println(message);
	}

	/**
	 * This method logs a class which is thrown by the document and is not catched correctly
	 * in that way that it should be logged for further researches
	 * 
	 * @param exception The exception which should be logged
	 */
	public static void logException(String exception)
	{
		System.err.println(exception);
	}
}

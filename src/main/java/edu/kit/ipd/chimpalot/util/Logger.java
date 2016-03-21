package edu.kit.ipd.chimpalot.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

/**
 * This Class represents the basic logger and is intended to unify
 * all text notifications within the Application.
 * @author Robin
 *
 */
public final class Logger {
	/**
	 * This Enumeration models the States of the Logger, which 
	 * represent the precision of the Logger with the following order:
	 * (DEBUG > LOG > ERROR)
	 * 
	 * @author Robin
	 *
	 */
    private enum LoggerState {
        DEBUG, LOG, ERROR;
        private int value() {
            switch(this) {
            case DEBUG: return 2;
            case LOG: return 1;
            default: return 0;
            }
        }
    }
    private static LoggerState state = LoggerState.DEBUG;
    private static boolean logToFile = false;
    private static String filename = "chimpalot.log";
    
    //private Constructor to prevent instantiation.
	private Logger() {

	}
	
	/**
	 * Sets the state of the logger. If {@code state} is not a valid state, nothing will happen,
	 * and the Logger will keep its previous (or initial) state. Possible states are:
	 * "DEBUG", "LOG", "ERROR". "DEBUG" is the chattiest, "ERROR" the least.
	 * 
	 * @param state the new state. Case is ignored.
	 */
	public static void setLoggerState(String state) {
		if (state.equalsIgnoreCase("debug")) {
			Logger.state = LoggerState.DEBUG;
			return;
		}
		if (state.equalsIgnoreCase("log")) {
			Logger.state = LoggerState.LOG;
			return;
		}
		if (state.equalsIgnoreCase("error")) {
			Logger.state = LoggerState.ERROR;
			return;
		}
	}
	
	/**
	 * This method is used for debug purposes and will only log messages if 
	 * the State of the Logger is at DEBUG (or higher) and should be used for
	 * messages which are only viable for debugging purposes.
	 * 
	 * @param message the message which should be logged
	 */
	public static void debug(String message) {
		if(state.value() >= LoggerState.DEBUG.value()) {
			if (logToFile) {
				try {
					PrintWriter out = new PrintWriter(new FileWriter(filename, true), true);
					out.write(logTime() + message + System.lineSeparator());
					out.close();
				} catch (IOException e) {
					//Oh well.
				}
			}
			System.out.println(message);
		}
	    
	}
	/**
	 * This method will only log if the State of the Logger is at LOG or higher
	 * and should be used for messages which are non-critical, but provide decent
	 * insight into the software.
	 * 
	 * @param message the message which should be logged
	 */
	public static void log(String message) {
	    if(state.value() >= LoggerState.LOG.value()) {
	    	if (logToFile) {
				try {
					PrintWriter out = new PrintWriter(new FileWriter(filename, true), true);
					out.write(logTime() + message + System.lineSeparator());
					out.close();
				} catch (IOException e) {
					//Oh well.
				}
			}
	        System.out.println(message);
	    }

	}
	/**
	 * This method will always log the given message and should only be 
	 * used for errors or critical information regarding the Application.
	 * 
	 * @param message the message which should be displayed
	 */
	public static void error(String message) {
		if (logToFile) {
			try {
				PrintWriter out = new PrintWriter(new FileWriter(filename, true), true);
				out.write(logTime() + message + System.lineSeparator());
				out.close();
			} catch (IOException e) {
				//Oh well.
			}
		}
	    System.out.println(message);
	}
	/**
	 * This method logs a class which is thrown by the document and is not caught correctly
	 * in that way that it should be logged for further researches
	 * 
	 * @param exception The exception which should be logged
	 */
	public static void logException(String exception)
	{
		if (logToFile) {
			try {
				PrintWriter out = new PrintWriter(new FileWriter(filename, true), true);
				out.write(logTime() + exception + System.lineSeparator());
				out.close();
			} catch (IOException e) {
				//Oh well.
			}
		}
		System.err.println(exception);
	}
	/**
	 * This method informs other components whether the Logger is in the Debug state
	 * or not.
	 * @return the debug state
	 */
	public static boolean isDebug() {
		return state.value() >= LoggerState.DEBUG.value();
	}
	
	private static String logTime() {
		return LocalDateTime.now().toString() + ": ";
	}
}

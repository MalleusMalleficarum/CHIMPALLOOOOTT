package edu.kit.ipd.chimpalot.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * This globally visible class contains static configuration info that has been
 * configured before runtime. Its values are usually set by the Router class
 * from the web.xml file within the WAR archive when starting the application.
 * The configured values can be accessed as needed. The configured values, once set,
 * never change.
 * 
 * @author jonas
 */
public class GlobalApplicationConfig {
	
	private static final String propertiesFilename = "chimpalot.properties";
	private static final String springApplicationProperties = "application.properties";
	
	private static boolean configured = false;
	private static String configAWSAccessKeyId;
	private static String configAWSSecretKey;
	private static String configDBPath;
	private static String configPublicBaseURL;
	
	private static String pyBossaMainURL;
	private static String pyBossaAccessKey;
	private static int pyBossaProjectId;
	
	private static boolean configSandbox;
	private static String requesterUsername;
	private static String requesterPassword; //TODO should not be kept in plain text (NF120).
	private static int pyBossaPaymentThreshold;	//in cents
	
	private static List<String> notifiedRequesterMail;
	
	public static List<String> getNotifiedRequesterMail() {
		if (!configured) 
			throw new Error("Tried to access configuration while not configured");
		return notifiedRequesterMail;
	}
	
	public static String getAWSAccessKeyID() {
		if (!configured)
			throw new Error("Tried to access configuration while not configured");
		return configAWSAccessKeyId;
	}

	public static String getAWSSecretKey() {
		if (!configured)
			throw new Error("Tried to access configuration while not configured");
		return configAWSSecretKey;
	}

	public static String getDBPath() {
		if (!configured)
			throw new Error("Tried to access configuration while not configured");
		return configDBPath;
	}

	public static String getPublicBaseURL() {
		if (!configured)
			throw new Error("Tried to access configuration while not configured");
		return configPublicBaseURL;
	}
	
	public static String getPyBossaMainURL() {
		if (!configured)
			throw new Error("Tried to access configuration while not configured");
		return pyBossaMainURL;
	}
	
	public static String getPyBossaAccessKey() {
		if (!configured)
			throw new Error("Tried to access configuration while not configured");
		return pyBossaAccessKey;
	}
	
	public static int getPyBossaProjectId() {
		if (!configured)
			throw new Error("Tried to access configuration while not configured");
		return pyBossaProjectId;
	}
	
	/**
	 * Returns the pyBossaPaymentThreshold in cents
	 * @return pyBossaPaymentThreshold
	 * @author Pascal Gabriel
	 */
	public static int getPyBossaPaymentThreshold() {
		if (!configured) {
			throw new Error("Tried to access configuration while not configured");
		}
		return pyBossaPaymentThreshold;
	}
	
	public static boolean isConfigured() {
		return configured;
	}

	/**
	 * @return true if the hit should be published on amazon mturk sandbox, false if it should be published on amazon mturk
	 */
	public static boolean isSandbox() {
		return configSandbox;
	}
	
	/**
	 * Automatically chooses dev profile, if set profile in application.properties is not "prod".
	 * 
	 * @see #configure(boolean)
	 */
	public static void configure() {
		Properties properties = new Properties();
		try {
			BufferedInputStream stream = new BufferedInputStream(new FileInputStream(springApplicationProperties));
			properties.load(stream);
			stream.close();
		} catch (IOException e) {
			Logger.log("Getting spring configuration from resources...");
			try {
				BufferedInputStream stream = new BufferedInputStream(
						GlobalApplicationConfig.class.getResourceAsStream("/" + springApplicationProperties));
				properties.load(stream);
				stream.close();
			} catch (IOException e2) {
				Logger.logException("Failed to read spring configuration file. Assuming not development.");
				configure(false);
				return;
			}
		}
		
		if (properties.getProperty("spring.profiles.active").equals("prod")) {
			configure(false);
		} else {
			configure(true);
		}
	}

	/**
	 * Set the application config from the properties file, if {@code dev} is {@code false}.
	 * Otherwise development values are loaded.
	 * Should be called right at application start.
	 * 
	 * @param dev whether or not to use development config
	 * @see edu.kit.ipd.chimpalot.Application
	 */
	public static void configure(boolean dev) { 
		if (!dev) { 
			Logger.log("Configuring from file '" + propertiesFilename + "'.");

			Properties properties = new Properties();
			try {
				BufferedInputStream stream = new BufferedInputStream(new FileInputStream(propertiesFilename));
				properties.load(stream);
				stream.close();
			} catch (IOException e) {
				Logger.log("Getting configuration from resources...");
				try {
					BufferedInputStream stream = new BufferedInputStream(
									GlobalApplicationConfig.class.getResourceAsStream("/" + propertiesFilename));
					properties.load(stream);
					stream.close();
				} catch (IOException e2) {
					throw new Error("Failed to read configuration file.", e);
				}
			}

			configAWSAccessKeyId = properties.getProperty("CreativeCrowd_AWSAccessKeyId");
			configAWSSecretKey = properties.getProperty("CreativeCrowd_AWSSecretKey");
			configDBPath = properties.getProperty("CreativeCrowd_DBPath");
			configPublicBaseURL = properties.getProperty("CreativeCrowd_PublicBaseURL");
			configSandbox = properties.getProperty("CreativeCrowd_IsSandbox").equals("true");
			requesterUsername = properties.getProperty("CreativeCrowd_RequesterUsername");
			requesterPassword = properties.getProperty("CreativeCrowd_RequesterPassword");
			pyBossaMainURL = properties.getProperty("Chimpalot_PyBossaMainURL");
			pyBossaAccessKey = properties.getProperty("Chimpalot_PyBossaAccessKey");
			//add all mails set in the config
			notifiedRequesterMail = new LinkedList<String>();
			String allMails = properties.getProperty("Chimpalot_NotifiedRequesterMail");
			String[] all = allMails.split("@@");
			for (int i = 0; i < all.length; i++) {
				notifiedRequesterMail.add(all[i]);
			}
			try {
				pyBossaProjectId = Integer.parseInt(properties.getProperty("Chimpalot_PyBossaProjectID"));
			} catch (NumberFormatException e1) {
				throw new Error("Failed to parse a number from '" + properties.getProperty("Chimpalot_PyBossaProjectID")
				+ "' for PyBossaProjectId. This should be an integer.");
			}
			try {
				setPyBossaPaymentThreshold(Integer.parseInt(properties.getProperty("Chimpalot_PyBossaPaymentThreshold")));
			} catch (NumberFormatException e) {
				throw new Error("Failed to parse a number from '" + properties.getProperty("Chimpalot_PyBossaPaymentThreshold")
				+ "' for PyBossaPaymentThreshold. This should be an integer.");
			} catch (IllegalArgumentException e) {
				throw new Error(e.getMessage());
			}
		} else {
			Logger.log("Configuring with development values.");

			// Otherwise, we'll use fallback values for development.
			configAWSAccessKeyId = "AKIAITJZI67YTQNCJ5PA"; // FIXME
			configAWSSecretKey = "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"; // FIXME
			configDBPath = "jdbc:sqlite:CreativeCrowd.db";
			configPublicBaseURL = "https://localhost:8010"; // assumes you have the SSL server running locally
			configSandbox = true;
			requesterUsername = "user";
			requesterPassword = "password"; //Secure as f*ck
			setPyBossaPaymentThreshold(500);
			
			pyBossaMainURL = "http://crowdcrafting.org/api/";
			pyBossaAccessKey ="api_key=0ae3365a-220b-41ce-867c-a3f1cee5bf2e";
			pyBossaProjectId = 3623;
			notifiedRequesterMail = new LinkedList<String>();
			notifiedRequesterMail.add("chimpalot.reward@gmail.com");
		}

		configured = true;
	}

	/**
	 * 
	 * @return the username for the requester interface.
	 */
	public static String getRequesterUsername() {
		if (!configured)
			throw new Error("Tried to access configuration while not configured");
		return requesterUsername;
	}
	
	/**
	 * 
	 * @return the password for the requester interface. Currently NOT encrypted.
	 */
	public static String getRequesterPassword() {
		if (!configured)
			throw new Error("Tried to access configuration while not configured");
		return requesterPassword;
	}
	
	/**
	 * Sets the threshold for the payment of the PyBossa workers. Must be at least 500 cents.
	 * The set value will not be helped when stopping the application.
	 * 
	 * @param threshold the new threshold
	 * @throws IllegalArgumentException if the threshold is lower than 500ct.
	 * @author Thomas Friedel
	 */
	public static void setPyBossaPaymentThreshold(int threshold) throws IllegalArgumentException {
		if (threshold < 500) {
			String message = "PyBossaPaymentThreshold must be at least 500 cents.";
			Logger.logException(message);
			throw new IllegalArgumentException(message);
		}
		pyBossaPaymentThreshold = threshold;
	}
	
	/**
	 * 
	 * @return a json-representation of the config. The password is hidden.
	 */
	public static String jsonify() {
		if (!configured)
			throw new Error("Tried to access configuration while not configured");
		StringBuilder result = new StringBuilder("{");
		result.append("\"aWSAccessKeyID\": \"" + getAWSAccessKeyID() + "\", ");
		result.append("\"aWSSecretKey\": \"" + getAWSSecretKey() + "\", ");
		result.append("\"dBPath\": \"" + getDBPath() + "\", ");
		result.append("\"publicBaseURL\": \"" + getPublicBaseURL() + "\", ");
		result.append("\"sandbox\": " + isSandbox() + ", ");
		result.append("\"requesterUsername\": \"" + getRequesterUsername() + "\", ");
		//result.append("\"requesterPassword\": \"" + getRequesterPassword() + "\", "); Hide this (for now)
		result.append("\"pyBossaPaymentThreshold\": " + getPyBossaPaymentThreshold());
		return (result + "}");
	}
}

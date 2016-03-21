package edu.kit.ipd.creativecrowd.util;

import javax.servlet.ServletContext;

/**
 * This globally visible class contains static configuration info that has been
 * configured before runtime. Its values are usually set by the Router class
 * from the web.xml file within the WAR archive when starting the application.
 * The configured values can be accessed as needed. The configured values, once set,
 * never change.
 * 
 * @author jonas
 * @see Router
 */
public class GlobalApplicationConfig {
	private static boolean configured = false;
	private static String configAWSAccessKeyId;
	private static String configAWSSecretKey;
	private static String configDBPath;
	private static String configPublicBaseURL;
	private static boolean configSandbox;
	private static String requesterCredentials;

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

	public static boolean isConfigured() {
		return configured;
	}

	/**
	 * @return true if the hit should be published on amazon mturk sandbox, false if it should be published on amazon mturk
	 */
	public static boolean isSandbox() {
		return configSandbox;
		// TODO SANDBOX in configureFROM SERVLETContext einbinden
	}

	/**
	 * Set the application config from the given servlet context. This is usually
	 * called while processing the first request after startup.
	 * 
	 * @param ctx the ServletContext containing the configuration, or null
	 * @see Router
	 */
	public static void configureFromServletContext(ServletContext ctx) {
		// Check if the servlet context is present. If it is, we're running inside a servlet container
		// and our config is in the web.xml of the WAR archive.
		if (ctx != null) {
			System.out.println("Running in servlet container -- configuring from servlet context (usually from web.xml)");

			configAWSAccessKeyId = requireParam(ctx, "CreativeCrowd_AWSAccessKeyId");
			configAWSSecretKey = requireParam(ctx, "CreativeCrowd_AWSSecretKey");
			configDBPath = requireParam(ctx, "CreativeCrowd_DBPath");
			configPublicBaseURL = requireParam(ctx, "CreativeCrowd_PublicBaseURL");
			configSandbox = requireParam(ctx, "CreativeCrowd_IsSandbox").equals("true");
			requesterCredentials = requireParam(ctx, "CreativeCrowd_RequesterCredentials");
		} else {
			System.out.println("Running standalone (no servlet context) -- configuring with development values.");

			// Otherwise, we'll use fallback values for development.
			configAWSAccessKeyId = "AKIAITJZI67YTQNCJ5PA"; // FIXME
			configAWSSecretKey = "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"; // FIXME
			configDBPath = "jdbc:sqlite:CreativeCrowd.db";
			configPublicBaseURL = "https://localhost:4567"; // assumes you have the SSL server running locally
			configSandbox = true;
			requesterCredentials = ""; // no authentication of the requester
		}

		configured = true;
	}

	private static String requireParam(ServletContext ctx, String key) {
		String val = ctx.getInitParameter(key);
		if (val == null) {
			throw new Error("Missing servlet context param '" + key + "' in web.xml!");
		}
		return val;
	}

	public static String getRequesterCredentials() {
		if (!configured)
			throw new Error("Tried to access configuration while not configured");
		return requesterCredentials;
	}
}

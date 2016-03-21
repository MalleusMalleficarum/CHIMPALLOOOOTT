package edu.kit.ipd.creativecrowd.crowdplatform;

public enum PlatformIdentity {
    MTurk("MT"), PyBossa("PB"), Unspecified("UN");
	
	private String prefix;
	
	private PlatformIdentity(String prefix) {
		this.prefix = prefix;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	/**
	 * If the String {@code s} starts with a prefix which is assigned to a platform, then that platform is returned.
	 * If not, this returns the platform {@code Unspecified}.
	 * 
	 * @param s a string to get the platform from
	 * @return the platform
	 * @author Thomas Friedel
	 */
	public static PlatformIdentity getIdentityFromPrefix(String s) {
		for (PlatformIdentity platform : values()) {
			if (s.startsWith(platform.prefix)) {
				return platform;
			}
		}
		return Unspecified;
	}
}

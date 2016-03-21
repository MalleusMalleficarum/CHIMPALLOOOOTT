package edu.kit.ipd.creativecrowd.transformer;

import edu.kit.ipd.creativecrowd.crowdplatform.PlatformIdentity;

/**
 * This interface gives a method to distribute a given extend assignment
 * request among the different platforms.
 * 
 * @author Robin
 *
 */
public interface LoadBalance {
	/**
	 * Determines the amount of additional assignments for a platform from 
	 * the given increase amount.
	 * @param id the platforms
	 * @param amount the amount of increase
	 * @return how much each platform should be increased.
	 */
    public Iterable<Integer> distribution(Iterable<PlatformIdentity> id, int amount);
}

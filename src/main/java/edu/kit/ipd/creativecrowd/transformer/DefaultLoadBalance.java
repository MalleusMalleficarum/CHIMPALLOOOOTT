package edu.kit.ipd.creativecrowd.transformer;

import java.util.ArrayList;
import java.util.List;

import edu.kit.ipd.creativecrowd.crowdplatform.PlatformIdentity;
/**
 * This class represents the Default Load distribution which increases
 * the amount of assignments on all platforms by the same amount.
 * @author Robin
 *
 */
public class DefaultLoadBalance implements LoadBalance {
	
	/**
	 * Increases the amount of assignments by the original amount specified for all platforms.
	 */
    @Override
    public Iterable<Integer> distribution(Iterable<PlatformIdentity> id, int amount) {
        List<Integer> result = new ArrayList<>();
        for(@SuppressWarnings("unused") PlatformIdentity pid : id) {
        	result.add(amount);
        }
        return result;
    }

}

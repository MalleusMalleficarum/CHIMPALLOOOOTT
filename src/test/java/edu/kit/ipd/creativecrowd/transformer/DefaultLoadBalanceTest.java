package edu.kit.ipd.creativecrowd.transformer;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import edu.kit.ipd.creativecrowd.crowdplatform.PlatformIdentity;

public class DefaultLoadBalanceTest {

	DefaultLoadBalance dlb;
	LinkedList<PlatformIdentity> pi;
	
	@Before
	public void setUp() {
		dlb = new DefaultLoadBalance();
		pi = new LinkedList<PlatformIdentity>();
		pi.add(PlatformIdentity.MTurk);
		pi.add(PlatformIdentity.PyBossa);
	}
	@Test
	public void testDistribution() {
		Iterable<Integer> test = dlb.distribution(pi, 12);
		Iterator<Integer> tester = test.iterator();
		assertEquals(tester.next(), tester.next());
		int j = 0;
		for(@SuppressWarnings("unused") Integer i : test) {
			j++;
		}
		assertEquals(2, j);
	}

}

package edu.kit.ipd.creativecrowd.operations;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class StrategyLoaderTest {
	Map<String, String> params;
	StrategyLoader<AssignmentPaymentOutcomeCalculator> loader;

	@Before
	public void setUp() throws Exception {
		params = new HashMap<String, String>();
		params.put("apoc_class",
				"edu.kit.ipd.creativecrowd.operations.strategies.BonusAPOC");
		params.put("test", "test");
		loader = new StrategyLoader<AssignmentPaymentOutcomeCalculator>(params);
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Führt die Methode loadFromParam des StrategyLoaders aus und stellt
	 * sicher, dass die richtige Klasse geladen wird.
	 */
	@Test
	public void test() {
		try {
			AbstractStrategy a = loader.loadFromParam("apoc_class");
			assertTrue(a instanceof AssignmentPaymentOutcomeCalculator);
		} catch (StrategyNotFoundException e) {
			fail(e.getMessage());
		}

	}

	/**
	 * Führt die Methode loadFromParam des StrategyLoaders aus und stellt
	 * sicher, dass bei einem Value, zu dem keine Klasse existiert auch keine
	 * geladen wird.
	 */
	@Test
	public void testException() {
		try {
			loader.loadFromParam("test");

		} catch (Exception e) {
			assertTrue(e instanceof StrategyNotFoundException);
		}

	}

}

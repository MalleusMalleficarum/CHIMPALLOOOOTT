package edu.kit.ipd.creativecrowd.operations;

import java.util.Map;

/**
 * This class loads strategies dynamically when they´re needed based on the entered params
 * 
 * @author Anika
 * @param <T> type of strategy
 */
class StrategyLoader<T extends AbstractStrategy> {
	protected final Map<String, String> params;

	StrategyLoader(Map<String, String> params) {
		this.params = params;
	}

	/**
	 * loads strategies
	 * 
	 * @param key specifies where to find the class in the map
	 * @return instance of the class specified inn the map
	 * @throws StrategyNotFoundException if the strategy class doesn´t exist
	 */
	T loadFromParam(String key) throws StrategyNotFoundException {
		String implementationName = params.get(key);

		try {
			// We want to allow this unchecked cast since we catch the ClassCastException below.
			// (We pretty much fuck up the entire Java type & generics system along the way)
			@SuppressWarnings("unchecked")
			Class<T> c = (Class<T>) Class.forName(implementationName);

			try {
				T strat = c.newInstance();
				AbstractStrategy abs = strat;
				abs.setParams(params);

				return strat;

			} catch (InstantiationException e) {
				throw new StrategyNotFoundException("Strategy class invalid for " + key + ": " + implementationName, e);
			} catch (IllegalAccessException e) {
				throw new StrategyNotFoundException("Strategy class invalid for " + key + ": " + implementationName, e);
			}
		} catch (ClassNotFoundException | NullPointerException | ClassCastException e) {
			throw new StrategyNotFoundException("Could not find strategy class or class does not conform to strategy interface: " + implementationName, e);
		}
	}
}

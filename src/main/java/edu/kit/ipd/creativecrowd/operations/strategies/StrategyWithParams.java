package edu.kit.ipd.creativecrowd.operations.strategies;

import java.util.Map;

/**
 * @author Anika+Jonas
 *         superclass for all Strategies
 *         to generalise how they get params
 */
public abstract class StrategyWithParams {
	/**
	 * Map containing all params as Strings
	 */
	private Map<String, String> params;

	/**
	 * sets params
	 * 
	 * @param theParams they get
	 */
	public void setParams(Map<String, String> theParams) {
		this.params = theParams;
	}

	/**
	 * this method finds a value by its key
	 * 
	 * @param name key
	 * @param default value if value is null
	 * @return the value of the given key
	 */
	protected int getIntParam(String name, int def) {
		String s = this.params.get(name);
		if (s != null) {
			return Integer.parseInt(s);
		} else {
			return def;
		}
	}

	/**
	 * this method finds a value by its key
	 * 
	 * @param name key
	 * @param def default value if value is empty
	 * @return the value of the given key
	 */
	protected float getFloatParam(String name, float def) {
		String s = this.params.get(name);
		if (s != null) {
			return Float.parseFloat(s);
		} else {
			return def;
		}
	}

	/**
	 * this method finds a value by its key
	 * 
	 * @param name key
	 * @param def default value if value is null
	 * @return
	 */
	protected String getStringParam(String name, String def) {
		String r = this.params.get(name);
		if (r == null) {
			r = def;
		}
		return r;
	}
}

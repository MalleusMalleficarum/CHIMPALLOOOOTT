package edu.kit.ipd.creativecrowd.operations;

import java.util.Map;

/**
 * This class is the super class for all strategies
 * strategies are building blocks of the program and have a certain task
 * 
 * @author Anika & Jonas
 */
interface AbstractStrategy {
	/**
	 * gives strategyParams to a new instance of a strategy
	 * 
	 * @param theParams from the Experiment Spec
	 */
	void setParams(Map<String, String> theParams);
}

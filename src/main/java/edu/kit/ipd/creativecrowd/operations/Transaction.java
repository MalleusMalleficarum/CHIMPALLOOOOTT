package edu.kit.ipd.creativecrowd.operations;

import java.util.Map;

/**
 * super class for all Transactions, which can be called by classes from the package Connector
 * 
 * @author Anika + Jonas
 */
public class Transaction {
	/**
	 * method for loading the strategyImplementation for a
	 * TotalAssignmentCountController using a strategyloader
	 * 
	 * @param params
	 *        params the requester specified in the config data
	 * @return an Instance of TotalAssignmentCountController
	 * @throws StrategyNotFoundException
	 *         if the specified strategy doesn�t exist or isn�t accesible
	 */
	protected TotalAssignmentCountController loadTACCStrategy(
			Map<String, String> params) throws StrategyNotFoundException {
		return new StrategyLoader<TotalAssignmentCountController>(params)
				.loadFromParam("tacc_class");
	}

	/**
	 * method for loading the suitable strategy implementation of TaskConstellationMutator
	 * 
	 * @param params the requester specified in configData
	 * @return Instance of TCM
	 * @throws StrategyNotFoundException
	 */
	protected TaskConstellationMutator loadTCMStrategy(
			Map<String, String> params) throws StrategyNotFoundException {
		return new StrategyLoader<TaskConstellationMutator>(params)
				.loadFromParam("tcm_class");
	}

	/**
	 * method for loading the suitable strategy implementation of a RatingSufficiencyDecider
	 * 
	 * @param params the requester specified in ConfigFile
	 * @return instance of RSD
	 * @throws StrategyNotFoundException if the strategy class disappeared
	 */
	protected RatingSufficiencyDecider loadRSDStrategy(
			Map<String, String> params) throws StrategyNotFoundException {
		return new StrategyLoader<RatingSufficiencyDecider>(params)
				.loadFromParam("rsd_class");
	}

	/**
	 * method for loading the suitable strategy implementation of a RatingWualityIndexCalculator
	 * 
	 * @param params the requester specified in the Configfile
	 * @return instance of RQIC
	 * @throws StrategyNotFoundException if the strategy class disappeared
	 */
	protected RatingQualityIndexCalculator loadRQICStrategy(
			Map<String, String> params) throws StrategyNotFoundException {
		return new StrategyLoader<RatingQualityIndexCalculator>(params)
				.loadFromParam("rqic_class");
	}

	/**
	 * method for loading the specified strategy implementation of a AnswerQualityIndex
	 * 
	 * @param params the requester specified in the configfile
	 * @return instance of AQIC
	 * @throws StrategyNotFoundException if strategy class could not be found
	 */
	protected AnswerQualityIndexCalculator loadAQICStrategy(
			Map<String, String> params) throws StrategyNotFoundException {
		return new StrategyLoader<AnswerQualityIndexCalculator>(params)
				.loadFromParam("aqic_class");
	}

	/**
	 * method for loading the specified strategy implementation of a FeedbackTextGenerator
	 * 
	 * @param params the requester specified in the configfile
	 * @return instance of FTG
	 * @throws StrategyNotFoundException if strategy class could not be found
	 */
	protected FeedbackTextGenerator loadFTGStrategy(Map<String, String> params)
			throws StrategyNotFoundException {
		return new StrategyLoader<FeedbackTextGenerator>(params)
				.loadFromParam("ftg_class");
	}

	/**
	 * method for loading the specified strategy implementation of a AssignmentPaymentOutcomeCalculator
	 * 
	 * @param params the requester specified in configgile
	 * @return instance of APOC
	 * @throws StrategyNotFoundException if strategy class could not be found
	 */
	protected AssignmentPaymentOutcomeCalculator loadAPOCStrategy(
			Map<String, String> params) throws StrategyNotFoundException {
		return new StrategyLoader<AssignmentPaymentOutcomeCalculator>(params)
				.loadFromParam("apoc_class");
	}
}

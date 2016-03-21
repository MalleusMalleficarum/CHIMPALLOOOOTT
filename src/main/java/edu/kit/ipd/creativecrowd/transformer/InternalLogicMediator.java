package edu.kit.ipd.creativecrowd.transformer;


import edu.kit.ipd.creativecrowd.crowdplatform.PlatformIdentity;

/**
 * This Class is meant to communicate with the logic of the transformer in order to
 * assert an easy way to extend the functionality of the transformer without causing 
 * too much code changes.
 * 
 * @author Robin
 *
 */
public class InternalLogicMediator {
    private LoadBalance howto;
    private BudgetVerification verification;
    private IDGenerator generator;
    
    /**
     * Generates a new LogicMediator. This should only be used in the transformer 
     * package, thus it is protected.
     */
    protected InternalLogicMediator() {
    	this.howto = new DefaultLoadBalance();
    	this.verification = new DefaultBudgetVerification();
    	this.generator = new IDGenerator();
    }
    /**
     * This method communicates with the corresponding software component that is
     * responsible for determining the number to increase the maximum assignments
     * for the platforms.
     * 
     * @param id the platforms
     * @param amount the amount of increase
     * @return how much each individual platform should get increased.
     */
    protected Iterable<Integer> loadDistribution(Iterable<PlatformIdentity> id, int amount) {
        Iterable<Integer> result = howto.distribution(id, amount);
        return result;
        
    }
    /**
     * This method communicates with the software element that determines whether
     * the budget currently present on the platforms suffices to fund the experiment.
     * 
     * @param id the platforms
     * @return whether the budget suffices or not.
     */
    protected boolean budgetVerification(Iterable<Boolean> id) {
        return verification.verify(id);
        
    }
    /**
     * This method contacts the Idgenerator to recieve an ID which not only represents
     * the experiment, but also codes in the IDs of all participating Platforms.
     * 
     * @param pidStrings the String of the Platforms participating in the Hit distribution.
     * @return the unified String ID.
     */
    protected String generateID(Iterable<String> pidStrings) {
        
        return generator.getMainID(pidStrings);
        
    }
    /**
     * This method contacts the IDgeneratot to extract the ID from a specified platform 
     * using the mainID.
     * 
     * @param mainID the MainID from which the information is extracted.
     * @param id the platform from which the original ID is needed.
     * @return the original ID.
     */
    protected String getID(String mainID, PlatformIdentity id) {
        return generator.getID(mainID, id);
    }
    
    
}

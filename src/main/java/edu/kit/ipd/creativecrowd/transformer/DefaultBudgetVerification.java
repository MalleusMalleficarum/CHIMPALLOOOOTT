package edu.kit.ipd.creativecrowd.transformer;

/**
 * This class represents the default budget verification.
 * 
 * @author Robin
 *
 */
public class DefaultBudgetVerification implements BudgetVerification {

	/**
	 * This method only validates the current budgets if all platforms can supply a
	 * neccessary amount from their budget.
	 */
    @Override
    public boolean verify(Iterable<Boolean> id) {
        boolean response = true;

        for(boolean b : id) {
        	if(response == true) {
        		response = b;
        	}
        }

        return response;
    }

}

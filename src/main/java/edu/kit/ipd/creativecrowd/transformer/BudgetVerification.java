package edu.kit.ipd.creativecrowd.transformer;

/**
 * This interface provides a method to verify whether the budgets currently
 * present at the different platforms suffice to start the experiment. In 
 * case that multiple platforms might share the same budget or simply may be
 * ignored for the purpose of the experiment.
 * 
 * @author Robin 
 *
 */
public interface BudgetVerification {
	/**
	 * Verifies whether the given Budgets of the platforms alone are sufficient
	 * to launch the experiment.
	 * 
	 * @param id the validation of the budgets alone.
	 * @return whether the budget suffices alone.
	 */
    public boolean verify(Iterable<Boolean> id);
}

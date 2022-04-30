package edu.vanderbilt.studzikm;

import java.util.function.Supplier;

/**
 * Interface for computing rewards for actions
 */
public interface RewardComputation {

	/**
	 * Computes the reward of an action for the supplied country
	 *
	 * @param result the action to compute the reward for
	 * @param retrieveCountry the country to compute the reward for
	 * @return the numeric reward of the action for the given country
	 */
	Double computeReward(ActionResult<?> result, Supplier<Country> retrieveCountry);
}

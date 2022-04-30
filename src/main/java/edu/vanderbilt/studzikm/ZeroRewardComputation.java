package edu.vanderbilt.studzikm;

import java.util.function.Supplier;

/**
 * A reward computation that always produces a score of 0
 */
public class ZeroRewardComputation implements RewardComputation {

	/**
	 * Computes the a reward of 0
	 * @param result the action to compute the reward for
	 * @param retrieveCountry the country to compute the reward for
	 * @return 0
	 */
	@Override
	public Double computeReward(ActionResult<?> result, Supplier<Country> retrieveCountry) {
		return 0.0;
	}

}

package edu.vanderbilt.studzikm;

import java.util.Map;
import java.util.function.Supplier;

/**
 * Computation for computing discounted rewards
 */
public class DiscountedRewardComputation implements RewardComputation {

	private Double gamma;
	private Map<String, Double> initialQualities;

	/**
	 * Creates the discounted reward computation
	 * @param gamma the discount factor
	 * @param initialQualities initial qualities for all countries
	 */
	public DiscountedRewardComputation(Double gamma, Map<String, Double> initialQualities) {
		this.gamma = gamma;
		this.initialQualities = initialQualities;
	}

	/**
	 * Computes the discounted reward for the given country given the action result
	 * @param result the action to compute the reward for
	 * @param retrieveCountry the country to compute the reward for
	 * @return the numeric reward
	 */
	@Override
	public Double computeReward(ActionResult<?> result, Supplier<Country> retrieveCountry) {
		Country country = retrieveCountry.get();

		return (Math.pow(gamma, result.getSchedulePosition()) * country.computeQuality()) - 
				initialQualities.get(country.getName());
	}

}

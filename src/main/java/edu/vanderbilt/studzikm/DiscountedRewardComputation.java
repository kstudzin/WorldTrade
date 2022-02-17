package edu.vanderbilt.studzikm;

import java.util.Map;
import java.util.function.Supplier;

public class DiscountedRewardComputation implements RewardComputation {

	private Double gamma;
	private Map<String, Double> initialQualities;

	public DiscountedRewardComputation(Double gamma, Map<String, Double> initialQualities) {
		this.gamma = gamma;
		this.initialQualities = initialQualities;
	}

	@Override
	public Double computeReward(ActionResult<?> result, Supplier<Country> retrieveCountry) {
		Country country = retrieveCountry.get();

		return (Math.pow(gamma, result.getSchedulePosition()) * country.computeQuality()) - 
				initialQualities.get(country.getName());
	}

}

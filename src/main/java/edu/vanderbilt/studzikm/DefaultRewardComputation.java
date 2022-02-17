package edu.vanderbilt.studzikm;

import java.util.function.Supplier;

public class DefaultRewardComputation implements RewardComputation {

	@Override
	public Double computeReward(ActionResult<?> result, Supplier<Country> retrieveCountry) {
		return 0.0;
	}

}

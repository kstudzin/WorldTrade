package edu.vanderbilt.studzikm;

import java.util.function.Supplier;

public interface RewardComputation {

	Double computeReward(ActionResult<?> result, Supplier<Country> retrieveCountry);
}

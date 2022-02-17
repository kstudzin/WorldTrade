package edu.vanderbilt.studzikm;

import java.util.Map;

public interface RewardComputationBuilder {

	RewardComputationBuilder setInitialQualities(Map<String, Double> initialQualities);

	RewardComputation build();
}

package edu.vanderbilt.studzikm;

import java.util.Map;

// TODO Is this file needed?
public interface RewardComputationBuilder {

	RewardComputationBuilder setInitialQualities(Map<String, Double> initialQualities);

	RewardComputation build();
}

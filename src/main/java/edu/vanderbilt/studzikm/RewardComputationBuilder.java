package edu.vanderbilt.studzikm;

public interface RewardComputationBuilder {

	RewardComputationBuilder setInitialQuality(double initialQuality);

	RewardComputation build();
}

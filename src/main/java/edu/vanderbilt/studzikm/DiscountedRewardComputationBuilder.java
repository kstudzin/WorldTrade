package edu.vanderbilt.studzikm;

import java.util.Map;

public class DiscountedRewardComputationBuilder implements RewardComputationBuilder {

	private double gamma;
	private Map<String, Double> initialQualities;

	public DiscountedRewardComputationBuilder() { }

	public DiscountedRewardComputationBuilder setGamma(double gamma) {
		this.gamma = gamma;
		return this;
	}

	public DiscountedRewardComputationBuilder setInitialQualities(Map<String, Double> initialQualities) {
		this.initialQualities = initialQualities;
		return this;
	}

	@Override
	public RewardComputation build() {
		return new DiscountedRewardComputation(gamma, initialQualities);
	}

}
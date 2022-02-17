package edu.vanderbilt.studzikm;

public class DiscountedRewardComputationBuilder implements RewardComputationBuilder {

	private double gamma;
	private double initialQuality;

	public DiscountedRewardComputationBuilder() { }

	public DiscountedRewardComputationBuilder setGamma(double gamma) {
		this.gamma = gamma;
		return this;
	}

	public DiscountedRewardComputationBuilder setInitialQuality(double initialQuality) {
		this.initialQuality = initialQuality;
		return this;
	}

	@Override
	public RewardComputation build() {
		return new DiscountedRewardComputation(gamma, initialQuality);
	}

}
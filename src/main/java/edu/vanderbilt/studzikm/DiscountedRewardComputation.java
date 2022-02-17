package edu.vanderbilt.studzikm;

public class DiscountedRewardComputation implements RewardComputation {

	private Double gamma;
	private Double initQuality;

	public DiscountedRewardComputation(Double gamma, Double initQuality) {
		this.gamma = gamma;
		this.initQuality = initQuality;
	}

	@Override
	public Double computeReward(SearchNode node) {
		return (Math.pow(gamma, node.getDepth()) * node.getQuality()) - initQuality;
	}

}

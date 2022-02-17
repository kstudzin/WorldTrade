package edu.vanderbilt.studzikm;

public class DiscountedRewardComputation implements RewardComputation {

	private Double gamma;
	private Double initQuality;

	public DiscountedRewardComputation(Double gamma, Double initQuality) {
		this.gamma = gamma;
		this.initQuality = initQuality;
	}

	@Override
	public Double computeReward(ActionResult<?> result) {
		return (Math.pow(gamma, result.getSchedulePosition()) * result.getQuality()) - initQuality;
	}

}

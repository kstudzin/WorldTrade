package edu.vanderbilt.studzikm;

public class TransformResult extends ActionResult<Transform> {

	public TransformResult(World world, 
			Transform transform, 
			Country self, 
			RewardComputation rewardCompuation, 
			int schedulePosition) {
		super(world, transform, self, rewardCompuation, schedulePosition);
	}

	public double getSuccessProbability(SuccessProbabilityComputation computation) {
		return computation.compute(this);
	}
}

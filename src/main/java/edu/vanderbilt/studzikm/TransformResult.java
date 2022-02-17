package edu.vanderbilt.studzikm;

public class TransformResult extends ActionResult<Transform> {

	public TransformResult(World world, 
			Transform transform, 
			Country performer, 
			RewardComputation rewardCompuation, 
			int schedulePosition) {
		super(world, transform, performer, rewardCompuation, schedulePosition);
	}

	public double getSuccessProbability(SuccessProbabilityComputation computation) {
		return computation.compute(this);
	}
}

package edu.vanderbilt.studzikm;

public class TransformResult extends ActionResult<Transform> {

	public TransformResult(World world, 
			Transform transform, 
			Country self, 
			RewardComputation rewardCompuation, 
			int schedulePosition,
			ResourceDelta delta) {
		super(world, transform, self, rewardCompuation, schedulePosition, delta);
	}

}

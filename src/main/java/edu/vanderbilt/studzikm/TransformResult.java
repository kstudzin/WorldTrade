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

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		return true;
	}

}

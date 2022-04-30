package edu.vanderbilt.studzikm;

/**
 * The result of a country performing a transform
 */
public class TransformResult extends ActionResult<Transform> {

	/**
	 * Creates a transform result
	 * @param world the world state resulting from an action
	 * @param transform the tranform that was applied
	 * @param self the country that performed the transform
	 * @param rewardComputation the computation to find the reward of the action for the performing country
	 * @param schedulePosition the position within the schedule of this action
	 * @param delta the amount of resource change
	 */
	public TransformResult(World world, 
			Transform transform, 
			Country self, 
			RewardComputation rewardComputation,
			int schedulePosition,
			ResourceDelta delta) {
		super(world, transform, self, rewardComputation, schedulePosition, delta);
	}

	/**
	 * Gets the role of the performing country in this action
	 *
	 * This is not particularly useful except to maintain the same
	 * interface as TransferResult
	 * @return UNARY
	 */
	@Override
	public Role getRole() {
		return Role.UNARY;
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

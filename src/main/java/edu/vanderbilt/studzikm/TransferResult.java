package edu.vanderbilt.studzikm;

import java.util.Objects;

/**
 * The result of a country performing a transfer
 */
public class TransferResult extends ActionResult<Transfer> {

	private Country other;
	private double otherReward;
	private Role selfRole;

	/**
	 * Creates a transfer result
	 * @param world the world state resulting from the action
	 * @param transform the transform that was applied
	 * @param self the country that initiated the transfer
	 * @param other the other country involved in the transfer
	 * @param rewardCompuation the computation to find the reward of the action for the initiating country
	 * @param schedulePosition the position within the schedule of this action
	 * @param selfRole whether the initiating country transferred resources to another country or
	 *                   had resources transferred to it
	 * @param delta the amount of resource change
	 */
	public TransferResult(World world, 
			Transfer transform, 
			Country self, 
			Country other, 
			RewardComputation rewardCompuation, 
			int schedulePosition,
			Role selfRole,
			ResourceDelta delta) {
		super(world, transform, self, rewardCompuation, schedulePosition, delta);
		this.other = other; 
		this.otherReward = rewardCompuation.computeReward(this, this::getOther);
		this.selfRole = selfRole;
	}

	/**
	 * Gets the role
	 * @return  whether the initiating country sent or received the items
	 */
	public Role getRole() {
		return selfRole;
	}

	/**
	 * Gets the other country involved in the transfer
	 * @return the other involved country
	 */
	public Country getOther() {
		return other;
	}

	/**
	 * Gets the reward of the action for the other involved country
	 * @return the reward for the other country
	 */
	public Double getOtherReward() {
		return otherReward;
	}

	@Override
	public String toString() {
		return "TransferResult [other=" + other.getName() + ", "
				+ "action=" + action + ", "
				+ "self=" + self.getName() + ", "
				+ "role=" + selfRole + ", "
				+ "quality=" + quality + ", "
				+ "reward=" + reward + ", "
				+ "world=" + world + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(other, otherReward, selfRole);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		TransferResult other = (TransferResult) obj;
		return Objects.equals(this.other, other.other)
				&& Double.doubleToLongBits(otherReward) == Double.doubleToLongBits(other.otherReward)
				&& selfRole == other.selfRole;
	}

}

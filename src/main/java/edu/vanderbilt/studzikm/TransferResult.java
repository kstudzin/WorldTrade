package edu.vanderbilt.studzikm;

import java.util.Objects;

public class TransferResult extends ActionResult<Transfer> {

	enum Role {
		SENDER, RECIEVER
	}

	private Country other;
	private double otherReward;
	private Role selfRole;

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

	public Role getRole() {
		return selfRole;
	}

	public Country getOther() {
		return other;
	}

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

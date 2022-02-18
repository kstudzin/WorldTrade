package edu.vanderbilt.studzikm;

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
			Role selfRole) {
		super(world, transform, self, rewardCompuation, schedulePosition);
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

	public double getSuccessProbability(SuccessProbabilityComputation computation) {
		return computation.compute(this);
	}

	public double getOtherReward() {
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

}

package edu.vanderbilt.studzikm;

public class ActionResult<T extends Action> {

	World world;
	T action;
	Country self;
	Double quality;
	Double reward;
	int schedulePosition;

	public ActionResult(World world, 
			T transform, 
			Country self, 
			RewardComputation rewardComputation,
			int schedulePosition) {
		this.world = world;
		this.action = transform;
		this.self = self;
		this.quality = this.self.computeQuality();
		this.reward = rewardComputation.computeReward(this, this::getSelf);
		this.schedulePosition = schedulePosition;
	}

	public World getWorld() {
		return world;
	}

	public T getAction() {
		return action;
	}

	public Double getQuality() {
		return quality;
	}

	public Double getReward() {
		return reward;
	}

	public int getSchedulePosition() {
		return schedulePosition;
	}

	public Country getSelf() {
		return self;
	}

	@Override
	public String toString() {
		return "ActionResult [transform=" + action + ", self=" + self.getName() + ", quality="
				+ quality + ", reward=" + reward + ", world=" + world + "]";
	}
}

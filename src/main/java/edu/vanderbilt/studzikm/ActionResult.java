package edu.vanderbilt.studzikm;

public class ActionResult<T extends Action> {

	World world;
	T transform;
	Country performer;
	Double quality;
	Double reward;
	int schedulePosition;

	public ActionResult(World world, 
			T transform, 
			Country performer, 
			RewardComputation rewardComputation,
			int schedulePosition) {
		this.world = world;
		this.transform = transform;
		this.performer = performer;
		this.quality = this.performer.computeQuality();
		this.reward = rewardComputation.computeReward(this, this::getPerformer);
		this.schedulePosition = schedulePosition;
	}

	public World getWorld() {
		return world;
	}

	public T getAction() {
		return transform;
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

	public Country getPerformer() {
		return performer;
	}

	@Override
	public String toString() {
		return "ActionResult [transform=" + transform + ", performer=" + performer.getName() + ", quality="
				+ quality + ", reward=" + reward + ", world=" + world + "]";
	}
}

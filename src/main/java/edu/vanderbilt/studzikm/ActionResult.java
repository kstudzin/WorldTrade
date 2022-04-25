package edu.vanderbilt.studzikm;

import java.util.Objects;

public class ActionResult<T extends Action> {

	World world;
	T action;
	Country self;
	Double quality;
	Double reward;
	int schedulePosition;
	ResourceDelta delta;

	public ActionResult(World world, 
			T transform, 
			Country self, 
			RewardComputation rewardComputation,
			int schedulePosition,
			ResourceDelta delta) {
		this.world = world;
		this.action = transform;
		this.self = self;
		this.quality = this.self.computeQuality();
		this.reward = rewardComputation.computeReward(this, this::getSelf);
		this.schedulePosition = schedulePosition;
		this.delta = delta;
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

	public ResourceDelta getResourceDelta() {
		return delta;
	}

	public Action.Type getType() {
		return action.getType();
	}

	@Override
	public String toString() {
		return "ActionResult [action=" + action + ", "
				+ "self=" + self.getName() + ", "
				+ "quality=" + quality + ", "
				+ "reward=" + reward + ", "
				+ "world=" + world + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(action, delta, quality, reward, schedulePosition, self, world);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ActionResult<?> other = (ActionResult<?>) obj;
		return Objects.equals(action, other.action) && Objects.equals(delta, other.delta)
				&& Objects.equals(quality, other.quality) && Objects.equals(reward, other.reward)
				&& schedulePosition == other.schedulePosition && Objects.equals(self, other.self)
				&& Objects.equals(world, other.world);
	}

}

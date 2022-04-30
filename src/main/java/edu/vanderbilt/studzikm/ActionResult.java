package edu.vanderbilt.studzikm;

import java.util.Objects;

/**
 * Represents the state of the world as the result of an action.
 *
 * @param <T> the type of action that this result represents
 */
public abstract class ActionResult<T extends Action> {

	/**
	 * Represents the role that a country played in an action.
	 * Most important for Transfer actions
	 */
	public enum Role {
		SENDER, RECEIVER, UNARY, NULL
	}

	World world;
	T action;
	Country self;
	Double quality;
	Double reward;
	int schedulePosition;
	ResourceDelta delta;

	/**
	 * Creates an action result
	 *
	 * @param world the world resulting from the action
	 * @param action the action performed
	 * @param self the country that performed the action
	 * @param rewardComputation computation of the reward of this action for the country who performed it
	 * @param schedulePosition position of this action within the complete schedule of actions
	 * @param delta delta of resources gained and lost through this action
	 */
	public ActionResult(World world, 
			T action,
			Country self, 
			RewardComputation rewardComputation,
			int schedulePosition,
			ResourceDelta delta) {
		this.world = world;
		this.action = action;
		this.self = self;
		this.quality = this.self.computeQuality();
		this.reward = rewardComputation.computeReward(this, this::getSelf);
		this.schedulePosition = schedulePosition;
		this.delta = delta;
	}

	/**
	 * Gets the world resulting from the action
	 * @return the world
	 */
	public World getWorld() {
		return world;
	}

	/**
	 * Gets the action performed
	 * @return the action
	 */
	public T getAction() {
		return action;
	}

	/**
	 * Gets the quality of the country after performing this action
	 * @return quality score
	 */
	public Double getQuality() {
		return quality;
	}

	/**
	 * Gets the reward for the country after perroming this action
	 * @return the reward
	 */
	public Double getReward() {
		return reward;
	}

	/**
	 * Gets the position of this action within the schedule
	 * @return integer representing the schedule position
	 */
	public int getSchedulePosition() {
		return schedulePosition;
	}

	/**
	 * Gets the country that performed the action
	 * @return the country
	 */
	public Country getSelf() {
		return self;
	}

	/**
	 * Gets the delta of resources gained and lost through this action
	 * @return the delta
	 */
	public ResourceDelta getResourceDelta() {
		return delta;
	}

	/**
	 * Gets the type of this action
	 * @return the type
	 */
	public Action.Type getType() {
		return action.getType();
	}

	/**
	 * Gets the name of the resource that is the primary output of this action
	 * @return the resource name
	 */
	public String getName() {
		return action.getName();
	}

	/**
	 * Gets the role that the initiating country played in action
	 * @return the role 
	 */
	public abstract Role getRole();

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

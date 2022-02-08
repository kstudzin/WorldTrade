package edu.vanderbilt.studzikm;

public class ActionResult<T extends Action> {

	protected World world;
	protected T transform;
	protected Country performer;
	protected Double utility;

	public ActionResult(World world, T transform, Country performer) {
		this.world = world;
		this.transform = transform;
		this.performer = performer;
		this.utility = this.performer.computeUtility(world);
	}

	public World getWorld() {
		return world;
	}

	public T getAction() {
		return transform;
	}

	public Double getUtility() {
		return utility;
	}

	@Override
	public String toString() {
		return "ActionResult [world=" + world + ", transform=" + transform + ", performer=" + performer.getName() + ", utility="
				+ utility + "]";
	}
}

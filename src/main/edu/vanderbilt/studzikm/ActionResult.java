package edu.vanderbilt.studzikm;

public class ActionResult {

	private World world;
	private Action transform;
	private Country performer;
	private Double utility;

	public ActionResult(World world, Action transform, Country performer) {
		this.world = world;
		this.transform = transform;
		this.performer = performer;
		this.utility = this.performer.computeUtility(world);
	}

	public World getWorld() {
		return world;
	}

	public Action getAction() {
		return transform;
	}

	public Double getUtility() {
		return utility;
	}
}

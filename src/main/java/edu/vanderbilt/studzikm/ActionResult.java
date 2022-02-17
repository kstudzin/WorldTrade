package edu.vanderbilt.studzikm;

public class ActionResult<T extends Action> {

	World world;
	T transform;
	Country performer;
	Double quality;

	public ActionResult(World world, T transform, Country performer) {
		this.world = world;
		this.transform = transform;
		this.performer = performer;
		this.quality = this.performer.computeQuality();
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

	@Override
	public String toString() {
		return "ActionResult [transform=" + transform + ", performer=" + performer.getName() + ", quality="
				+ quality + ", world=" + world + "]";
	}
}

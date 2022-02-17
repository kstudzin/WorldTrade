package edu.vanderbilt.studzikm;

public class TransformResult extends ActionResult<Transform> {

	public TransformResult(World world, Transform transform, Country performer) {
		super(world, transform, performer);
	}

	public double getSuccessProbability(SuccessProbabilityComputation computation) {
		return computation.compute(this);
	}
}

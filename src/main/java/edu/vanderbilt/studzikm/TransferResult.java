package edu.vanderbilt.studzikm;

public class TransferResult extends ActionResult<Transfer> {

	private Country participant;

	public TransferResult(World world, Transfer transform, Country performer, Country participant) {
		super(world, transform, performer);
		this.participant = participant;
	}

	public Country getParticipant() {
		return participant;
	}

	public double getSuccessProbability(SuccessProbabilityComputation computation) {
		return computation.compute(this);
	}

	@Override
	public String toString() {
		return "TransferResult [participant=" + participant.getName() + ", transform=" + transform
				+ ", performer=" + performer.getName() + ", quality=" + quality + ", reward=" + reward + ", world=" + world + "]";
	}

}

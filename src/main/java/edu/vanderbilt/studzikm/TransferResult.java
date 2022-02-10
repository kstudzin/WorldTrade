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

	@Override
	public String toString() {
		return "TransferResult [participant=" + participant.getName() + ", transform=" + transform
				+ ", performer=" + performer.getName() + ", utility=" + utility + ", world=" + world + "]";
	}

}

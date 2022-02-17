package edu.vanderbilt.studzikm;

public class TransferResult extends ActionResult<Transfer> {

	private Country participant;
	private double participantReward;

	public TransferResult(World world, 
			Transfer transform, 
			Country performer, 
			Country participant, 
			RewardComputation rewardCompuation, 
			int schedulePosition) {
		super(world, transform, performer, rewardCompuation, schedulePosition);
		this.participant = participant;
		this.participantReward = rewardCompuation.computeReward(this, this::getParticipant);
	}

	public Country getParticipant() {
		return participant;
	}

	public double getSuccessProbability(SuccessProbabilityComputation computation) {
		return computation.compute(this);
	}

	public double getParicipantReward() {
		return participantReward;
	}

	@Override
	public String toString() {
		return "TransferResult [participant=" + participant.getName() + ", transform=" + transform
				+ ", performer=" + performer.getName() + ", quality=" + quality + ", reward=" + reward + ", world=" + world + "]";
	}

}

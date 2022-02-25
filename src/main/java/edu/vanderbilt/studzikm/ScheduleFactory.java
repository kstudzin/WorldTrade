package edu.vanderbilt.studzikm;

public class ScheduleFactory {

	private ExpectedUtilityComputation expectedUtilityComputation;

	public ScheduleFactory(ExpectedUtilityComputation expectedUtilityComputation) {
		this.expectedUtilityComputation = expectedUtilityComputation;
	}

	public Schedule create(SearchNode node) {
		return Schedule.create(node, expectedUtilityComputation);
	}
}
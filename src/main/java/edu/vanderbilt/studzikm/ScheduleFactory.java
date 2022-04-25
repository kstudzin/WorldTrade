package edu.vanderbilt.studzikm;

public class ScheduleFactory {

	public Schedule create(SearchNode node, double averageNodesGenerated) {
		return Schedule.create(node, averageNodesGenerated);
	}
}

package edu.vanderbilt.studzikm;

/**
 * Factory for creating schedules
 */
public class ScheduleFactory {

	/**
	 * Creates a schedule
	 * @param node the leaf node of the schedule
	 * @param averageNodesGenerated average number of nodes generated in the search
	 * @return the schedule
	 */
	public Schedule create(SearchNode node, double averageNodesGenerated) {
		return Schedule.create(node, averageNodesGenerated);
	}
}

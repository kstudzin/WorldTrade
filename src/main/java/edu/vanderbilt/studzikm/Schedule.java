package edu.vanderbilt.studzikm;

import java.util.Deque;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class Schedule {

	private Deque<ScheduleItem> items = new LinkedList<>();

	private Schedule () {

	}

	public static Schedule create(SearchNode result, 
			ExpectedUtilityComputation expectedUtilityComputation) {
		Schedule schedule = new Schedule();

		while (result.getDepth() != 0) {
			ScheduleItem item = createItem(result, expectedUtilityComputation);
			schedule.items.addFirst(item);
			result = result.getParent();
		}

		return schedule;
	}

	private static ScheduleItem createItem(SearchNode result, 
			ExpectedUtilityComputation expectedUtilityComputation) {
		return ScheduleItem.create(result, expectedUtilityComputation);
	}

	@Override
	public String toString() {
		return "[ \n" + items.stream()
		.map(ScheduleItem::toStringBuilder)
		.map(StringBuilder::toString)
		.collect(Collectors.joining("\n")) + "\n]";
	}
}

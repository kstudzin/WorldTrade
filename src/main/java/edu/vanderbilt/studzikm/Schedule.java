package edu.vanderbilt.studzikm;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Schedule {

	private List<ScheduleItem> items = new LinkedList<>();

	private Schedule () {

	}

	public static Schedule create(SearchNode result, 
			ExpectedUtilityComputation expectedUtilityComputation) {
		Schedule schedule = new Schedule();

		while (result.getDepth() != 0) {
			ScheduleItem item = createItem(result, expectedUtilityComputation);
			schedule.items.add(item);
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

	public int size() {
		return items.size();
	}

	public ScheduleItem get(int index) {
		return items.get(index);
	}

}

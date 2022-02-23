package edu.vanderbilt.studzikm;

import java.util.Deque;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class Schedule {

	private Deque<ScheduleItem> items;
	private Schedule () {
		items = new LinkedList<>();
	}

	public static Schedule create(SearchNode result) {
		Schedule schedule = new Schedule();

		while (result.getDepth() != 0) {
			ScheduleItem item = createItem(result);
			schedule.items.addFirst(item);
			result = result.getParent();
		}

		return schedule;
	}

	private static ScheduleItem createItem(SearchNode result) {
		return ScheduleItem.create(result);
	}

	@Override
	public String toString() {
		return "[ \n" + items.stream()
		.map(ScheduleItem::toStringBuilder)
		.map(StringBuilder::toString)
		.collect(Collectors.joining("\n")) + "\n]";
	}
}

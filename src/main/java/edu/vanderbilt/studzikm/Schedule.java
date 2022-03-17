package edu.vanderbilt.studzikm;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Schedule implements Iterable<ScheduleItem> {

	private List<ScheduleItem> items = new LinkedList<>();
	double averageNodesGenerated;

	private Schedule () {

	}

	public static Schedule create(SearchNode result,
								  ExpectedUtilityComputation expectedUtilityComputation,
								  double averageNodesGenerated) {
		Schedule schedule = new Schedule();
		schedule.averageNodesGenerated = averageNodesGenerated;

		while (result.getDepth() != 0) {
			ScheduleItem item = createItem(result, expectedUtilityComputation);
			schedule.items.add(item);
			result = result.getParent();
		}

		Collections.reverse(schedule.items);
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

	@Override
	public Iterator<ScheduleItem> iterator() {
		return items.iterator();
	}

	public Stream<ScheduleItem> stream() {
		return items.stream();
	}
}

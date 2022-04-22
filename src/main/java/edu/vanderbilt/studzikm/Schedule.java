package edu.vanderbilt.studzikm;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Schedule implements Iterable<ScheduleItem> {

	private List<ScheduleItem> items = new LinkedList<>();
	private double averageNodesGenerated;

	private Schedule () {

	}

	public static Schedule create(SearchNode result,
								  ExpectedUtilityComputation expectedUtilityComputation,
								  double averageNodesGenerated) {
		Schedule schedule = new Schedule();
		schedule.averageNodesGenerated = averageNodesGenerated;

		Set<String> countries = new HashSet<>();

		while (result.getDepth() != 0) {
			if (result.getAction() instanceof TransferResult) {
				String involvedParty = ((TransferResult)result.getAction()).getOther().getName();
				countries.add(involvedParty);
			}
			ScheduleItem item = createItem(result, expectedUtilityComputation, countries);
			schedule.items.add(item);
			result = result.getParent();
		}

		Collections.reverse(schedule.items);
		return schedule;
	}

	private static ScheduleItem createItem(SearchNode result, 
			ExpectedUtilityComputation expectedUtilityComputation,
										   Set<String> countries) {
		return ScheduleItem.create(result, expectedUtilityComputation, countries);
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

	public double getAverageNodesGenerated() {
		return averageNodesGenerated;
	}
}

package edu.vanderbilt.studzikm;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents a country's schedule of actions to perform
 */
public class Schedule implements Iterable<ScheduleItem> {

	private List<ScheduleItem> items = new LinkedList<>();
	private double averageNodesGenerated;

	private Schedule () {

	}

	/**
	 * Factory method to create schedule
	 * @param result the leaf node to create the schedule from
	 * @param averageNodesGenerated average number of nodes generated at each search level
	 * @return the schedule
	 */
	public static Schedule create(SearchNode result,
								  double averageNodesGenerated) {
		Schedule schedule = new Schedule();
		schedule.averageNodesGenerated = averageNodesGenerated;

		while (result.getDepth() != 0) {
			ScheduleItem item = createItem(result);
			schedule.items.add(item);
			result = result.getParent();
		}

		Collections.reverse(schedule.items);
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

	/**
	 * Gets the number of items in the schedule
	 * @return the number of items
	 */
	public int size() {
		return items.size();
	}

	/**
	 * Gets a schedule item at a specific position
	 * @param index the position of the schedule item to retrieve
	 * @return the schedule item
	 */
	public ScheduleItem get(int index) {
		return items.get(index);
	}

	/**
	 * Iterator of schedule items
	 * @return the iterator
	 */
	@Override
	public Iterator<ScheduleItem> iterator() {
		return items.iterator();
	}

	/**
	 * Stream of schedule items
	 * @return the stream
	 */
	public Stream<ScheduleItem> stream() {
		return items.stream();
	}

	/**
	 * Gets the average number of nodes generated at each search level
	 * @return the average number of nodes generated
	 */
	public double getAverageNodesGenerated() {
		return averageNodesGenerated;
	}
}

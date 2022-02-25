package edu.vanderbilt.studzikm;

import java.util.Comparator;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Search {

	Logger log = LogManager.getLogger(Search.class);

	private StateGenerator stateGenerator;
	private SearchNodeFactory nodeFactory;
	private ScheduleFactory scheduleFactory;
	private Deque<SearchNode> frontier = new LinkedList<>();

	// Comparing only the rewards for self leads to a partial order. Two results that
	// are mutually unordered may sort differently on different runs. This is
	// difficult for testing. The method below  enforces a total order, that is
	// somewhat arbitrary.
	Comparator<ActionResult<?>> comparator = (x, y) -> {
		int compareResult = x.getReward().compareTo(y.getReward());

		if (compareResult == 0 && 
			x instanceof TransferResult &&
			y instanceof TransferResult) {

			// If self rewards are equal, compare rewards for other 
			TransferResult xAsTransferResult = (TransferResult) x;
			TransferResult yAsTransferResult = (TransferResult) y;
			compareResult = xAsTransferResult.getOtherReward()
					.compareTo(yAsTransferResult.getOtherReward());

		} else if (compareResult == 0 &&
				x instanceof TransformResult) {
			// prefer transforms to transfers
			return 1;
		} else if (compareResult == 0 && 
				y instanceof TransformResult) {
			// prefer transforms to transfers
			return -1;
		}

		// This comparison isn't particularly meaningful. It simply creates a 
		// deterministic ordering which is useful for tests
		if (compareResult == 0) {
			compareResult = Integer.compare(x.hashCode(), y.hashCode());
		}

		return compareResult;
	};

	public Search(StateGenerator stateGenerator,
			SearchNodeFactory nodeFactory,
			ScheduleFactory scheduleFactory) {
		this.stateGenerator = stateGenerator;
		this.nodeFactory = nodeFactory;
		this.scheduleFactory = scheduleFactory;
	}

	public Schedule search(World initState, Country country, int maxDepth) {
		frontier.addFirst(nodeFactory.createRoot(initState, country));

		int depth = 0;

		while (!frontier.isEmpty()) {
			SearchNode n = frontier.removeFirst();
			log.debug("Expanding node: " + n);

			depth++;
			List<SearchNode> next = stateGenerator.generateStates(n.getState(), n.getCountry(), depth)
					.stream()
					.map(state -> (ActionResult<?>)state)
					.sorted(comparator)
					.map(e -> nodeFactory.createNode(n, e))
					.collect(Collectors.toList());

			log.debug("Found " + next.size() + " next states");
			next.forEach(node -> log.trace(node.getAction()));

			if (next.isEmpty()) {
				continue;
			} else if (depth >= maxDepth) {
				SearchNode maxReward = next.get(next.size() - 1);
				return scheduleFactory.create(maxReward);
			}

			next.stream()
			.peek(n::addChild)
			.forEach(frontier::addFirst);

		}

		return null;
	}

}

package edu.vanderbilt.studzikm;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.checkerframework.checker.units.qual.A;

public class Search {

	Logger log = LogManager.getLogger(Search.class);

	private StateGenerator stateGenerator;
	private SearchNodeFactory nodeFactory;
	private ScheduleFactory scheduleFactory;
	private Frontier frontier;
	private Reached reached;

	public Search(StateGenerator stateGenerator,
			SearchNodeFactory nodeFactory,
			Frontier frontier,
			Reached reached,
			ScheduleFactory scheduleFactory) {
		this.stateGenerator = stateGenerator;
		this.nodeFactory = nodeFactory;
		this.frontier = frontier;
		this.reached = reached;
		this.scheduleFactory = scheduleFactory;
	}

	public Schedule search(World initState, Country country, int maxDepth) {
		return search(initState, country, maxDepth, 1).get(0);
	}

	public List<Schedule> search(World initState, Country country, int maxDepth, int numSearchResults) {
		frontier.add(nodeFactory.createRoot(initState, country));

		int depth = 0;

		List<Integer> numResults = new ArrayList<>();
		while (!frontier.isEmpty()) {
			SearchNode n = frontier.getNext();
			log.debug("Expanding node: " + n);

			depth++;
			List<SearchNode> next = stateGenerator.generateStates(n.getState(), n.getCountry(), depth)
					.map(state -> (ActionResult<?>)state)
					.map(e -> nodeFactory.createNode(n, e))
					.collect(Collectors.toList());

			log.debug("Found " + next.size() + " next states");
			next.forEach(node -> log.trace(node.getAction()));
			numResults.add(next.size());

			if (next.isEmpty()) {
				continue;
			} else {
				frontier.add(next, reached);
			}

			if (depth >= maxDepth) {
				Double averageNodes = numResults.stream()
								.mapToInt(Integer::intValue)
								.average()
								.orElse(0.0);

				return IntStream.range(0, numSearchResults)
						.mapToObj(i -> frontier.getNext())
						.map(node -> scheduleFactory.create(node, averageNodes))
						.collect(Collectors.toList());
			}

		}

		return null;
	}

}

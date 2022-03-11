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
		frontier.add(nodeFactory.createRoot(initState, country));

		int depth = 0;

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

			if (next.isEmpty()) {
				continue;
			} else {
				frontier.add(next, reached);
			}

			if (depth >= maxDepth) {
					SearchNode maxReward = frontier.getNext();
					return scheduleFactory.create(maxReward);
			}

		}

		return null;
	}

}

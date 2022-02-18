package edu.vanderbilt.studzikm;

import java.util.ArrayList;
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
	private Deque<SearchNode> frontier = new LinkedList<>();

	public Search(StateGenerator stateGenerator,
			SearchNodeFactory nodeFactory) {
		this.stateGenerator = stateGenerator;
		this.nodeFactory = nodeFactory;
	}

	public List<ActionResult<? extends Action>> search(World initState, Country country, int maxDepth) {
		frontier.addFirst(nodeFactory.createRoot(initState, country));

		int depth = 0;

		while (!frontier.isEmpty()) {
			SearchNode n = frontier.removeFirst();
			log.debug("Expanding node: " + n);

			depth++;
			List<SearchNode> next = stateGenerator.generateStates(n.getState(), country, depth)
					.stream()
					.map(state -> (ActionResult<?>)state)
					.sorted((x, y) -> x.getReward().compareTo(y.getReward()))
					.map(e -> nodeFactory.createNode(n, e))
					.collect(Collectors.toList());

			log.debug("Found " + next.size() + " next states");
			next.forEach(node -> log.trace(node.getAction()));

			if (next.isEmpty()) {
				continue;
			} else if (depth >= maxDepth) {
				SearchNode maxUtility = next.get(next.size() - 1);
				return retrieveActions(maxUtility);
			}

			next.stream()
			.peek(n::addChild)
			.forEach(frontier::addFirst);

		}

		return null;
	}

	private List<ActionResult<? extends Action>> retrieveActions(SearchNode maxUtility) {

		SearchNode parent = maxUtility;
		List<ActionResult<?>> actions = new ArrayList<>();

		while (parent.getDepth() != 0) {
			actions.add(parent.getAction());
			parent = parent.getParent();
		}

		return actions;
	}
}

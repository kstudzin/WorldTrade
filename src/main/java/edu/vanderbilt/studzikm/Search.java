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

			depth++;
			int effectivelyFinalDepth = depth; 
			List<SearchNode> next = stateGenerator.generateStates(n.getState(), country)
					.stream()
					.map(e -> nodeFactory.createNode(n, e, effectivelyFinalDepth))
					.sorted((x, y) -> y.getReward().compareTo(x.getReward()))
					.collect(Collectors.toList());

			log.debug("Found " + next.size() + " next states");
			next.forEach(node -> log.trace(node.getAction()));

			if (next.isEmpty()) {
				continue;
			}
			SearchNode maxUtility = next.get(0);

			if (depth >= maxDepth) {
				return retrieveActions(maxUtility);
			}

			next.forEach(node -> {
				n.addChild(node); 
				frontier.addFirst(node);
				}
			);

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

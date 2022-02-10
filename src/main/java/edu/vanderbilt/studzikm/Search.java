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
	private Deque<SearchNode> frontier = new LinkedList<>();

	public Search(StateGenerator stateGenerator,
			World initState) {
		this.stateGenerator = stateGenerator;
		frontier.addFirst(new SearchNode(initState));
	}

	public List<ActionResult<? extends Action>> search(Country country, double threshold, int maxDepth) {

		int depth = 0;

		while (!frontier.isEmpty()) {
			SearchNode n = frontier.removeFirst();
			List<SearchNode> next = stateGenerator.generateStates(n.getState(), country)
					.stream()
					.sorted((x, y) -> y.getUtility().compareTo(x.getUtility()))
					.map(e -> new SearchNode(e.getWorld(), n, e))
					.collect(Collectors.toList());
			depth++;

			System.out.println("\nFound " + next.size() + " next states\n");
			next.forEach(node -> System.out.println(node.getAction()));

			SearchNode maxUtility = next.get(0);
			if (Double.compare(threshold, country.computeUtility(maxUtility.getState())) <= 0 || depth >= maxDepth) {
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
		while (parent.getAction() != null) {
			actions.add(parent.getAction());
			parent = parent.getParent();
		}
		return actions;
	}
}

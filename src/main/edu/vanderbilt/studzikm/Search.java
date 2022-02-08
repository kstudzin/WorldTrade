package edu.vanderbilt.studzikm;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Search {

	private StateGenerator stateGenerator;
	private Deque<SearchNode> frontier = new LinkedList<>();

	public Search(StateGenerator stateGenerator,
			World initState) {
		this.stateGenerator = stateGenerator;
		frontier.addFirst(new SearchNode(initState));
	}

	public List<Action> search(Country country, double threshold, int maxDepth) {

		int depth = 0;

		while (!frontier.isEmpty()) {
			SearchNode n = frontier.removeFirst();
			List<SearchNode> next = stateGenerator.generateStates(n.getState(), country)
					.stream()
					.sorted((x, y) -> (int)(x.getUtility() - y.getUtility()))
					.map(e -> new SearchNode(e.getWorld(), n, e.getAction()))
					.collect(Collectors.toList());
			depth++;

			SearchNode maxUtility = next.get(0);
			if (Double.compare(threshold, country.computeUtility(maxUtility.getState())) <= 0 || depth >= maxDepth) {
				return retrieveActions(maxUtility);
			}

			next.stream()
			.forEach(node -> {
				n.addChild(node); 
				frontier.addFirst(node);
				}
			);

		}

		return null;
	}

	private List<Action> retrieveActions(SearchNode maxUtility) {

		SearchNode parent = maxUtility.getParent();
		List<Action> actions = new ArrayList<>();
		while (parent != null) {
			actions.add(parent.getAction());
			parent = parent.getParent();
		}
		return actions;
	}
}

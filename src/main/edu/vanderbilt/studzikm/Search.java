package edu.vanderbilt.studzikm;

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
		frontier.addFirst(new SearchNode(initState, null, null));
	}

	public World search(Country country, double threshold, int maxDepth) {

		int depth = 0;

		while (!frontier.isEmpty()) {
			SearchNode n = frontier.removeFirst();
			List<SearchNode> next = stateGenerator.generateStates(n.getState(), country)
					.entrySet() // Entry includes World and Transform
					.stream()
					.sorted((x, y) -> (int)(country.computeUtility(x.getKey()) - country.computeUtility(y.getKey())))
					.map(e -> new SearchNode(e.getKey(), n, e.getValue()))
					.collect(Collectors.toList());
			
			World maxUtility = next.get(0).getState();
			if (Double.compare(threshold, country.computeUtility(maxUtility)) <= 0 || depth > maxDepth) {
				return maxUtility;
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
}

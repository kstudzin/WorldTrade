package edu.vanderbilt.studzikm;

import edu.vanderbilt.studzikm.planning.Planner;


public class SearchNodeFactory {

	private final ExpectedUtilityComputation expectedUtilityComputation;
	private Planner planner;

	public SearchNodeFactory(ExpectedUtilityComputation expectedUtilityComputation,
							 Planner planner) {
		this.expectedUtilityComputation = expectedUtilityComputation;
		this.planner = planner;
	}

	public SearchNode createNode(SearchNode parent, ActionResult<?> actionResult) {
		return new SearchNode(parent, actionResult);
	}

	public SearchNode createRoot(World initState, Country self) {
		return new SearchNode(initState,
				self,
				expectedUtilityComputation,
				planner);
	}
}

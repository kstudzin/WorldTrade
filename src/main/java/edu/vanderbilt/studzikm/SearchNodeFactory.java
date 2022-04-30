package edu.vanderbilt.studzikm;

import edu.vanderbilt.studzikm.planning.Planner;

/**
 * Factory for creating search nodes
 */
public class SearchNodeFactory {

	private final ExpectedUtilityComputation expectedUtilityComputation;
	private Planner planner;

	/**
	 * Creates a factory for creating search nodes
	 * @param expectedUtilityComputation the expecuted utility computation nodes should use to
	 *                                      compute their expected utility
	 * @param planner the planner nodes should use when computing expected utility
	 */
	public SearchNodeFactory(ExpectedUtilityComputation expectedUtilityComputation,
							 Planner planner) {
		this.expectedUtilityComputation = expectedUtilityComputation;
		this.planner = planner;
	}

	/**
	 * Creates a search node from a parent node and an action result
	 * @param parent the parent of the created node
	 * @param actionResult the action result this node represents
	 * @return the created node
	 */
	public SearchNode createNode(SearchNode parent, ActionResult<?> actionResult) {
		return new SearchNode(parent, actionResult);
	}

	/**
	 * Creates a root search node from a world state and a country
	 * @param initState the initial world state
	 * @param self the initial state of the country the search is for
	 * @return the created root node
	 */
	public SearchNode createRoot(World initState, Country self) {
		return new SearchNode(initState,
				self,
				expectedUtilityComputation,
				planner);
	}
}

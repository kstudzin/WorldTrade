package edu.vanderbilt.studzikm;

public class SearchNodeFactory {

	public SearchNode createNode(SearchNode parent, ActionResult<?> actionResult) {
		return new SearchNode(parent, actionResult);
	}

	public SearchNode createRoot(World initState, Country self) {
		return new SearchNode(initState, self);
	}
}

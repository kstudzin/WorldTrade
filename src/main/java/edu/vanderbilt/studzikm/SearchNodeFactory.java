package edu.vanderbilt.studzikm;

public class SearchNodeFactory {

	private RewardComputationBuilder rewardComputationBuilder;

	public SearchNodeFactory(RewardComputationBuilder rewardComputationBuilder) {
		this.rewardComputationBuilder = rewardComputationBuilder;
	}

	public SearchNode createNode(SearchNode parent, ActionResult<?> actionResult, Integer depth) {
		return new SearchNode(parent, actionResult, depth);
	}

	public SearchNode createRoot(World initState, Country self) {
		rewardComputationBuilder.setInitialQuality(self.computeQuality());
		return new SearchNode(initState, self, rewardComputationBuilder.build());
	}
}

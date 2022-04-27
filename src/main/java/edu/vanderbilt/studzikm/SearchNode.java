package edu.vanderbilt.studzikm;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import edu.vanderbilt.studzikm.planning.Planner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SearchNode {
	Logger log = LogManager.getLogger(SearchNode.class);

	private Planner planner;
	private SearchNode parent;
	private Set<SearchNode> children = new HashSet<>();
	private ActionResult<?> actionResult;
	private Set<String> involvedParties;
	private ExpectedUtilityComputation expectedUtilityComputation;
	private Double expectedUtility;

	public SearchNode(SearchNode parent,
					  ActionResult<?> actionResult) {
		if (parent == null) throw new NullPointerException("If parent is null use SearchNode(World) constructor");
		if (actionResult == null) throw new NullPointerException("If actionResult is null use SearchNode(World) constructor");

		this.parent = parent;
		this.actionResult = actionResult;
		this.involvedParties = parent.involvedParties == null ?
				new HashSet<>() :
				new HashSet<>(parent.involvedParties);
		if (actionResult.getType() == Action.Type.TRANSFER) {
			String otherName = ((TransferResult)actionResult).getOther().getName();
			this.involvedParties.add(otherName);
		}
		this.expectedUtilityComputation = parent.expectedUtilityComputation;
		this.planner = parent.planner.copy();
	}


	public SearchNode(World current, Country self,
					  ExpectedUtilityComputation expectedUtilityComputation,
					  Planner planner) {
		this.actionResult = new NullActionResult(current, new NullAction(), self, new DefaultRewardComputation(), 0, null);
		this.expectedUtilityComputation = expectedUtilityComputation;
		this.planner = planner;
	}


	public World getState() {
		return actionResult.getWorld();
	}

	public Country getCountry() {
		return actionResult.getSelf();
	}

	public SearchNode getParent() {
		return parent;
	}

	public ActionResult<?> getActionResult() {
		return actionResult;
	}

	public void setActionResult(ActionResult<?> actionResult) {
		this.actionResult = actionResult;
	}

	public Double getQuality() {
		return actionResult.getQuality();
	}

	public Double getReward() {
		return actionResult.getReward();
	}

	public Set<SearchNode> getChildren() {
		return Collections.unmodifiableSet(children);
	}


	public void setChildren(Set<SearchNode> children) {
		this.children = children;
	}


	public void addChild(SearchNode child) {
		this.children.add(child);
	}

	public int getDepth() {
		return actionResult.getSchedulePosition();
	}

	public Set<String> getInvolvedParties() {
		return involvedParties;
	}

	public double computeExpectedUtility() {
		if (expectedUtility == null) {
			expectedUtility = expectedUtilityComputation.compute(actionResult,
					actionResult.getWorld(),
					involvedParties,
					planner);
		}
		return expectedUtility;
	}

	@Override
	public String toString() {
		return "SearchNode [parent=" + (parent == null ? "none" : parent.hashCode()) + ", "
				+ "children=" + children.size() + ", "
				+ "expectedUtility=" + expectedUtility + ", "
				+ "action=" + actionResult + "]";
	}

}

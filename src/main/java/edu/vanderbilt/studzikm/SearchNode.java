package edu.vanderbilt.studzikm;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SearchNode {

	private SearchNode parent;
	private Set<SearchNode> children = new HashSet<>();
	private ActionResult<?> action;
	private Integer depth;
	private RewardComputation rewardComputation;
	private double reward;

	public SearchNode(SearchNode parent, ActionResult<?> action, Integer depth) {
		if (parent == null) throw new NullPointerException("If parent is null use SearchNode(World) constructor");
		if (action == null) throw new NullPointerException("If action is null use SearchNode(World) constructor");

		this.parent = parent;
		this.action = action;
		this.depth = depth;
		this.rewardComputation = this.parent.rewardComputation;
		this.reward = this.rewardComputation.computeReward(this);
	}


	public SearchNode(World current, Country self, RewardComputation rewardComputation) {
		this.action = new ActionResult<>(current, new NullAction(), self);
		this.rewardComputation = rewardComputation;
		this.depth = 0;
		this.reward = 0;
	}


	public World getState() {
		return action.getWorld();
	}


	public SearchNode getParent() {
		return parent;
	}


	public ActionResult<?> getAction() {
		return action;
	}

	public void setAction(ActionResult<?> action) {
		this.action = action;
	}

	public Double getQuality() {
		return action.getQuality();
	}

	public Integer getDepth() {
		return depth;
	}

	public Double getReward() {
		return reward;
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

	@Override
	public String toString() {
		return "SearchNode [parent=" + parent.hashCode() + ", children=" + children.size() + ", action=" + action
				+ ", state=" + getState() + "]";
	}

}

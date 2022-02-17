package edu.vanderbilt.studzikm;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SearchNode {
	Logger log = LogManager.getLogger(SearchNode.class);

	private SearchNode parent;
	private Set<SearchNode> children = new HashSet<>();
	private ActionResult<?> action;

	public SearchNode(SearchNode parent, ActionResult<?> action) {
		if (parent == null) throw new NullPointerException("If parent is null use SearchNode(World) constructor");
		if (action == null) throw new NullPointerException("If action is null use SearchNode(World) constructor");

		this.parent = parent;
		this.action = action;
	}


	public SearchNode(World current, Country self) {
		this.action = new ActionResult<>(current, new NullAction(), self);
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

	public Double getReward() {
		return action.getReward();
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
		return action.getSchedulePosition();
	}

	@Override
	public String toString() {
		return "SearchNode [parent=" + parent.hashCode() + ", children=" + children.size() + ", action=" + action
				+ ", state=" + getState() + "]";
	}

}

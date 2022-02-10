package edu.vanderbilt.studzikm;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SearchNode {


	private World state;
	private SearchNode parent;
	private Set<SearchNode> children = new HashSet<>();
	private ActionResult<?> action;

	public SearchNode(World current, SearchNode parent, ActionResult<?> action) {
		if (parent == null) throw new NullPointerException("If parent is null use SearchNode(World) constructor");
		if (action == null) throw new NullPointerException("If action is null use SearchNode(World) constructor");

		this.state = current;
		this.parent = parent;
		this.action = action;
	}


	public SearchNode(World current) {
		this.state = current;
	}


	public World getState() {
		return state;
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
				+ ", state=" + state + "]";
	}

}

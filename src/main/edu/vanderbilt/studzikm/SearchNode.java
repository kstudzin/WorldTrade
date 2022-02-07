package edu.vanderbilt.studzikm;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SearchNode {

	private World state;
	private SearchNode parent;
	private Set<SearchNode> children = new HashSet<>();
	private Transform action;
	
	public SearchNode(World current, SearchNode parent, Transform action) {
		this.state = current;
		this.parent = parent;
		this.action = action;
	}
	
	
	public World getState() {
		return state;
	}


	public SearchNode getParent() {
		return parent;
	}


	public Transform getAction() {
		return action;
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
}

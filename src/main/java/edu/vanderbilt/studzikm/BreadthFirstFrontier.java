package edu.vanderbilt.studzikm;

import java.util.Collection;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Implementation of a frontier used for a breadth first search
 */
public class BreadthFirstFrontier implements Frontier {

    private SearchNodeComparator minSearchNodeComparator = new SearchNodeComparator();
    private Comparator<SearchNode> maxSearchNodeComparator = (x, y) -> minSearchNodeComparator.compare(y, x);
    private PriorityQueue<SearchNode> frontier;

    /**
     * Creates a breadth first frontier
     *
     * @param size initial size of the froniter
     */
    public BreadthFirstFrontier(int size) {
        this.frontier = new PriorityQueue<>(size, maxSearchNodeComparator);
    }

    /**
     * Gets the next search node to expand in the search
     *
     * @return the next search node
     */
    @Override
    public SearchNode getNext() {
        return frontier.poll();
    }

    /**
     * Adds a
     * @param next
     * @param reached
     */
    @Override
    public void add(Collection<SearchNode> next, Reached reached) {
        next.stream()
                .filter(node -> !reached.contains(node))
                .forEach(frontier::offer);
    }

    @Override
    public void add(SearchNode first) {
        frontier.offer(first);
    }

    @Override
    public boolean isEmpty() {
        return frontier.isEmpty();
    }
}

package edu.vanderbilt.studzikm;

import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;

/**
 * Frontier that uses a heuristic depth first search to organize search nodes
 */
public class HeuristicDepthFirstFrontier implements Frontier {

    private SearchNodeComparator comparator = new SearchNodeComparator();
    private Deque<SearchNode> frontier = new LinkedList<>();
    private SearchNode first;

    /**
     * Get the next search node to explore
     * @return the search node to explore
     */
    @Override
    public SearchNode getNext() {
        first = frontier.removeFirst();
        return first;
    }

    /**
     * Adds search nodes to the frontier
     * @param next the collection of nodes to add
     * @param reached object representing the set of search nodes reached so far. Avoids searching a node more than once.
     */
    @Override
    public void add(Collection<SearchNode> next, Reached reached) {
        next.stream()
                .filter(node -> !reached.contains(node))
                .peek(first::addChild)
                .sorted(comparator)
                .forEach(frontier::addFirst);
    }

    /**
     * Adds the first node, i.e. the root node, to the frontier
     * @param first the search node to add
     */
    @Override
    public void add(SearchNode first) {
        frontier.addFirst(first);
    }

    /**
     * Determines if this frontier is empty
     * @return true if the frontier is empty
     */
    @Override
    public boolean isEmpty() {
        return frontier.isEmpty();
    }
}

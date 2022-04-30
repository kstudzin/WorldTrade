package edu.vanderbilt.studzikm;

import java.util.Collection;

/**
 * Interface representing the Frontier of a search. Tracks search nodes that still need to be explored.
 */
public interface Frontier {

    /**
     * Gets the next search node to expand
     * @return
     */
    SearchNode getNext();

    /**
     * Add new search nodes to the frontier
     * @param next the collection of nodes to add
     * @param reached object representing the set of search nodes reached so far. Avoids searching a node more than once.
     */
    void add(Collection<SearchNode> next, Reached reached);

    /**
     * Add the first search node, i.e., the root, to the frontier.
     * @param first the search node to add
     */
    void add(SearchNode first);

    /**
     * Determine if the frontier is empty
     * @return true if there are no search elements in this frontier
     */
    boolean isEmpty();
}

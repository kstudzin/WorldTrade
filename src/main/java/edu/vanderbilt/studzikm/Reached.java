package edu.vanderbilt.studzikm;

/**
 * Interface for tracking nodes that have been searched
 */
public interface Reached {

    /**
     * Determines if the given node has been searched
     * @param node the node to consider
     * @return true if the given search node has already been expanded
     */
    boolean contains(SearchNode node);
}

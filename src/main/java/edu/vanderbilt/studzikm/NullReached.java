package edu.vanderbilt.studzikm;

/**
 * Implementation of reached with no functionality. Placeholder for implementation later
 */
public class NullReached implements Reached {

    /**
     * Determines in the given node has already been searched
     * @param node the node to consider
     * @return true if the node has already been searched
     */
    @Override
    public boolean contains(SearchNode node) {
        return false;
    }
}

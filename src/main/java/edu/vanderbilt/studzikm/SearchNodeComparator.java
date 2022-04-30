package edu.vanderbilt.studzikm;

import java.util.Comparator;

/**
 * Compare search nodes based on expected utility.
 * If expected utilities are the same, compare search node strings. This is
 * not meaningful but it is useful for writing deterministic tests.
 */
public class SearchNodeComparator implements Comparator<SearchNode> {

    /**
     * Compares search nodes for the purpose of sorting
     * @param x one search node to compare
     * @param y second search node to compare
     * @return 0 if nodes are are equale, -1 if x is smaller, 1 if x is bigger
     */
    @Override
    public int compare(SearchNode x, SearchNode y) {
        double xEu = x.computeExpectedUtility();
        double yEu = y.computeExpectedUtility();
        int result = Double.compare(xEu, yEu);

        if (result == 0) {
            result = x.toString().compareTo(y.toString());
        }

        return result;
    }

}

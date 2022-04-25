package edu.vanderbilt.studzikm;

import java.util.Comparator;

public class SearchNodeComparator implements Comparator<SearchNode> {

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

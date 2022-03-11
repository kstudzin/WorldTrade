package edu.vanderbilt.studzikm;

import java.util.Comparator;

public class SearchNodeComparator implements Comparator<SearchNode> {

    @Override
    public int compare(SearchNode x, SearchNode y) {
        ActionResultComparator arc = new ActionResultComparator();
        return arc.compare(x.getAction(), y.getAction());
    }

}

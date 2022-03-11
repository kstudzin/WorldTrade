package edu.vanderbilt.studzikm;

import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;

public class HeuristicDepthFirstFrontier implements Frontier {

    private SearchNodeComparator comparator = new SearchNodeComparator();
    private Deque<SearchNode> frontier = new LinkedList<>();
    private SearchNode first;

    @Override
    public SearchNode getNext() {
        first = frontier.removeFirst();
        return first;
    }

    @Override
    public void add(Collection<SearchNode> next, Reached reached) {
        next.stream()
                .filter(node -> !reached.contains(node))
                .peek(first::addChild)
                .sorted(comparator)
                .forEach(frontier::addFirst);
    }

    @Override
    public void add(SearchNode first) {
        frontier.addFirst(first);
    }

    @Override
    public boolean isEmpty() {
        return frontier.isEmpty();
    }
}

package edu.vanderbilt.studzikm;

import java.util.Collection;
import java.util.Comparator;
import java.util.PriorityQueue;

public class BreadthFirstFrontier implements Frontier {

    private SearchNodeComparator minSearchNodeComparator = new SearchNodeComparator();
    private Comparator<SearchNode> maxSearchNodeComparator = (x, y) -> minSearchNodeComparator.compare(y, x);
    private PriorityQueue<SearchNode> frontier;

    public BreadthFirstFrontier(int size) {
        this.frontier = new PriorityQueue<>(size, maxSearchNodeComparator);
    }

    @Override
    public SearchNode getNext() {
        return frontier.poll();
    }

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

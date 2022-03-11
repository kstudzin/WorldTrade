package edu.vanderbilt.studzikm;

import java.util.Collection;

public interface Frontier {

    SearchNode getNext();

    void add(Collection<SearchNode> next);

    void add(SearchNode first);

    boolean isEmpty();
}
